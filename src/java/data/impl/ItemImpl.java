package data.impl;

import data.dao.ItemDao;
import data.driver.MySQLDriver;
import model.Item;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ItemImpl implements ItemDao {
    private static final Logger LOGGER = Logger.getLogger(ItemImpl.class.getName());

    private static final String BASE_SELECT =
        "SELECT i.*, c.name AS category_name, l.name AS location_name, " +
        "u.name AS author_name, u.phone AS author_phone, u.code AS author_code, u.email AS author_email, u.role AS author_role " +
        "FROM items i " +
        "JOIN categories c ON i.category_id = c.id " +
        "JOIN locations l ON i.location_id = l.id " +
        "LEFT JOIN users u ON i.user_id = u.id ";

    @Override
    public List<Item> searchAndFilterItems(String type, int categoryId, int locationId, String keyword) {
        List<Item> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder(BASE_SELECT).append(" WHERE 1=1 ");
        List<Object> params = new ArrayList<>();

        if ("FOUND".equalsIgnoreCase(type)) {

            sql.append(" AND i.status = 2 ");
        } else if ("LOST".equalsIgnoreCase(type)) {

            sql.append(" AND i.status = 1 ");
        } else {

            sql.append(" AND i.status IN (1, 2) ");
        }

        if (categoryId > 0) {
            sql.append(" AND i.category_id = ? ");
            params.add(categoryId);
        }

        if (locationId > 0) {
            sql.append(" AND i.location_id = ? ");
            params.add(locationId);
        }

        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append(" AND (i.title LIKE ? OR i.description LIKE ?) ");
            String searchPattern = "%" + keyword.trim() + "%";
            params.add(searchPattern);
            params.add(searchPattern);
        }

        sql.append(" ORDER BY i.created_at DESC");

        try (Connection con = MySQLDriver.getConnection();
             PreparedStatement ps = con.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new Item(rs));
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Lỗi searchAndFilterItems: ", ex);
        }
        return list;
    }

    @Override
    public Item getItemById(int id) {
        String sql = BASE_SELECT + " WHERE i.id = ?";
        try (Connection con = MySQLDriver.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Item(rs);
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Lỗi getItemById: ", ex);
        }
        return null;
    }

    @Override
    public List<Item> getSmartMatchingItems(int categoryId, int locationId, int currentItemId) {
        List<Item> list = new ArrayList<>();
        String sql = BASE_SELECT +
                     " WHERE i.status = 2 AND i.category_id = ? AND i.location_id = ? AND i.id != ? " +
                     " ORDER BY i.created_at DESC";
        try (Connection con = MySQLDriver.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, categoryId);
            ps.setInt(2, locationId);
            ps.setInt(3, currentItemId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new Item(rs));
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Lỗi getSmartMatchingItems: ", ex);
        }
        return list;
    }

    @Override
    public boolean insertItem(String title, int categoryId, int locationId, String description, String image, int status, String adminNote, Integer userId) {
        String sql = "INSERT INTO items (title, category_id, location_id, description, image, status, admin_note, user_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = MySQLDriver.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, title.trim());
            ps.setInt(2, categoryId);
            ps.setInt(3, locationId);
            ps.setString(4, description != null ? description.trim() : "");
            ps.setString(5, image != null ? image.trim() : "");
            ps.setInt(6, status);
            ps.setString(7, (adminNote != null && !adminNote.trim().isEmpty()) ? adminNote.trim() : null);
            if (userId != null && userId > 0) {
                ps.setInt(8, userId);
            } else {
                ps.setNull(8, java.sql.Types.INTEGER);
            }
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Lỗi insertItem: ", ex);
        }
        return false;
    }

    @Override
    public boolean updateItem(int id, String title, int categoryId, int locationId, String description, String image, int userId) {
        String sql = "UPDATE items SET title = ?, category_id = ?, location_id = ?, description = ?, image = ? WHERE id = ? AND user_id = ? AND status = 1";
        try (Connection con = MySQLDriver.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, title.trim());
            ps.setInt(2, categoryId);
            ps.setInt(3, locationId);
            ps.setString(4, description != null ? description.trim() : "");
            ps.setString(5, image != null ? image.trim() : "");
            ps.setInt(6, id);
            ps.setInt(7, userId);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Lỗi updateItem: ", ex);
        }
        return false;
    }

    @Override
    public boolean updateItemByAdmin(int id, String title, int categoryId, int locationId, String description, String image, String adminNote) {
        String sql = "UPDATE items SET title = ?, category_id = ?, location_id = ?, description = ?, image = ?, admin_note = ? WHERE id = ?";
        try (Connection con = MySQLDriver.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, title.trim());
            ps.setInt(2, categoryId);
            ps.setInt(3, locationId);
            ps.setString(4, description != null ? description.trim() : "");
            ps.setString(5, image != null ? image.trim() : "");
            ps.setString(6, (adminNote != null && !adminNote.trim().isEmpty()) ? adminNote.trim() : null);
            ps.setInt(7, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Lỗi updateItemByAdmin: ", ex);
        }
        return false;
    }

    @Override
    public boolean deleteItem(int id) {
        String sql = "DELETE FROM items WHERE id = ?";
        try (Connection con = MySQLDriver.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Lỗi deleteItem: ", ex);
        }
        return false;
    }

    @Override
    public boolean updateStatusAndNote(int id, int status, String adminNote) {
        String sql = "UPDATE items SET status = ?, admin_note = ? WHERE id = ?";
        try (Connection con = MySQLDriver.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, status);
            ps.setString(2, (adminNote != null && !adminNote.trim().isEmpty()) ? adminNote.trim() : null);
            ps.setInt(3, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Lỗi updateStatusAndNote: ", ex);
        }
        return false;
    }

    @Override
    public boolean closeItem(int id) {
        String sql = "UPDATE items SET status = 0 WHERE id = ?";
        try (Connection con = MySQLDriver.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Lỗi closeItem: ", ex);
        }
        return false;
    }

    @Override
    public List<Item> getItemsByUserId(int userId, Integer statusFilter) {
        List<Item> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder(BASE_SELECT).append(" WHERE i.user_id = ? ");
        if (statusFilter != null) {
            sql.append(" AND i.status = ? ");
        }
        sql.append(" ORDER BY i.created_at DESC");

        try (Connection con = MySQLDriver.getConnection();
             PreparedStatement ps = con.prepareStatement(sql.toString())) {
            ps.setInt(1, userId);
            if (statusFilter != null) {
                ps.setInt(2, statusFilter);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new Item(rs));
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Lỗi getItemsByUserId: ", ex);
        }
        return list;
    }

    @Override
    public List<Item> getAllItems(String typeFilter, Integer statusFilter) {
        return getAllItems(typeFilter, statusFilter, null, null);
    }

    @Override
    public List<Item> getAllItems(String typeFilter, Integer statusFilter, String roleFilter, String keyword) {
        List<Item> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder(BASE_SELECT).append(" WHERE 1=1 ");
        List<Object> params = new ArrayList<>();

        if (typeFilter != null && !typeFilter.trim().isEmpty() && !typeFilter.equalsIgnoreCase("ALL")) {
            sql.append(" AND i.type = ? ");
            params.add(typeFilter.trim().toUpperCase());
        }

        if (statusFilter != null) {
            sql.append(" AND i.status = ? ");
            params.add(statusFilter);
        }

        if (roleFilter != null && !roleFilter.trim().isEmpty() && !roleFilter.equalsIgnoreCase("ALL")) {
            if ("ADMIN".equalsIgnoreCase(roleFilter)) {
                sql.append(" AND (i.user_id IS NULL OR u.role = 'admin') ");
            } else if ("STUDENT".equalsIgnoreCase(roleFilter)) {
                sql.append(" AND (i.user_id IS NOT NULL AND (u.role != 'admin' OR u.role IS NULL)) ");
            }
        }

        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append(" AND (i.title LIKE ? OR i.description LIKE ? OR u.name LIKE ? OR u.code LIKE ?) ");
            String searchPattern = "%" + keyword.trim() + "%";
            params.add(searchPattern);
            params.add(searchPattern);
            params.add(searchPattern);
            params.add(searchPattern);
        }

        sql.append(" ORDER BY i.created_at DESC");

        try (Connection con = MySQLDriver.getConnection();
             PreparedStatement ps = con.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new Item(rs));
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Lỗi getAllItems: ", ex);
        }
        return list;
    }

    @Override
    public int countTotalItems() {
        String sql = "SELECT COUNT(*) FROM items";
        try (Connection con = MySQLDriver.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Lỗi countTotalItems: ", ex);
        }
        return 0;
    }

    @Override
    public int countItemsByStatus(int status) {
        String sql = "SELECT COUNT(*) FROM items WHERE status = ?";
        try (Connection con = MySQLDriver.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, status);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Lỗi countItemsByStatus: ", ex);
        }
        return 0;
    }
}
