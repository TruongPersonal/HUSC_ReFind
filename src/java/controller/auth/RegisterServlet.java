package controller.auth;

import data.dao.Database;
import data.dao.UserDao;
import data.utils.EmailUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

@WebServlet(name = "RegisterServlet", urlPatterns = {"/register"})
public class RegisterServlet extends HttpServlet {
    private static final Pattern STUDENT_CODE_PATTERN = Pattern.compile("^[0-9]{2}[tT][0-9]{7}$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^0[0-9]{9}$");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            response.sendRedirect(request.getContextPath() + "/home");
            return;
        }
        request.getRequestDispatcher("/views/register.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String code = request.getParameter("code");
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirm_password");

        if (code == null || code.trim().isEmpty() ||
            name == null || name.trim().isEmpty() ||
            password == null || password.isEmpty()) {
            request.setAttribute("errorMessage", "Vui lòng điền đầy đủ các thông tin bắt buộc.");
            preserveFormData(request, code, name, email, phone);
            request.getRequestDispatcher("/views/register.jsp").forward(request, response);
            return;
        }

        code = code.trim();
        name = name.trim();
        email = (email != null) ? email.trim() : "";
        phone = (phone != null) ? phone.trim() : "";

        if (!STUDENT_CODE_PATTERN.matcher(code).matches()) {
            request.setAttribute("errorMessage", "Mã số sinh viên không hợp lệ (ví dụ: 23t1020573).");
            preserveFormData(request, code, name, email, phone);
            request.getRequestDispatcher("/views/register.jsp").forward(request, response);
            return;
        }

        if (!phone.isEmpty() && !PHONE_PATTERN.matcher(phone).matches()) {
            request.setAttribute("errorMessage", "Số điện thoại không hợp lệ (phải gồm 10 chữ số và bắt đầu bằng số 0).");
            preserveFormData(request, code, name, email, phone);
            request.getRequestDispatcher("/views/register.jsp").forward(request, response);
            return;
        }

        if (email.isEmpty()) {
            email = code.toLowerCase() + "@husc.edu.vn";
        } else if (!EMAIL_PATTERN.matcher(email).matches()) {
            request.setAttribute("errorMessage", "Địa chỉ email không hợp lệ.");
            preserveFormData(request, code, name, email, phone);
            request.getRequestDispatcher("/views/register.jsp").forward(request, response);
            return;
        }
        if (!password.equals(confirmPassword)) {
            request.setAttribute("errorMessage", "Mật khẩu xác nhận không khớp. Vui lòng nhập lại.");
            preserveFormData(request, code, name, email, phone);
            request.getRequestDispatcher("/views/register.jsp").forward(request, response);
            return;
        }

        if (password.length() < 6) {
            request.setAttribute("errorMessage", "Mật khẩu phải có ít nhất 6 ký tự.");
            preserveFormData(request, code, name, email, phone);
            request.getRequestDispatcher("/views/register.jsp").forward(request, response);
            return;
        }

        UserDao userDao = Database.getUserDao();

        if (userDao.checkCodeExists(code)) {
            request.setAttribute("errorMessage", "Mã số sinh viên này đã được đăng ký tài khoản trong hệ thống.");
            preserveFormData(request, code, name, email, phone);
            request.getRequestDispatcher("/views/register.jsp").forward(request, response);
            return;
        }
        if (!phone.isEmpty() && userDao.checkPhoneExists(phone)) {
            request.setAttribute("errorMessage", "Số điện thoại này đã được sử dụng bởi một tài khoản khác.");
            preserveFormData(request, code, name, email, phone);
            request.getRequestDispatcher("/views/register.jsp").forward(request, response);
            return;
        }
        if (userDao.checkEmailExists(email)) {
            request.setAttribute("errorMessage", "Địa chỉ email này đã được sử dụng bởi một tài khoản khác.");
            preserveFormData(request, code, name, email, phone);
            request.getRequestDispatcher("/views/register.jsp").forward(request, response);
            return;
        }

        String otpCode = EmailUtils.generateOtp();
        long now = System.currentTimeMillis();
        long otpExpiryTime = now + (5 * 60 * 1000);
        long resendAvailableTime = now + (60 * 1000);

        Map<String, Object> pendingUser = new HashMap<>();
        pendingUser.put("code", code);
        pendingUser.put("name", name);
        pendingUser.put("email", email);
        pendingUser.put("phone", phone);
        pendingUser.put("password", password);
        pendingUser.put("otp", otpCode);
        pendingUser.put("expiry", otpExpiryTime);
        pendingUser.put("resendAvailableAt", resendAvailableTime);

        HttpSession session = request.getSession(true);
        session.setAttribute("pendingRegistration", pendingUser);

        EmailUtils.sendOtpEmail(email, name, otpCode);

        response.sendRedirect(request.getContextPath() + "/verify-otp");
    }

    private void preserveFormData(HttpServletRequest request, String code, String name, String email, String phone) {
        request.setAttribute("param_code", code);
        request.setAttribute("param_name", name);
        request.setAttribute("param_email", email);
        request.setAttribute("param_phone", phone);
    }
}
