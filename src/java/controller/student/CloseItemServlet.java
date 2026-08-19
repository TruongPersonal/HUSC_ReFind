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

@WebServlet(name = "CloseItemServlet", urlPatterns = {"/close-item"})
public class CloseItemServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
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

        HttpSession session = request.getSession(false);
        User currentUser = (User) session.getAttribute("user");

        ItemDao itemDao = Database.getItemDao();
        Item item = itemDao.getItemById(itemId);

        if (item != null) {
            if (currentUser.isAdmin() || item.getUserId() == currentUser.getId()) {
                itemDao.closeItem(itemId);
                session.setAttribute("successMessage", "Đồ vật đã được nhận lại thành công!");
            } else {
                session.setAttribute("errorMessage", "Bạn không có quyền thực hiện thao tác này.");
            }
        }

        response.sendRedirect(request.getContextPath() + "/item-detail?id=" + itemId);
    }

}
