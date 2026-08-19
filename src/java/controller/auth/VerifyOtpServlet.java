package controller.auth;

import data.dao.Database;
import data.dao.UserDao;
import data.utils.EmailUtils;
import data.utils.SecurityUtils;
import model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;

@WebServlet(name = "VerifyOtpServlet", urlPatterns = {"/verify-otp"})
public class VerifyOtpServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("pendingRegistration") == null) {
            response.sendRedirect(request.getContextPath() + "/register");
            return;
        }

        @SuppressWarnings("unchecked")
        Map<String, Object> pendingUser = (Map<String, Object>) session.getAttribute("pendingRegistration");
        Long resendAvailableAt = (Long) pendingUser.get("resendAvailableAt");
        long now = System.currentTimeMillis();
        long cooldownSeconds = 0;
        if (resendAvailableAt != null && resendAvailableAt > now) {
            cooldownSeconds = (resendAvailableAt - now + 999) / 1000;
        }
        populateCooldown(request, pendingUser);

        request.getRequestDispatcher("/views/verify_otp.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("pendingRegistration") == null) {
            response.sendRedirect(request.getContextPath() + "/register");
            return;
        }

        @SuppressWarnings("unchecked")
        Map<String, Object> pendingUser = (Map<String, Object>) session.getAttribute("pendingRegistration");
        if ("resend".equalsIgnoreCase(request.getParameter("action"))) {
            Long resendAvailableAt = (Long) pendingUser.get("resendAvailableAt");
            long now = System.currentTimeMillis();
            if (resendAvailableAt != null && now < resendAvailableAt) {
                long remainingSecs = (resendAvailableAt - now + 999) / 1000;
                request.setAttribute("errorMessage", "Vui lòng chờ " + remainingSecs + " giây trước khi yêu cầu gửi lại mã.");
                request.setAttribute("cooldownSeconds", remainingSecs);
                request.getRequestDispatcher("/views/verify_otp.jsp").forward(request, response);
                return;
            }

            String newOtp = EmailUtils.generateOtp();
            pendingUser.put("otp", newOtp);
            pendingUser.put("expiry", now + (5 * 60 * 1000));
            pendingUser.put("resendAvailableAt", now + (60 * 1000));

            EmailUtils.sendOtpEmail((String) pendingUser.get("email"), (String) pendingUser.get("name"), newOtp);
            request.setAttribute("successMessage", "Mã OTP mới đã được gửi.");
            request.setAttribute("cooldownSeconds", 60L);
            request.getRequestDispatcher("/views/verify_otp.jsp").forward(request, response);
            return;
        }

        String inputOtp = request.getParameter("otp");

        if (inputOtp == null || inputOtp.trim().isEmpty()) {
            populateCooldown(request, pendingUser);
            request.setAttribute("errorMessage", "Vui lòng nhập mã OTP 6 chữ số.");
            request.getRequestDispatcher("/views/verify_otp.jsp").forward(request, response);
            return;
        }

        inputOtp = inputOtp.trim();
        String expectedOtp = (String) pendingUser.get("otp");
        long expiryTime = (Long) pendingUser.get("expiry");

        if (System.currentTimeMillis() > expiryTime) {
            populateCooldown(request, pendingUser);
            request.setAttribute("errorMessage", "Mã OTP đã hết hạn. Vui lòng bấm 'Gửi lại'.");
            request.getRequestDispatcher("/views/verify_otp.jsp").forward(request, response);
            return;
        }

        if (!expectedOtp.equals(inputOtp)) {
            populateCooldown(request, pendingUser);
            request.setAttribute("errorMessage", "Mã OTP không chính xác. Vui lòng kiểm tra lại.");
            request.getRequestDispatcher("/views/verify_otp.jsp").forward(request, response);
            return;
        }

        String code = (String) pendingUser.get("code");
        String name = (String) pendingUser.get("name");
        String email = (String) pendingUser.get("email");
        String phone = (String) pendingUser.get("phone");
        String rawPassword = (String) pendingUser.get("password");
        String hashedPassword = SecurityUtils.hashPassword(rawPassword);

        UserDao userDao = Database.getUserDao();
        boolean created = userDao.insertUser(code, name, email, phone, hashedPassword, "student");

        if (created) {
            User newUser = userDao.findUserByCode(code);
            session.removeAttribute("pendingRegistration");
            if (newUser != null) {
                session.setAttribute("user", newUser);
            }
            session.setAttribute("successMessage", "Tài khoản đã được kích hoạt thành công.");
            response.sendRedirect(request.getContextPath() + "/home");
        } else {
            populateCooldown(request, pendingUser);
            request.setAttribute("errorMessage", "Có lỗi xảy ra trong quá trình lưu tài khoản. Vui lòng thử lại.");
            request.getRequestDispatcher("/views/verify_otp.jsp").forward(request, response);
        }
    }

    private void populateCooldown(HttpServletRequest request, Map<String, Object> pendingUser) {
        Long resendAvailableAt = (Long) pendingUser.get("resendAvailableAt");
        long now = System.currentTimeMillis();
        long cooldownSeconds = 0;
        if (resendAvailableAt != null && resendAvailableAt > now) {
            cooldownSeconds = (resendAvailableAt - now + 999) / 1000;
        }
        request.setAttribute("cooldownSeconds", cooldownSeconds);
    }
}
