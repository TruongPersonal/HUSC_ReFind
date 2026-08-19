package controller.student;

import data.dao.Database;
import data.dao.ItemDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Item;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet(name = "LandingServlet", urlPatterns = {"/landing", ""})
public class LandingServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        ItemDao itemDao = Database.getItemDao();

        request.setAttribute("totalItems", itemDao.countTotalItems());
        request.setAttribute("returnedItems", itemDao.countItemsByStatus(0));
        request.setAttribute("holdingItems", itemDao.countItemsByStatus(2));

        List<Item> allItems = itemDao.getAllItems(null, null);
        List<Item> recentItems = allItems.stream()
                .filter(item -> item.getStatus() == 1 || item.getStatus() == 2)
                .limit(8)
                .collect(Collectors.toList());

        request.setAttribute("recentItems", recentItems);

        request.getRequestDispatcher("/views/landing.jsp").forward(request, response);
    }
}
