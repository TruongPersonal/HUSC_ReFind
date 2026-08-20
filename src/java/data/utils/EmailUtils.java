package data.utils;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.security.SecureRandom;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EmailUtils {
    private static final Logger LOGGER = Logger.getLogger(EmailUtils.class.getName());
    private static final SecureRandom RANDOM = new SecureRandom();

    public static String generateOtp() {
        int otp = 100000 + RANDOM.nextInt(900000);
        return String.valueOf(otp);
    }

    public static boolean sendOtpEmail(String recipientEmail, String recipientName, String otpCode) {
        String senderEmail = Constants.SENDER_EMAIL != null ? Constants.SENDER_EMAIL.trim() : "";
        String senderPassword = Constants.SENDER_PASSWORD != null ? Constants.SENDER_PASSWORD.trim() : "";

        // Nếu chưa cấu hình email hoặc mật khẩu ứng dụng -> Ghi log giả lập (Local dev fallback)
        if (senderEmail.isEmpty() || senderPassword.isEmpty()) {
            LOGGER.log(Level.INFO,
                "\n======================================================\n" +
                "[HUSC ReFind - GIẢ LẬP GỬI EMAIL OTP (LOCAL DEV)]\n" +
                "Gửi tới : {0} ({1})\n" +
                "Mã OTP  : {2}\n" +
                "Hiệu lực: 5 phút\n" +
                "======================================================",
                new Object[]{recipientEmail, recipientName, otpCode}
            );
            return true;
        }

        try {
            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", Constants.SMTP_HOST);
            props.put("mail.smtp.port", Constants.SMTP_PORT);
            props.put("mail.smtp.ssl.protocols", "TLSv1.2");
            props.put("mail.smtp.connectiontimeout", "10000");
            props.put("mail.smtp.timeout", "10000");

            Session session = Session.getInstance(props, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(senderEmail, senderPassword);
                }
            });

            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(senderEmail, "HUSC ReFind", "UTF-8"));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipientEmail, recipientName, "UTF-8"));
            message.setSubject("[HUSC ReFind] Mã xác thực OTP đăng ký tài khoản: " + otpCode, "UTF-8");

            String htmlBody = buildOtpEmailHtml(recipientName, otpCode);
            message.setContent(htmlBody, "text/html; charset=UTF-8");

            Transport.send(message);
            LOGGER.log(Level.INFO, "Đã gửi email OTP thực tế thành công tới: {0}", recipientEmail);
            return true;
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Không thể gửi email OTP qua SMTP, chuyển về ghi log Console: {0}", e.getMessage());
            LOGGER.log(Level.INFO,
                "\n======================================================\n" +
                "[HUSC ReFind - FALLBACK EMAIL OTP]\n" +
                "Gửi tới : {0} ({1})\n" +
                "Mã OTP  : {2}\n" +
                "======================================================",
                new Object[]{recipientEmail, recipientName, otpCode}
            );
            return true;
        }
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
