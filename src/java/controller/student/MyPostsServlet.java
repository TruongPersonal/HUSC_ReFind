package controller.student;

import data.dao.Database;
import data.dao.ItemDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Item;
import model.User;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "MyPostsServlet", urlPatterns = {"/my-posts"})
public class MyPostsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        User currentUser = (User) session.getAttribute("user");

        String statusStr = request.getParameter("status");
        Integer statusFilter = null;
        if (statusStr != null && !statusStr.trim().isEmpty() && !statusStr.equalsIgnoreCase("ALL")) {
            try {
                statusFilter = Integer.parseInt(statusStr.trim());
            } catch (NumberFormatException ignored) {}
        }

        ItemDao itemDao = Database.getItemDao();
        List<Item> myItems = itemDao.getItemsByUserId(currentUser.getId(), statusFilter);

        request.setAttribute("items", myItems);
        request.setAttribute("categories", Database.getCategoryDao().allCategories());
        request.setAttribute("locations", Database.getLocationDao().allLocations());
        request.setAttribute("selectedStatus", statusStr != null ? statusStr : "ALL");

        request.getRequestDispatcher("/views/my_posts.jsp").forward(request, response);
    }
}
