package controller.admin;

import data.dao.Database;
import data.dao.LocationDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Location;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "AdminLocationServlet", urlPatterns = {"/admin/locations"})
public class AdminLocationServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        LocationDao locationDao = Database.getLocationDao();
        List<Location> locations = locationDao.allLocations();

        request.setAttribute("locations", locations);
        request.getRequestDispatcher("/views/admin/location_manager.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        LocationDao locationDao = Database.getLocationDao();
        HttpSession session = request.getSession(true);

        if ("add".equalsIgnoreCase(action)) {
            String name = request.getParameter("name");
            if (name != null && !name.trim().isEmpty()) {
                locationDao.insertLocation(name.trim());
                session.setAttribute("successMessage", "Thêm địa điểm mới thành công!");
            }
        } else if ("update".equalsIgnoreCase(action)) {
            String idStr = request.getParameter("id");
            String name = request.getParameter("name");
            if (idStr != null && name != null && !name.trim().isEmpty()) {
                locationDao.updateLocation(Integer.parseInt(idStr.trim()), name.trim());
                session.setAttribute("successMessage", "Cập nhật địa điểm thành công!");
            }
        } else if ("delete".equalsIgnoreCase(action)) {
            String idStr = request.getParameter("id");
            if (idStr != null && !idStr.trim().isEmpty()) {
                try {
                    boolean ok = locationDao.deleteLocation(Integer.parseInt(idStr.trim()));
                    if (ok) {
                        session.setAttribute("successMessage", "Xóa địa điểm thành công.");
                    } else {
                        session.setAttribute("errorMessage", "Không thể xóa địa điểm đang có bài viết liên kết.");
                    }
                } catch (Exception e) {
                    session.setAttribute("errorMessage", "Không thể xóa địa điểm đang có bài viết liên kết.");
                }
            }
        }

        response.sendRedirect(request.getContextPath() + "/admin/locations");
    }
}
