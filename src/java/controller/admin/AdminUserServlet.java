package controller.admin;

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
import java.util.List;
import java.util.regex.Pattern;

@WebServlet(name = "AdminUserServlet", urlPatterns = {"/admin/users"})
public class AdminUserServlet extends HttpServlet {
    private static final Pattern PHONE_PATTERN = Pattern.compile("^0[0-9]{9}$");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");
    private static final Pattern STUDENT_CODE_PATTERN = Pattern.compile("^[0-9]{2}[tT][0-9]{7}$");
    private static final Pattern ADMIN_CODE_PATTERN = Pattern.compile("^[a-zA-Z0-9_-]{3,30}$");

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        UserDao userDao = Database.getUserDao();

        String role = request.getParameter("role");
        if (role == null || role.trim().isEmpty()) {
            role = "ALL";
        }

        String keyword = request.getParameter("keyword");

        List<User> users = userDao.searchUsers(keyword, role);

        int totalUsers = userDao.countUsersByRole("ALL");
        int studentCount = userDao.countUsersByRole("student");
        int adminCount = userDao.countUsersByRole("admin");

        request.setAttribute("users", users);
        request.setAttribute("selectedRole", role);
        request.setAttribute("keyword", keyword != null ? keyword.trim() : "");
        request.setAttribute("totalUsers", totalUsers);
        request.setAttribute("studentCount", studentCount);
        request.setAttribute("adminCount", adminCount);
        request.setAttribute("activeAdminNav", "users");

