package data.utils;

import jakarta.servlet.http.Part;
import java.io.File;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.Duration;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class UploadUtils {

    private static final Logger LOGGER = Logger.getLogger(UploadUtils.class.getName());
    private static final Set<String> ALLOWED_EXTENSIONS = Set.of(".jpg", ".jpeg", ".png", ".webp");
    private static final HttpClient HTTP_CLIENT = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(15))
            .build();

    private UploadUtils() {}

    public static String getFileExtension(Part filePart) {
        String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex > 0) {
            return fileName.substring(dotIndex).toLowerCase();
        }
        return "";
    }

    public static boolean isAllowedImageExtension(String extension) {
        return ALLOWED_EXTENSIONS.contains(extension);
    }

    public static String saveUploadedFile(Part filePart, String uploadDirPath) throws Exception {
        String ext = getFileExtension(filePart);
        if (!isAllowedImageExtension(ext)) {
            return null;
        }
        String uniqueName = UUID.randomUUID().toString() + ext;

        if (isSupabaseConfigured()) {
            String supabaseUrl = uploadToSupabase(filePart, uniqueName);
            if (supabaseUrl != null) {
                return supabaseUrl;
            }
            LOGGER.log(Level.WARNING, "Upload lên Supabase thất bại, tự động chuyển về lưu trữ cục bộ.");
        }

        File uploadDir = new File(uploadDirPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        File runtimeFile = new File(uploadDir, uniqueName);
        filePart.write(runtimeFile.getAbsolutePath());

        syncToSourceDirectory(runtimeFile, uploadDirPath, uniqueName);

        return uniqueName;
    }

    private static boolean isSupabaseConfigured() {
        return Constants.SUPABASE_URL != null && !Constants.SUPABASE_URL.trim().isEmpty()
            && Constants.SUPABASE_KEY != null && !Constants.SUPABASE_KEY.trim().isEmpty();
    }

    private static String uploadToSupabase(Part filePart, String fileName) {
        try {
            String baseUrl = Constants.SUPABASE_URL.replaceAll("/+$", "");
            String bucket = Constants.SUPABASE_BUCKET.trim();
            String folder = Constants.SUPABASE_FOLDER.replaceAll("^/+|/+$", "");
            String objectPath = folder.isEmpty() ? fileName : (folder + "/" + fileName);

            String endpoint = baseUrl + "/storage/v1/object/" + bucket + "/" + objectPath;

            String contentType = filePart.getContentType();
            if (contentType == null || contentType.isEmpty()) {
                contentType = "image/jpeg";
            }

            byte[] fileBytes;
            try (InputStream is = filePart.getInputStream()) {
                fileBytes = is.readAllBytes();
            }

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(endpoint))
                    .header("apikey", Constants.SUPABASE_KEY.trim())
                    .header("Authorization", "Bearer " + Constants.SUPABASE_KEY.trim())
                    .header("Content-Type", contentType)
                    .header("x-upsert", "true")
                    .timeout(Duration.ofSeconds(20))
                    .POST(HttpRequest.BodyPublishers.ofByteArray(fileBytes))
                    .build();

            HttpResponse<String> response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() >= 200 && response.statusCode() < 300) {
                String publicUrl = baseUrl + "/storage/v1/object/public/" + bucket + "/" + objectPath;
                LOGGER.log(Level.INFO, "Đã upload ảnh thành công lên Supabase Storage: {0}", publicUrl);
                return publicUrl;
            } else {
                LOGGER.log(Level.WARNING, "Supabase Storage trả về HTTP {0}: {1}", new Object[]{response.statusCode(), response.body()});
            }
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Lỗi khi upload ảnh lên Supabase: {0}", e.getMessage());
        }
        return null;
    }

    private static void syncToSourceDirectory(File runtimeFile, String uploadDirPath, String fileName) {
        try {
            String buildWebPattern = "build" + File.separator + "web";
            if (uploadDirPath.contains(buildWebPattern)) {
                String sourceDirPath = uploadDirPath.replace(buildWebPattern, "web");
                File sourceDir = new File(sourceDirPath);
                if (sourceDir.exists() || (sourceDir.getParentFile() != null && sourceDir.getParentFile().exists())) {
                    sourceDir.mkdirs();
                    Path targetPath = Paths.get(sourceDirPath, fileName);
                    Files.copy(runtimeFile.toPath(), targetPath, StandardCopyOption.REPLACE_EXISTING);
                    return;
                }
            }

            File dir = new File(uploadDirPath);
            for (int i = 0; i < 5 && dir != null; i++) {
                File candidate = new File(dir, "web" + File.separator + "assets" + File.separator + "uploads" + File.separator + "items");
                if (candidate.exists() && !candidate.getAbsolutePath().equals(runtimeFile.getParentFile().getAbsolutePath())) {
                    Path targetPath = Paths.get(candidate.getAbsolutePath(), fileName);
                    Files.copy(runtimeFile.toPath(), targetPath, StandardCopyOption.REPLACE_EXISTING);
                    return;
                }
                dir = dir.getParentFile();
            }
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Không thể đồng bộ file sang source directory: {0}", e.getMessage());
        }
    }

    public static boolean deleteUploadedFile(String fileNameOrUrl, String uploadDirPath) {
        if (fileNameOrUrl == null || fileNameOrUrl.trim().isEmpty()) {
            return false;
        }

        String target = fileNameOrUrl.trim();

        if (target.startsWith("http://") || target.startsWith("https://")) {
            if (isSupabaseConfigured() && target.contains(Constants.SUPABASE_BUCKET)) {
                return deleteFromSupabase(target);
            }
            return true;
        }

        if (uploadDirPath == null || uploadDirPath.trim().isEmpty()) {
            return false;
        }

        String cleanFileName = Paths.get(target).getFileName().toString();
        if (cleanFileName.isEmpty() || cleanFileName.equals(".") || cleanFileName.equals("..")) {
            return false;
        }

        boolean deleted = false;
        try {
            File runtimeFile = new File(uploadDirPath, cleanFileName);
            if (runtimeFile.exists()) {
                deleted = runtimeFile.delete();
            }

            String buildWebPattern = "build" + File.separator + "web";
            if (uploadDirPath.contains(buildWebPattern)) {
                String sourceDirPath = uploadDirPath.replace(buildWebPattern, "web");
                File sourceFile = new File(sourceDirPath, cleanFileName);
                if (sourceFile.exists()) {
                    sourceFile.delete();
                }
            }

            File dir = new File(uploadDirPath);
            for (int i = 0; i < 5 && dir != null; i++) {
                File candidate = new File(dir, "web" + File.separator + "assets" + File.separator + "uploads" + File.separator + "items" + File.separator + cleanFileName);
                if (candidate.exists() && !candidate.getAbsolutePath().equals(runtimeFile.getAbsolutePath())) {
                    candidate.delete();
                }
                dir = dir.getParentFile();
            }
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Không thể xóa file ảnh: {0}", e.getMessage());
        }
        return deleted;
    }

    private static boolean deleteFromSupabase(String publicUrl) {
        try {
            String baseUrl = Constants.SUPABASE_URL.replaceAll("/+$", "");
            String bucket = Constants.SUPABASE_BUCKET.trim();
            String prefix = baseUrl + "/storage/v1/object/public/" + bucket + "/";

            if (!publicUrl.startsWith(prefix)) {
                return false;
            }

            String objectPath = publicUrl.substring(prefix.length());
            String deleteEndpoint = baseUrl + "/storage/v1/object/" + bucket + "/" + objectPath;

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(deleteEndpoint))
                    .header("apikey", Constants.SUPABASE_KEY.trim())
                    .header("Authorization", "Bearer " + Constants.SUPABASE_KEY.trim())
                    .timeout(Duration.ofSeconds(15))
                    .DELETE()
                    .build();

            HttpResponse<String> response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
            return response.statusCode() >= 200 && response.statusCode() < 300;
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Không thể xóa ảnh trên Supabase: {0}", e.getMessage());
            return false;
        }
    }
}
