package controller.admin;

import data.dao.CategoryDao;
import data.dao.Database;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Category;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "AdminCategoryServlet", urlPatterns = {"/admin/categories"})
public class AdminCategoryServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        CategoryDao categoryDao = Database.getCategoryDao();
        List<Category> categories = categoryDao.allCategories();

        request.setAttribute("categories", categories);
        request.getRequestDispatcher("/views/admin/category_manager.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        CategoryDao categoryDao = Database.getCategoryDao();
        HttpSession session = request.getSession(true);

        if ("add".equalsIgnoreCase(action)) {
            String name = request.getParameter("name");
            if (name != null && !name.trim().isEmpty()) {
                categoryDao.insertCategory(name.trim());
                request.getServletContext().setAttribute("globalCategories", categoryDao.allCategories());
                session.setAttribute("successMessage", "Thêm danh mục mới thành công!");
            }
        } else if ("update".equalsIgnoreCase(action)) {
            String idStr = request.getParameter("id");
            String name = request.getParameter("name");
            if (idStr != null && name != null && !name.trim().isEmpty()) {
                categoryDao.updateCategory(Integer.parseInt(idStr.trim()), name.trim());
                request.getServletContext().setAttribute("globalCategories", categoryDao.allCategories());
                session.setAttribute("successMessage", "Cập nhật danh mục thành công!");
            }
        } else if ("delete".equalsIgnoreCase(action)) {
            String idStr = request.getParameter("id");
            if (idStr != null && !idStr.trim().isEmpty()) {
                try {
                    boolean ok = categoryDao.deleteCategory(Integer.parseInt(idStr.trim()));
                    if (ok) {
                        request.getServletContext().setAttribute("globalCategories", categoryDao.allCategories());
                        session.setAttribute("successMessage", "Xóa danh mục thành công.");
                    } else {
                        session.setAttribute("errorMessage", "Không thể xóa danh mục đang có bài viết liên kết.");
                    }
                } catch (Exception e) {
                    session.setAttribute("errorMessage", "Không thể xóa danh mục đang có bài viết liên kết.");
                }
            }
        }

        response.sendRedirect(request.getContextPath() + "/admin/categories");
    }
}
