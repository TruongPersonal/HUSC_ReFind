package data.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class EnvConfig {

    private static final Logger LOGGER = Logger.getLogger(EnvConfig.class.getName());
    private static final Map<String, String> ENV_CACHE = new HashMap<>();
    private static boolean initialized = false;

    static {
        loadDotEnv();
    }

    private EnvConfig() {}

    private static synchronized void loadDotEnv() {
        if (initialized) return;
        initialized = true;

        File envFile = findDotEnvFile();
        if (envFile == null || !envFile.exists() || !envFile.canRead()) {
            LOGGER.log(Level.INFO, "Không tìm thấy file .env, hệ thống sẽ sử dụng Environment Variables hoặc giá trị mặc định.");
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(envFile, StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                }
                int eqIdx = line.indexOf('=');
                if (eqIdx > 0) {
                    String key = line.substring(0, eqIdx).trim();
                    String val = line.substring(eqIdx + 1).trim();

                    if ((val.startsWith("\"") && val.endsWith("\"")) || (val.startsWith("'") && val.endsWith("'"))) {
                        if (val.length() >= 2) {
                            val = val.substring(1, val.length() - 1);
                        }
                    }
                    ENV_CACHE.put(key, val);
                }
            }
            LOGGER.log(Level.INFO, "Đã nạp thành công cấu hình từ file .env: {0}", envFile.getAbsolutePath());
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Lỗi khi đọc file .env: {0}", e.getMessage());
        }
    }

    private static File findDotEnvFile() {
        String userDir = System.getProperty("user.dir");
        if (userDir != null) {
            File directFile = new File(userDir, ".env");
            if (directFile.exists()) return directFile;

            File current = new File(userDir);
            for (int i = 0; i < 4 && current != null; i++) {
                File candidate = new File(current, ".env");
                if (candidate.exists()) return candidate;
                current = current.getParentFile();
            }
        }

        String catalinaBase = System.getProperty("catalina.base");
        if (catalinaBase != null) {
            File catFile = new File(catalinaBase, ".env");
            if (catFile.exists()) return catFile;
        }

        try {
            var url = EnvConfig.class.getProtectionDomain().getCodeSource().getLocation();
            if (url != null) {
                File classPathDir = new File(url.toURI());
                File dir = classPathDir;
                for (int i = 0; i < 6 && dir != null; i++) {
                    File candidate = new File(dir, ".env");
                    if (candidate.exists()) return candidate;
                    dir = dir.getParentFile();
                }
            }
        } catch (Exception ignored) {}

        return null;
    }

    public static String get(String key, String defaultValue) {
        String val = System.getenv(key);
        if (val != null && !val.trim().isEmpty()) {
            return val.trim();
        }

        val = System.getProperty(key);
        if (val != null && !val.trim().isEmpty()) {
            return val.trim();
        }

        val = ENV_CACHE.get(key);
        if (val != null && !val.trim().isEmpty()) {
            return val.trim();
        }

        return defaultValue;
    }

    public static String get(String key) {
        return get(key, null);
    }
}
