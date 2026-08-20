package controller.admin;

import data.dao.CategoryDao;
import data.dao.Database;
import data.dao.ItemDao;
import data.dao.LocationDao;
import data.utils.UploadUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import model.Category;
import model.Item;
import model.Location;
import model.User;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "AdminItemServlet", urlPatterns = {"/admin/items", "/admin/lost-items", "/admin/found-items"})
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024 * 2,
    maxFileSize = 1024 * 1024 * 10,
    maxRequestSize = 1024 * 1024 * 20
)
public class AdminItemServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String statusStr = request.getParameter("status");
        String roleStr = request.getParameter("role");
        String typeStr = request.getParameter("type");
        String keyword = request.getParameter("keyword");

        Integer statusFilter = null;
        if (statusStr != null && !statusStr.trim().isEmpty() && !statusStr.equalsIgnoreCase("ALL")) {
            try {
                statusFilter = Integer.parseInt(statusStr.trim());
            } catch (NumberFormatException ignored) {}
        }

        ItemDao itemDao = Database.getItemDao();
        CategoryDao categoryDao = Database.getCategoryDao();
        LocationDao locationDao = Database.getLocationDao();

        List<Item> items = itemDao.getAllItems(typeStr, statusFilter, roleStr, keyword);
        List<Category> categories = categoryDao.allCategories();
        List<Location> locations = locationDao.allLocations();

        request.setAttribute("items", items);
        request.setAttribute("categories", categories);
        request.setAttribute("locations", locations);
        request.setAttribute("selectedStatus", statusStr != null ? statusStr : "ALL");
        request.setAttribute("selectedRole", roleStr != null ? roleStr : "ALL");
        request.setAttribute("selectedType", typeStr != null ? typeStr : "ALL");
        request.setAttribute("keyword", keyword != null ? keyword.trim() : "");

        request.getRequestDispatcher("/views/admin/items_manager.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        HttpSession session = request.getSession(true);
        User currentUser = (User) session.getAttribute("user");
        ItemDao itemDao = Database.getItemDao();

        if ("create_found".equalsIgnoreCase(action)) {
            String title = request.getParameter("title");
            String categoryIdStr = request.getParameter("category_id");
            String locationIdStr = request.getParameter("location_id");
            String description = request.getParameter("description");
            String adminNote = request.getParameter("admin_note");
            Part filePart = request.getPart("image");

            if (title == null || title.trim().isEmpty() ||
                categoryIdStr == null || locationIdStr == null ||
                filePart == null || filePart.getSize() == 0) {
                session.setAttribute("errorMessage", "Vui lòng nhập đầy đủ tiêu đề, danh mục, địa điểm và tải ảnh lên.");
                response.sendRedirect(request.getContextPath() + "/admin/items");
                return;
            }

            int categoryId = Integer.parseInt(categoryIdStr.trim());
            int locationId = Integer.parseInt(locationIdStr.trim());

            String uploadPath = request.getServletContext().getRealPath("/assets/uploads/items");
            String uniqueFileName;
            try {
                uniqueFileName = UploadUtils.saveUploadedFile(filePart, uploadPath);
            } catch (Exception e) {
                session.setAttribute("errorMessage", "Có lỗi xảy ra khi tải ảnh lên.");
                response.sendRedirect(request.getContextPath() + "/admin/items");
                return;
            }

            if (uniqueFileName == null) {
                session.setAttribute("errorMessage", "Chỉ chấp nhận ảnh JPG, JPEG, PNG hoặc WEBP.");
                response.sendRedirect(request.getContextPath() + "/admin/items");
                return;
            }

            if (adminNote == null || adminNote.trim().isEmpty()) {
                adminNote = "Tủ lưu trữ - Phòng Bảo vệ cổng chính";
            }

            boolean created = itemDao.insertItem(title, categoryId, locationId, description, uniqueFileName, 2, adminNote.trim(), null);
            if (created) {
                session.setAttribute("successMessage", "Thêm mới đồ thất lạc thành công!");
            } else {
                session.setAttribute("errorMessage", "Có lỗi xảy ra khi lưu bài viết.");
            }
        } else if ("hold_in_cabinet".equalsIgnoreCase(action)) {
            String idStr = request.getParameter("id");
            String adminNote = request.getParameter("admin_note");
            if (idStr != null && !idStr.trim().isEmpty()) {
                int itemId = Integer.parseInt(idStr.trim());
                if (adminNote == null || adminNote.trim().isEmpty()) {
                    adminNote = "Phòng trực";
                }
                itemDao.updateStatusAndNote(itemId, 2, adminNote.trim());
                session.setAttribute("successMessage", "Đã tiếp nhận đồ thất lạc và lưu giữ.");
            }
        } else if ("update_item".equalsIgnoreCase(action)) {
            String idStr = request.getParameter("id");
            String title = request.getParameter("title");
            String categoryIdStr = request.getParameter("category_id");
            String locationIdStr = request.getParameter("location_id");
            String description = request.getParameter("description");
            String adminNote = request.getParameter("admin_note");
            Part filePart = request.getPart("image");

            if (idStr != null && !idStr.trim().isEmpty() && title != null && !title.trim().isEmpty()) {
                int itemId = Integer.parseInt(idStr.trim());
                int categoryId = Integer.parseInt(categoryIdStr.trim());
                int locationId = Integer.parseInt(locationIdStr.trim());
                Item item = itemDao.getItemById(itemId);
                if (item != null) {
                    String oldImage = item.getImage();
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
                    boolean updated = itemDao.updateItemByAdmin(itemId, title.trim(), categoryId, locationId, description, imageName, adminNote != null ? adminNote.trim() : null);
                    if (updated) {
                        session.setAttribute("successMessage", "Cập nhật bài viết thành công!");
                    } else {
                        session.setAttribute("errorMessage", "Cập nhật bài viết thất bại.");
                    }
                }
            }
        } else if ("close".equalsIgnoreCase(action)) {
            String idStr = request.getParameter("id");
            if (idStr != null && !idStr.trim().isEmpty()) {
                itemDao.closeItem(Integer.parseInt(idStr.trim()));
                session.setAttribute("successMessage", "Đã đánh dấu hoàn tất trao trả món đồ.");
            }
        } else if ("delete".equalsIgnoreCase(action)) {
            String idStr = request.getParameter("id");
            if (idStr != null && !idStr.trim().isEmpty()) {
                int itemId = Integer.parseInt(idStr.trim());
                Item item = itemDao.getItemById(itemId);
                String image = (item != null) ? item.getImage() : null;
                boolean deleted = itemDao.deleteItem(itemId);
                if (deleted) {
                    if (image != null && !image.trim().isEmpty()) {
                        String uploadPath = request.getServletContext().getRealPath("/assets/uploads/items");
                        UploadUtils.deleteUploadedFile(image, uploadPath);
                    }
                    session.setAttribute("successMessage", "Đã xóa bài viết khỏi hệ thống.");
                } else {
                    session.setAttribute("errorMessage", "Xóa bài viết thất bại.");
                }
            }
        }

        response.sendRedirect(request.getContextPath() + "/admin/items");
    }
}
