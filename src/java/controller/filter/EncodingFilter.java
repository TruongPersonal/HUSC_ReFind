package controller.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(filterName = "EncodingFilter", urlPatterns = {"/*"})
public class EncodingFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        req.setCharacterEncoding("UTF-8");
        res.setCharacterEncoding("UTF-8");

        ServletContext ctx = req.getServletContext();
        if (ctx.getAttribute("globalCategories") == null) {
            try {
                ctx.setAttribute("globalCategories", data.dao.Database.getCategoryDao().allCategories());
            } catch (Exception ignored) {}
        }
        if (ctx.getAttribute("globalLocations") == null) {
            try {
                ctx.setAttribute("globalLocations", data.dao.Database.getLocationDao().allLocations());
            } catch (Exception ignored) {}
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }
}