        request.getRequestDispatcher("/views/admin/user_manager.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        UserDao userDao = Database.getUserDao();
        HttpSession session = request.getSession(true);
        User currentUser = (User) session.getAttribute("user");

        if ("add".equalsIgnoreCase(action)) {
            String name = request.getParameter("name");
            String code = request.getParameter("code");
            String email = request.getParameter("email");
            String phone = request.getParameter("phone");
            String password = request.getParameter("password");
            String role = request.getParameter("role");
            String assignedRole = (role != null && !role.trim().isEmpty()) ? role.trim() : "student";

            if (name == null || name.trim().isEmpty() || code == null || code.trim().isEmpty() || password == null || password.trim().isEmpty()) {
                session.setAttribute("errorMessage", "Vui lòng nhập đầy đủ các trường bắt buộc (Họ tên, Mã người dùng, Mật khẩu).");
            } else if ("student".equalsIgnoreCase(assignedRole) && !STUDENT_CODE_PATTERN.matcher(code.trim()).matches()) {
                session.setAttribute("errorMessage", "Mã số sinh viên không hợp lệ (ví dụ: 23T1020500).");
            } else if (!"student".equalsIgnoreCase(assignedRole) && !ADMIN_CODE_PATTERN.matcher(code.trim()).matches()) {
                session.setAttribute("errorMessage", "Mã quản trị viên không hợp lệ.");
            } else if (password.trim().length() < 6) {
                session.setAttribute("errorMessage", "Mật khẩu phải có ít nhất 6 ký tự.");
            } else if (email != null && !email.trim().isEmpty() && !EMAIL_PATTERN.matcher(email.trim()).matches()) {
                session.setAttribute("errorMessage", "Địa chỉ email không hợp lệ.");
            } else if (phone != null && !phone.trim().isEmpty() && !PHONE_PATTERN.matcher(phone.trim()).matches()) {
                session.setAttribute("errorMessage", "Số điện thoại không hợp lệ.");
            } else if (userDao.checkCodeExists(code.trim())) {
                session.setAttribute("errorMessage", "Mã người dùng này đã tồn tại trong hệ thống.");
            } else if (email != null && !email.trim().isEmpty() && userDao.checkEmailExists(email.trim())) {
                session.setAttribute("errorMessage", "Email này đã được sử dụng bởi người dùng khác.");
            } else if (phone != null && !phone.trim().isEmpty() && userDao.checkPhoneExists(phone.trim())) {
                session.setAttribute("errorMessage", "Số điện thoại này đã được sử dụng bởi người dùng khác.");
            } else {
                String hashedPassword = SecurityUtils.hashPassword(password.trim());
                boolean inserted = userDao.insertUser(code.trim(), name.trim(), email, phone, hashedPassword, assignedRole);
                if (inserted) {
                    session.setAttribute("successMessage", "Thêm người dùng mới thành công!");
                } else {
                    session.setAttribute("errorMessage", "Không thể thêm người dùng. Vui lòng thử lại.");
                }
            }
        } else if ("update".equalsIgnoreCase(action)) {
            String idStr = request.getParameter("id");
            String name = request.getParameter("name");
            String code = request.getParameter("code");
            String email = request.getParameter("email");
            String phone = request.getParameter("phone");
            String newPassword = request.getParameter("password");
            String role = request.getParameter("role");

            if (idStr != null && !idStr.trim().isEmpty() && name != null && !name.trim().isEmpty() && code != null && !code.trim().isEmpty()) {
                int userId = Integer.parseInt(idStr.trim());
                User existingUser = userDao.findUserById(userId);
                if (existingUser != null) {
                    String assignedRole = (role != null && !role.trim().isEmpty()) ? role.trim() : existingUser.getRole();

                    if ("student".equalsIgnoreCase(assignedRole) && !STUDENT_CODE_PATTERN.matcher(code.trim()).matches()) {
                        session.setAttribute("errorMessage", "Mã số sinh viên không hợp lệ (ví dụ: 23T1020500).");
                    } else if (!"student".equalsIgnoreCase(assignedRole) && !ADMIN_CODE_PATTERN.matcher(code.trim()).matches()) {
                        session.setAttribute("errorMessage", "Mã quản trị viên không hợp lệ.");
                    } else if (newPassword != null && !newPassword.trim().isEmpty() && newPassword.trim().length() < 6) {
                        session.setAttribute("errorMessage", "Mật khẩu phải có ít nhất 6 ký tự.");
                    } else if (email != null && !email.trim().isEmpty() && !EMAIL_PATTERN.matcher(email.trim()).matches()) {
                        session.setAttribute("errorMessage", "Địa chỉ email không hợp lệ.");
                    } else if (phone != null && !phone.trim().isEmpty() && !PHONE_PATTERN.matcher(phone.trim()).matches()) {
                        session.setAttribute("errorMessage", "Số điện thoại không hợp lệ.");
                    } else if (!code.trim().equalsIgnoreCase(existingUser.getRawCode()) && userDao.checkCodeExists(code.trim())) {
                        session.setAttribute("errorMessage", "Mã người dùng này đã tồn tại trong hệ thống.");
                    } else if (email != null && !email.trim().isEmpty()
                               && (existingUser.getRawEmail() == null || !email.trim().equalsIgnoreCase(existingUser.getRawEmail()))
                               && userDao.checkEmailExists(email.trim())) {
                        session.setAttribute("errorMessage", "Email này đã được sử dụng bởi người dùng khác.");
                    } else if (phone != null && !phone.trim().isEmpty()
                               && (existingUser.getRawPhone() == null || !phone.trim().equals(existingUser.getRawPhone()))
                               && userDao.checkPhoneExists(phone.trim())) {
                        session.setAttribute("errorMessage", "Số điện thoại này đã được sử dụng bởi người dùng khác.");
                    } else {
                        String hashedPassword = null;
                        if (newPassword != null && !newPassword.trim().isEmpty()) {
                            hashedPassword = SecurityUtils.hashPassword(newPassword.trim());
                        }
                        boolean updated = userDao.updateUserByAdmin(userId, code.trim(), name.trim(), email, phone, assignedRole, hashedPassword);
                        if (updated) {

                            if (currentUser != null && currentUser.getId() == userId) {
                                User freshUser = userDao.findUserById(userId);
                                session.setAttribute("user", freshUser);
                            }
                            session.setAttribute("successMessage", "Cập nhật thông tin người dùng thành công!");
                        } else {
                            session.setAttribute("errorMessage", "Cập nhật người dùng thất bại.");
                        }
                    }
                }
            }
        } else if ("toggle_status".equalsIgnoreCase(action)) {
            String idStr = request.getParameter("id");
            String statusStr = request.getParameter("status");
            if (idStr != null && statusStr != null) {
                int userId = Integer.parseInt(idStr.trim());
                int newStatus = Integer.parseInt(statusStr.trim());
                User targetUser = userDao.findUserById(userId);
                if (targetUser != null) {
                    if (currentUser != null && currentUser.getId() == userId) {
                        session.setAttribute("errorMessage", "Không thể tự khóa tài khoản Quản trị viên đang đăng nhập.");
                    } else {
                        boolean ok = userDao.updateUserStatus(userId, newStatus);
                        if (ok) {
                            if (newStatus == 0) {
                                session.setAttribute("successMessage", "Đã khóa tài khoản [" + targetUser.getName() + "] thành công.");
                            } else {
                                session.setAttribute("successMessage", "Đã mở khóa tài khoản [" + targetUser.getName() + "] thành công.");
                            }
                        } else {
                            session.setAttribute("errorMessage", "Thao tác cập nhật trạng thái thất bại.");
                        }
                    }
                }
            }
        } else if ("delete".equalsIgnoreCase(action)) {
            String idStr = request.getParameter("id");
            if (idStr != null && !idStr.trim().isEmpty()) {
                int userId = Integer.parseInt(idStr.trim());
                if (currentUser != null && currentUser.getId() == userId) {
                    session.setAttribute("errorMessage", "Bạn không thể xóa tài khoản Quản trị viên đang đăng nhập.");
                } else {
                    try {
                        boolean deleted = userDao.deleteUser(userId);
                        if (deleted) {
                            session.setAttribute("successMessage", "Xóa tài khoản thành công.");
                        } else {
                            session.setAttribute("errorMessage", "Không thể xóa người dùng.");
                        }
                    } catch (Exception e) {
                        session.setAttribute("errorMessage", "Không thể xóa người dùng đang có dữ liệu liên kết trong hệ thống.");
                    }
                }
            }
        }

        response.sendRedirect(request.getContextPath() + "/admin/users");
    }
}
