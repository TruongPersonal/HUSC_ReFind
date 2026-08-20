package data.utils;

import jakarta.servlet.http.Part;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Paths;
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
        return uploadToSupabase(filePart, uniqueName);
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
                LOGGER.log(Level.SEVERE, "Supabase Storage trả về HTTP {0}: {1}", new Object[]{response.statusCode(), response.body()});
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Lỗi khi upload ảnh lên Supabase: {0}", e.getMessage());
        }
        return null;
    }

    public static boolean deleteUploadedFile(String fileNameOrUrl, String uploadDirPath) {
        if (fileNameOrUrl == null || fileNameOrUrl.trim().isEmpty()) {
            return false;
        }

        String target = fileNameOrUrl.trim();
        if (target.startsWith("http://") || target.startsWith("https://")) {
            return deleteFromSupabase(target);
        }
        return true;
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
            LOGGER.log(Level.SEVERE, "Không thể xóa ảnh trên Supabase: {0}", e.getMessage());
            return false;
        }
    }
}
