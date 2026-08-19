package controller.auth;

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

@WebServlet(name = "LoginServlet", urlPatterns = {"/login"})
public class LoginServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            User user = (User) session.getAttribute("user");
            if (user.isAdmin()) {
                response.sendRedirect(request.getContextPath() + "/admin/dashboard");
            } else {
                response.sendRedirect(request.getContextPath() + "/home");
            }
            return;
        }

        if (session != null) {
            String successMsg = (String) session.getAttribute("successMessage");
            if (successMsg != null) {
                request.setAttribute("successMessage", successMsg);
                session.removeAttribute("successMessage");
            }
            String errorMsg = (String) session.getAttribute("errorMessage");
            if (errorMsg != null) {
                request.setAttribute("errorMessage", errorMsg);
                session.removeAttribute("errorMessage");
            }
        }

        request.getRequestDispatcher("/views/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String code = request.getParameter("code");
        String password = request.getParameter("password");

        if (code == null || code.trim().isEmpty() || password == null || password.isEmpty()) {
            request.setAttribute("errorMessage", "Vui lòng nhập đầy đủ Mã người dùng và Mật khẩu.");
            request.setAttribute("param_code", code);
            request.getRequestDispatcher("/views/login.jsp").forward(request, response);
            return;
        }

        code = code.trim();
        UserDao userDao = Database.getUserDao();
        User user = userDao.findUserByCode(code);

        if (user != null && SecurityUtils.passwordMatches(password, user.getPassword())) {
            if (user.isBanned()) {
                request.setAttribute("errorMessage", "Tài khoản của bạn đã bị tạm khóa bởi Quản trị viên. Vui lòng liên hệ để được hỗ trợ.");
                request.setAttribute("param_code", code);
                request.getRequestDispatcher("/views/login.jsp").forward(request, response);
                return;
            }

            HttpSession session = request.getSession(true);
            request.changeSessionId();
            session.setAttribute("user", user);

            if (user.isAdmin()) {
                response.sendRedirect(request.getContextPath() + "/admin/dashboard");
            } else {
                response.sendRedirect(request.getContextPath() + "/home");
            }
        } else {
            request.setAttribute("errorMessage", "Mã người dùng hoặc mật khẩu không chính xác.");
            request.setAttribute("param_code", code);
            request.getRequestDispatcher("/views/login.jsp").forward(request, response);
        }
    }
}
