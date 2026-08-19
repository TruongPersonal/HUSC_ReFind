package controller.student;

import data.dao.CategoryDao;
import data.dao.Database;
import data.dao.ItemDao;
import data.dao.LocationDao;
import data.dao.SavedItemDao;
import data.utils.HttpUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Category;
import model.Item;
import model.Location;
import model.User;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@WebServlet(name = "HomeServlet", urlPatterns = {"/home"})
public class HomeServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        ItemDao itemDao = Database.getItemDao();
        CategoryDao categoryDao = Database.getCategoryDao();
        LocationDao locationDao = Database.getLocationDao();
        SavedItemDao savedItemDao = Database.getSavedItemDao();

        String currentTab = request.getParameter("tab");
        if (currentTab == null || (!currentTab.equalsIgnoreCase("FOUND") && !currentTab.equalsIgnoreCase("LOST"))) {
            currentTab = "LOST";
        } else {
            currentTab = currentTab.toUpperCase();
        }

        int categoryId = HttpUtils.getIntParam(request, "category_id", 0);
        int locationId = HttpUtils.getIntParam(request, "location_id", 0);
        String keyword = HttpUtils.getStringParam(request, "keyword", null);

        List<Item> items = itemDao.searchAndFilterItems(currentTab, categoryId, locationId, keyword);

        List<Category> categories = categoryDao.allCategories();
        List<Location> locations = locationDao.allLocations();

        HttpSession session = request.getSession(false);
        Set<Integer> savedItemIds = new HashSet<>();
        if (session != null && session.getAttribute("user") != null) {
            User user = (User) session.getAttribute("user");
            List<Item> savedList = savedItemDao.getSavedItemsByUserId(user.getId());
            for (Item item : savedList) {
                savedItemIds.add(item.getId());
            }
        }

        int lostCount = itemDao.countItemsByStatus(1);
        int foundCount = itemDao.countItemsByStatus(2);
        request.setAttribute("items", items);
        request.setAttribute("categories", categories);
        request.setAttribute("locations", locations);
        request.setAttribute("currentTab", currentTab);
        request.setAttribute("selectedCategory", categoryId);
        request.setAttribute("selectedLocation", locationId);
        request.setAttribute("keyword", keyword);
        request.setAttribute("savedItemIds", savedItemIds);
        request.setAttribute("lostCount", lostCount);
        request.setAttribute("foundCount", foundCount);

        request.getRequestDispatcher("/views/home.jsp").forward(request, response);
    }
}
