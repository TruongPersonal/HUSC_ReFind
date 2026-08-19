package controller.student;

import data.dao.Database;
import data.dao.UserDao;
import data.utils.SecurityUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.User;
import java.io.IOException;
import java.util.regex.Pattern;

@WebServlet(name = "ProfileServlet", urlPatterns = {"/profile"})
public class ProfileServlet extends HttpServlet {
    private static final Pattern PHONE_PATTERN = Pattern.compile("^0[0-9]{9}$");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        User currentUser = (User) session.getAttribute("user");
        UserDao userDao = Database.getUserDao();
        User freshUser = userDao.findUserById(currentUser.getId());
        if (freshUser != null) {
            session.setAttribute("user", freshUser);
        }

        request.getRequestDispatcher("/views/profile.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        User currentUser = (User) session.getAttribute("user");
        String action = request.getParameter("action");
        UserDao userDao = Database.getUserDao();

        if ("update_info".equalsIgnoreCase(action)) {
            String name = request.getParameter("name");
            String phone = request.getParameter("phone");
            String email = request.getParameter("email");

            if (name == null || name.trim().isEmpty()) {
                request.setAttribute("errorMessage", "Họ và tên không được để trống.");
                request.getRequestDispatcher("/views/profile.jsp").forward(request, response);
                return;
            }
            name = name.trim();
            phone = (phone != null) ? phone.trim() : "";
            email = (email != null) ? email.trim() : "";

            if (!phone.isEmpty()) {
                if (!PHONE_PATTERN.matcher(phone).matches()) {
                    request.setAttribute("errorMessage", "Số điện thoại không hợp lệ (phải gồm 10 chữ số và bắt đầu bằng số 0).");
                    request.getRequestDispatcher("/views/profile.jsp").forward(request, response);
                    return;
                }

                User existingPhoneUser = userDao.findUserByPhone(phone);
                if (existingPhoneUser != null && existingPhoneUser.getId() != currentUser.getId()) {
                    request.setAttribute("errorMessage", "Số điện thoại này đã được sử dụng bởi một tài khoản khác.");
                    request.getRequestDispatcher("/views/profile.jsp").forward(request, response);
                    return;
                }
            }

            if (!email.isEmpty()) {
                if (!EMAIL_PATTERN.matcher(email).matches()) {
                    request.setAttribute("errorMessage", "Địa chỉ email không hợp lệ.");
                    request.getRequestDispatcher("/views/profile.jsp").forward(request, response);
                    return;
                }

                User existingUser = userDao.findUserByEmail(email);
                if (existingUser != null && existingUser.getId() != currentUser.getId()) {
                    request.setAttribute("errorMessage", "Địa chỉ email này đã được sử dụng bởi một tài khoản khác.");
                    request.getRequestDispatcher("/views/profile.jsp").forward(request, response);
                    return;
                }
            }

            boolean updated = userDao.updateProfile(currentUser.getId(), name, phone, email);
            if (updated) {
                User freshUser = userDao.findUserById(currentUser.getId());
                session.setAttribute("user", freshUser);
                request.setAttribute("successMessage", "Cập nhật thông tin thành công!");
            } else {
                request.setAttribute("errorMessage", "Cập nhật thông tin thất bại.");
            }
        } else if ("change_password".equalsIgnoreCase(action)) {
            String currentPassword = request.getParameter("current_password");
            String newPassword = request.getParameter("new_password");
            String confirmPassword = request.getParameter("confirm_password");

            if (currentPassword == null || newPassword == null || confirmPassword == null ||
                currentPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
                request.setAttribute("errorMessage", "Vui lòng nhập đầy đủ thông tin đổi mật khẩu.");
                request.getRequestDispatcher("/views/profile.jsp").forward(request, response);
                return;
            }

            if (!SecurityUtils.passwordMatches(currentPassword, currentUser.getPassword())) {
                request.setAttribute("errorMessage", "Mật khẩu hiện tại không chính xác.");
                request.getRequestDispatcher("/views/profile.jsp").forward(request, response);
                return;
            }

            if (!newPassword.equals(confirmPassword)) {
                request.setAttribute("errorMessage", "Mật khẩu mới và mật khẩu xác nhận không khớp.");
                request.getRequestDispatcher("/views/profile.jsp").forward(request, response);
                return;
            }

            if (newPassword.length() < 6) {
                request.setAttribute("errorMessage", "Mật khẩu mới phải có ít nhất 6 ký tự.");
                request.getRequestDispatcher("/views/profile.jsp").forward(request, response);
                return;
            }

            String hashedNew = SecurityUtils.hashPassword(newPassword);
            boolean updated = userDao.updatePassword(currentUser.getId(), hashedNew);
            if (updated) {
                User freshUser = userDao.findUserById(currentUser.getId());
                session.setAttribute("user", freshUser);
                request.setAttribute("successMessage", "Đổi mật khẩu thành công!");
            } else {
                request.setAttribute("errorMessage", "Đổi mật khẩu thất bại.");
            }
        }

        request.getRequestDispatcher("/views/profile.jsp").forward(request, response);
    }
}
