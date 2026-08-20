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

@WebServlet(name = "DeletePostServlet", urlPatterns = {"/delete-item"})
public class DeletePostServlet extends HttpServlet {

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
            if (currentUser.isAdmin() || (item.getUserId() != null && item.getUserId().equals(currentUser.getId()))) {
                String image = item.getImage();
                boolean deleted = itemDao.deleteItem(itemId);
                if (deleted) {
                    if (image != null && !image.trim().isEmpty()) {
                        String uploadPath = request.getServletContext().getRealPath("/assets/uploads/items");
                        data.utils.UploadUtils.deleteUploadedFile(image, uploadPath);
                    }
                    session.setAttribute("successMessage", "Xóa bài viết thành công.");
                } else {
                    session.setAttribute("errorMessage", "Xóa bài viết thất bại.");
                }
            } else {
                session.setAttribute("errorMessage", "Bạn không có quyền xóa bài viết này.");
            }
        }

        response.sendRedirect(request.getContextPath() + (currentUser.isAdmin() ? "/admin/dashboard" : "/my-posts"));
    }

}
