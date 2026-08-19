package controller.student;

import data.dao.Database;
import data.dao.SavedItemDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.User;
import java.io.IOException;

@WebServlet(name = "BookmarkServlet", urlPatterns = {"/bookmark"})
public class BookmarkServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"success\":false,\"message\":\"Vui lòng đăng nhập để lưu tin\"}");
            return;
        }

        User currentUser = (User) session.getAttribute("user");
        String itemIdStr = request.getParameter("item_id");

        if (itemIdStr == null || itemIdStr.trim().isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"success\":false,\"message\":\"Thiếu mã món đồ\"}");
            return;
        }

        int itemId;
        try {
            itemId = Integer.parseInt(itemIdStr.trim());
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"success\":false,\"message\":\"Mã món đồ không hợp lệ\"}");
            return;
        }

        SavedItemDao savedItemDao = Database.getSavedItemDao();
        boolean isNowSaved = savedItemDao.toggleSaveItem(currentUser.getId(), itemId);

        String message = isNowSaved ? "Đã lưu tin vào danh sách theo dõi" : "Đã bỏ lưu tin";
        response.getWriter().write(String.format("{\"success\":true,\"saved\":%b,\"message\":\"%s\"}", isNowSaved, message));
    }
}
