package controller.student;

import data.dao.Database;
import data.dao.SavedItemDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Item;
import model.User;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@WebServlet(name = "SavedPostsServlet", urlPatterns = {"/saved-items"})
public class SavedPostsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        User currentUser = (User) session.getAttribute("user");

        SavedItemDao savedItemDao = Database.getSavedItemDao();
        List<Item> savedItems = savedItemDao.getSavedItemsByUserId(currentUser.getId());

        Set<Integer> savedItemIds = new HashSet<>();
        for (Item item : savedItems) {
            savedItemIds.add(item.getId());
        }

        request.setAttribute("items", savedItems);
        request.setAttribute("savedItemIds", savedItemIds);
        request.getRequestDispatcher("/views/saved_items.jsp").forward(request, response);
    }
}
