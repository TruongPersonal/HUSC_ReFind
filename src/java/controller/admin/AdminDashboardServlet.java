package controller.admin;

import data.dao.Database;
import data.dao.ItemDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Item;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "AdminDashboardServlet", urlPatterns = {"/admin/dashboard"})
public class AdminDashboardServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        ItemDao itemDao = Database.getItemDao();

        int totalItems = itemDao.countTotalItems();
        int pendingLost = itemDao.countItemsByStatus(1);
        int holdingCabinet = itemDao.countItemsByStatus(2);
        int returnedItems = itemDao.countItemsByStatus(0);

        List<Item> allItems = itemDao.getAllItems(null, null);

        Map<String, Integer> categoryStats = new LinkedHashMap<>();
        Map<String, Integer> locationStats = new LinkedHashMap<>();

        for (Item item : allItems) {
            String cat = (item.getRawCategoryName() != null && !item.getRawCategoryName().isEmpty())
                    ? item.getRawCategoryName() : "Khác";
            categoryStats.put(cat, categoryStats.getOrDefault(cat, 0) + 1);

            String loc = (item.getRawLocationName() != null && !item.getRawLocationName().isEmpty())
                    ? item.getRawLocationName() : "Khác";
            locationStats.put(loc, locationStats.getOrDefault(loc, 0) + 1);
        }

        request.setAttribute("totalItems", totalItems);
        request.setAttribute("pendingLost", pendingLost);
        request.setAttribute("holdingCabinet", holdingCabinet);
        request.setAttribute("returnedItems", returnedItems);
        request.setAttribute("categoryStats", categoryStats);
        request.setAttribute("locationStats", locationStats);

        request.getRequestDispatcher("/views/admin/dashboard.jsp").forward(request, response);
    }
}
