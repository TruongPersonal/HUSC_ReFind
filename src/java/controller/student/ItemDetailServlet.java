package controller.student;

import data.dao.Database;
import data.dao.ItemDao;
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
import java.util.List;

@WebServlet(name = "ItemDetailServlet", urlPatterns = {"/item-detail"})
public class ItemDetailServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String idStr = request.getParameter("id");
        if (idStr == null || idStr.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/home");
            return;
        }

        int itemId;
        try {
            itemId = Integer.parseInt(idStr.trim());
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/home");
            return;
        }

        ItemDao itemDao = Database.getItemDao();
        SavedItemDao savedItemDao = Database.getSavedItemDao();

        Item item = itemDao.getItemById(itemId);
        if (item == null) {
            request.getSession(true).setAttribute("errorMessage", "Không tìm thấy thông tin món đồ yêu cầu.");
            response.sendRedirect(request.getContextPath() + "/home");
            return;
        }

        List<Item> matchingItems = null;
        if (item.isLost() && item.getStatus() == 1) {
            matchingItems = itemDao.getSmartMatchingItems(item.getCategoryId(), item.getLocationId(), item.getId());
        }

        boolean isSaved = false;
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            User user = (User) session.getAttribute("user");
            isSaved = savedItemDao.isSaved(user.getId(), itemId);
        }

        request.setAttribute("item", item);
        request.setAttribute("matchingItems", matchingItems);
        request.setAttribute("isSaved", isSaved);
        request.setAttribute("categories", Database.getCategoryDao().allCategories());
        request.setAttribute("locations", Database.getLocationDao().allLocations());

        request.getRequestDispatcher("/views/item_detail.jsp").forward(request, response);
    }
}
