package data.utils;

public class Constants {
    public static final String DB_URL = EnvConfig.get("DB_URL", "jdbc:mysql://localhost:3311/husc_refind?useUnicode=true&characterEncoding=UTF-8");
    public static final String USER = EnvConfig.get("DB_USER", "root");
    public static final String PASS = EnvConfig.get("DB_PASS", "Pass123!");

    public static final String UPLOAD_DIR = "assets/uploads/items";

    public static final String BREVO_API_KEY = EnvConfig.get("BREVO_API_KEY", "");
    public static final String SENDER_NAME = EnvConfig.get("SENDER_NAME", "HUSC ReFind");
    public static final String SENDER_EMAIL = EnvConfig.get("SENDER_EMAIL", "noreply.huscrefind@gmail.com");

    public static final String GEMINI_API_KEY = EnvConfig.get("GEMINI_API_KEY", "");
    public static final String GEMINI_MODEL = EnvConfig.get("GEMINI_MODEL", "gemini-3.7-flash");

    public static final String SUPABASE_URL = EnvConfig.get("SUPABASE_URL", "");
    public static final String SUPABASE_KEY = EnvConfig.get("SUPABASE_KEY", "");
    public static final String SUPABASE_BUCKET = EnvConfig.get("SUPABASE_BUCKET", "husc_refind");
    public static final String SUPABASE_FOLDER = EnvConfig.get("SUPABASE_FOLDER", "items");
}
