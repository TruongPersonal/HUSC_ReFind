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
import model.User;
import java.io.IOException;

@WebServlet(name = "CreatePostServlet", urlPatterns = {"/post-item"})
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024 * 2,
    maxFileSize = 1024 * 1024 * 10,
    maxRequestSize = 1024 * 1024 * 20
)
public class CreatePostServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect(request.getContextPath() + "/home");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        User currentUser = (User) session.getAttribute("user");

        String title = request.getParameter("title");
        String categoryIdStr = request.getParameter("category_id");
        String locationIdStr = request.getParameter("location_id");
        String description = request.getParameter("description");
        Part filePart = request.getPart("image");

        if (title == null || title.trim().isEmpty() ||
            categoryIdStr == null || categoryIdStr.trim().isEmpty() ||
            locationIdStr == null || locationIdStr.trim().isEmpty() ||
            filePart == null || filePart.getSize() == 0) {
            session.setAttribute("errorMessage", "Vui lòng nhập đầy đủ tiêu đề, danh mục, địa điểm và chọn 1 bức ảnh minh họa bắt buộc.");
            response.sendRedirect(request.getContextPath() + "/home?tab=LOST");
            return;
        }

        int categoryId = Integer.parseInt(categoryIdStr.trim());
        int locationId = Integer.parseInt(locationIdStr.trim());

        String uploadPath = request.getServletContext().getRealPath("/assets/uploads/items");
        String uniqueFileName;
        try {
            uniqueFileName = UploadUtils.saveUploadedFile(filePart, uploadPath);
        } catch (Exception e) {
            session.setAttribute("errorMessage", "Có lỗi xảy ra khi tải ảnh lên. Vui lòng thử lại.");
            response.sendRedirect(request.getContextPath() + "/home?tab=LOST");
            return;
        }

        if (uniqueFileName == null) {
            session.setAttribute("errorMessage", "Chỉ chấp nhận file ảnh có định dạng .jpg, .jpeg, .png, .webp");
            response.sendRedirect(request.getContextPath() + "/home?tab=LOST");
            return;
        }

        ItemDao itemDao = Database.getItemDao();
        boolean created = itemDao.insertItem(title, categoryId, locationId, description, uniqueFileName, 1, null, currentUser.getId());

        if (created) {
            session.setAttribute("successMessage", "Đăng tin báo mất đồ thành công! Bài viết đã được hiển thị công khai.");
        } else {
            session.setAttribute("errorMessage", "Có lỗi xảy ra khi lưu bài viết. Vui lòng thử lại.");
        }
        response.sendRedirect(request.getContextPath() + "/home?tab=LOST");
    }
}
