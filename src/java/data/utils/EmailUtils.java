package data.utils;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.time.Duration;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EmailUtils {
    private static final Logger LOGGER = Logger.getLogger(EmailUtils.class.getName());
    private static final SecureRandom RANDOM = new SecureRandom();
    private static final HttpClient HTTP_CLIENT = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    public static String generateOtp() {
        int otp = 100000 + RANDOM.nextInt(900000);
        return String.valueOf(otp);
    }

    public static boolean sendOtpEmail(String recipientEmail, String recipientName, String otpCode) {
        String brevoApiKey = Constants.BREVO_API_KEY != null ? Constants.BREVO_API_KEY.trim() : "";
        if (brevoApiKey.isEmpty()) {
            LOGGER.log(Level.SEVERE, "Chưa cấu hình BREVO_API_KEY.");
            return false;
        }

        String safeName = (recipientName != null && !recipientName.trim().isEmpty()) ? recipientName.trim() : "bạn";
        String htmlBody = buildOtpEmailHtml(safeName, otpCode);
        String subject = "[HUSC ReFind] Mã xác thực OTP đăng ký tài khoản: " + otpCode;

        try {
            String senderName = Constants.SENDER_NAME != null ? Constants.SENDER_NAME.trim() : "HUSC ReFind";
            String senderEmail = Constants.SENDER_EMAIL != null ? Constants.SENDER_EMAIL.trim() : "noreply.huscrefind@gmail.com";

            StringBuilder json = new StringBuilder();
            json.append("{")
                .append("\"sender\":{\"name\":\"").append(escapeJson(senderName)).append("\",\"email\":\"").append(escapeJson(senderEmail)).append("\"},")
                .append("\"to\":[{\"email\":\"").append(escapeJson(recipientEmail)).append("\",\"name\":\"").append(escapeJson(recipientName)).append("\"}],")
                .append("\"subject\":\"").append(escapeJson(subject)).append("\",")
                .append("\"htmlContent\":\"").append(escapeJson(htmlBody)).append("\"")
                .append("}");

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.brevo.com/v3/smtp/email"))
                    .header("api-key", brevoApiKey)
                    .header("Content-Type", "application/json")
                    .header("Accept", "application/json")
                    .timeout(Duration.ofSeconds(15))
                    .POST(HttpRequest.BodyPublishers.ofString(json.toString(), StandardCharsets.UTF_8))
                    .build();

            HttpResponse<String> response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

            if (response.statusCode() >= 200 && response.statusCode() < 300) {
                LOGGER.log(Level.INFO, "Đã gửi email OTP qua Brevo API thành công tới: {0}", recipientEmail);
                return true;
            } else {
                LOGGER.log(Level.WARNING, "Brevo API trả về HTTP {0}: {1}", new Object[]{response.statusCode(), response.body()});
                return false;
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Lỗi khi gửi email OTP qua Brevo: {0}", e.getMessage());
            return false;
        }
    }

    private static String escapeJson(String s) {
        if (s == null) return "";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            switch (c) {
                case '"': sb.append("\\\""); break;
                case '\\': sb.append("\\\\"); break;
                case '\b': sb.append("\\b"); break;
                case '\f': sb.append("\\f"); break;
                case '\n': sb.append("\\n"); break;
                case '\r': sb.append("\\r"); break;
                case '\t': sb.append("\\t"); break;
                default:
                    if (c < ' ') {
                        String t = "000" + Integer.toHexString(c);
                        sb.append("\\u").append(t.substring(t.length() - 4));
                    } else {
                        sb.append(c);
                    }
            }
        }
        return sb.toString();
    }

    private static String buildOtpEmailHtml(String recipientName, String otpCode) {
        String safeName = (recipientName != null && !recipientName.trim().isEmpty()) ? recipientName.trim() : "bạn";
        return "<!DOCTYPE html>"
             + "<html>"
             + "<head><meta charset='UTF-8'></head>"
             + "<body style='margin:0; padding:0; background-color:#f1f5f9; font-family:-apple-system, BlinkMacSystemFont, Segoe UI, Roboto, Helvetica, Arial, sans-serif;'>"
             + "  <div style='max-width:540px; margin:30px auto; background:#ffffff; border-radius:16px; overflow:hidden; box-shadow:0 4px 16px rgba(0,0,0,0.06); border:1px solid #e2e8f0;'>"
             + "    <div style='background:linear-gradient(135deg, #1e3a8a 0%, #2563eb 100%); padding:28px 24px; text-align:center; color:#ffffff;'>"
             + "      <h1 style='margin:0; font-size:24px; font-weight:700; letter-spacing:0.5px;'>🎓 HUSC ReFind</h1>"
             + "      <p style='margin:6px 0 0 0; font-size:13px; color:#bfdbfe;'>Hệ thống Tìm kiếm & Quản lý Đồ Thất Lạc</p>"
             + "    </div>"
             + "    <div style='padding:32px 28px; color:#334155; line-height:1.6; font-size:15px;'>"
             + "      <p style='margin:0 0 16px 0;'>Xin chào <strong>" + safeName + "</strong>,</p>"
             + "      <p style='margin:0 0 20px 0;'>Bạn đang thực hiện đăng ký tài khoản trên nền tảng <strong>HUSC ReFind</strong> (Trường Đại học Khoa học - Đại học Huế). Dưới đây là mã xác thực OTP của bạn:</p>"
             + "      <div style='text-align:center; margin:24px 0;'>"
             + "        <div style='display:inline-block; background-color:#eff6ff; border:2px dashed #3b82f6; border-radius:12px; padding:14px 32px; font-size:32px; font-weight:800; letter-spacing:8px; color:#1d4ed8; font-family:monospace;'>"
             +            otpCode
             + "        </div>"
             + "      </div>"
             + "      <p style='margin:0 0 12px 0; font-size:13px; color:#64748b; text-align:center;'>⏳ Mã xác thực có hiệu lực trong vòng <strong>5 phút</strong>.</p>"
             + "      <hr style='border:none; border-top:1px solid #e2e8f0; margin:24px 0;' />"
             + "      <p style='margin:0; font-size:12px; color:#94a3b8; line-height:1.5;'>"
             + "        🛡️ <strong>Lưu ý bảo mật:</strong> Tuyệt đối không chia sẻ mã này cho bất kỳ ai. Nếu bạn không yêu cầu đăng ký, vui lòng bỏ qua email này."
             + "      </p>"
             + "    </div>"
             + "    <div style='background-color:#f8fafc; padding:16px 24px; text-align:center; font-size:12px; color:#94a3b8; border-top:1px solid #e2e8f0;'>"
             + "      © 2026 Trường Đại học Khoa học, Đại học Huế - HUSC ReFind"
             + "    </div>"
             + "  </div>"
             + "</body>"
             + "</html>";
    }
}
