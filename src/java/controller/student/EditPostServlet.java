package controller.student;

import data.dao.Database;
import data.dao.ItemDao;
import data.utils.UploadUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import model.Item;
import model.User;
import java.io.IOException;

@WebServlet(name = "EditPostServlet", urlPatterns = {"/edit-item"})
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024 * 2,
    maxFileSize = 1024 * 1024 * 10,
    maxRequestSize = 1024 * 1024 * 20
)
public class EditPostServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect(request.getContextPath() + "/my-posts");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        User currentUser = (User) session.getAttribute("user");

        String idStr = request.getParameter("id");
        String title = request.getParameter("title");
        String categoryIdStr = request.getParameter("category_id");
        String locationIdStr = request.getParameter("location_id");
        String description = request.getParameter("description");
        String adminNote = request.getParameter("admin_note");
        Part filePart = request.getPart("image");

        if (idStr == null || title == null || title.trim().isEmpty() ||
            categoryIdStr == null || locationIdStr == null) {
            session.setAttribute("errorMessage", "Vui lòng điền đầy đủ các thông tin bắt buộc.");
            response.sendRedirect(request.getContextPath() + "/my-posts");
            return;
        }

        int itemId = Integer.parseInt(idStr.trim());
        int categoryId = Integer.parseInt(categoryIdStr.trim());
        int locationId = Integer.parseInt(locationIdStr.trim());

        ItemDao itemDao = Database.getItemDao();
        Item existingItem = itemDao.getItemById(itemId);

        if (existingItem == null) {
            session.setAttribute("errorMessage", "Không tìm thấy bài viết.");
            response.sendRedirect(request.getContextPath() + "/my-posts");
            return;
        }

        if (!currentUser.isAdmin() && (existingItem.getUserId() != currentUser.getId() || existingItem.getStatus() != 1)) {
            session.setAttribute("errorMessage", "Bạn không có quyền chỉnh sửa bài viết này.");
            response.sendRedirect(request.getContextPath() + "/my-posts");
            return;
        }

        String oldImage = existingItem.getImage();
        String imageName = oldImage;

        if (filePart != null && filePart.getSize() > 0) {
            String uploadPath = request.getServletContext().getRealPath("/assets/uploads/items");
            try {
                String uploaded = UploadUtils.saveUploadedFile(filePart, uploadPath);
                if (uploaded != null) {
                    imageName = uploaded;
                    if (oldImage != null && !oldImage.trim().isEmpty() && !oldImage.equals(uploaded)) {
                        UploadUtils.deleteUploadedFile(oldImage, uploadPath);
                    }
                }
            } catch (Exception ignored) {}
        }

        boolean updated;
        if (currentUser.isAdmin()) {
            String noteToSave = (adminNote != null && !adminNote.trim().isEmpty()) ? adminNote.trim() : existingItem.getRawAdminNote();
            updated = itemDao.updateItemByAdmin(itemId, title, categoryId, locationId, description, imageName, noteToSave);
        } else {
            updated = itemDao.updateItem(itemId, title, categoryId, locationId, description, imageName, currentUser.getId());
        }

        if (updated) {
            session.setAttribute("successMessage", "Cập nhật bài viết thành công!");
        } else {
            session.setAttribute("errorMessage", "Cập nhật bài viết thất bại.");
        }

        String redirectUrl = request.getParameter("redirect_url");
        String contextPath = request.getContextPath();
        if (redirectUrl != null && redirectUrl.startsWith(contextPath + "/") && !redirectUrl.startsWith("//")) {
            response.sendRedirect(redirectUrl);
            return;
        }
        response.sendRedirect(contextPath + (currentUser.isAdmin() ? "/admin/items" : "/my-posts"));
    }
}
