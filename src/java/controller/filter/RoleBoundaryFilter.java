package controller.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.User;
import data.dao.Database;
import java.io.IOException;
import java.util.Set;

@WebFilter(filterName = "RoleBoundaryFilter", urlPatterns = {"/*"})
public class RoleBoundaryFilter implements Filter {

    private static final Set<String> AUTH_REQUIRED_ROUTES = Set.of(
        "/post-item",
        "/edit-item",
        "/delete-item",
        "/close-item",
        "/my-posts",
        "/saved-items",
        "/profile",
        "/update-profile",
        "/change-password",
        "/bookmark"
    );

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String path = req.getServletPath();

        if (path.startsWith("/assets/") || path.startsWith("/css/") || path.startsWith("/js/") || path.startsWith("/img/") || path.startsWith("/api/") || path.equals("/chatbot")) {
            chain.doFilter(request, response);
            return;
        }

        HttpSession session = req.getSession(false);
        User currentUser = (session != null) ? (User) session.getAttribute("user") : null;

        if (currentUser != null) {
            User freshUser = Database.getUserDao().findUserById(currentUser.getId());
            if (freshUser == null || freshUser.isBanned()) {
                session.invalidate();
                currentUser = null;
            } else {
                session.setAttribute("user", freshUser);
                currentUser = freshUser;
            }
        }

        if (currentUser != null && currentUser.isAdmin()) {
            if (!path.startsWith("/admin") && !path.equals("/logout")) {
                res.sendRedirect(req.getContextPath() + "/admin/dashboard");
                return;
            }
            chain.doFilter(request, response);
            return;
        }

        if (currentUser != null && !currentUser.isAdmin()) {

            if (path.startsWith("/admin")) {
                res.sendRedirect(req.getContextPath() + "/home");
                return;
            }
            chain.doFilter(request, response);
            return;
        }

        if (currentUser == null) {

            if (path.startsWith("/admin")) {
                res.sendRedirect(req.getContextPath() + "/login");
                return;
            }

            if (AUTH_REQUIRED_ROUTES.contains(path)) {
                String xRequestedWith = req.getHeader("X-Requested-With");
                if ("XMLHttpRequest".equalsIgnoreCase(xRequestedWith)) {
                    res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    res.setContentType("application/json");
                    res.getWriter().write("{\"success\":false,\"message\":\"Vui lòng đăng nhập để tiếp tục.\"}");
                    return;
                }

                res.sendRedirect(req.getContextPath() + "/login");
                return;
            }
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }
}
