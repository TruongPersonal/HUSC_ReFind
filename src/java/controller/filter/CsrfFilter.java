package controller.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

@WebFilter(urlPatterns = "/*")
public class CsrfFilter implements Filter {
    private static final SecureRandom RANDOM = new SecureRandom();

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        HttpSession session = req.getSession(true);
        String token = (String) session.getAttribute("csrfToken");
        if (token == null) {
            byte[] bytes = new byte[32];
            RANDOM.nextBytes(bytes);
            token = Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
            session.setAttribute("csrfToken", token);
        }

        String path = req.getServletPath();
        if (path != null && (path.startsWith("/api/chatbot") || path.startsWith("/chatbot"))) {
            chain.doFilter(request, response);
            return;
        }

        if ("POST".equalsIgnoreCase(req.getMethod())) {
            String submitted = req.getHeader("X-CSRF-Token");
            if (submitted == null || submitted.isEmpty()) submitted = req.getParameter("csrf_token");
            if (submitted == null || !MessageDigest.isEqual(token.getBytes(StandardCharsets.UTF_8), submitted.getBytes(StandardCharsets.UTF_8))) {
                res.sendError(HttpServletResponse.SC_FORBIDDEN, "Yêu cầu không hợp lệ hoặc đã hết hạn. Vui lòng tải lại trang.");
                return;
            }
        }
        chain.doFilter(request, response);
    }
}
