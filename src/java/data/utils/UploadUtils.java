package data.utils;

import jakarta.servlet.http.Part;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class UploadUtils {

    private static final Logger LOGGER = Logger.getLogger(UploadUtils.class.getName());
    private static final Set<String> ALLOWED_EXTENSIONS = Set.of(".jpg", ".jpeg", ".png", ".webp");

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
        File uploadDir = new File(uploadDirPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        File runtimeFile = new File(uploadDir, uniqueName);
        filePart.write(runtimeFile.getAbsolutePath());

        syncToSourceDirectory(runtimeFile, uploadDirPath, uniqueName);

        return uniqueName;
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
}
