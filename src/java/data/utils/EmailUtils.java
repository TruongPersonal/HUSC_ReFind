package data.utils;

import java.security.SecureRandom;
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
        try {
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
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Không thể gửi email OTP: {0}", e.getMessage());
            return true;
        }
    }
}
