package data.impl;

import data.dao.SavedItemDao;
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

public class SavedItemImpl implements SavedItemDao {
    private static final Logger LOGGER = Logger.getLogger(SavedItemImpl.class.getName());

    @Override
    public boolean isSaved(int userId, int itemId) {
        String sql = "SELECT id FROM saved_items WHERE user_id = ? AND item_id = ?";
        try (Connection con = MySQLDriver.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, itemId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Lỗi isSaved: ", ex);
        }
        return false;
    }

    @Override
    public boolean toggleSaveItem(int userId, int itemId) {
        if (isSaved(userId, itemId)) {
            removeSavedItem(userId, itemId);
            return false;
        } else {
            String sql = "INSERT INTO saved_items (user_id, item_id) VALUES (?, ?)";
            try (Connection con = MySQLDriver.getConnection();
                 PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, userId);
                ps.setInt(2, itemId);
                ps.executeUpdate();
                return true;
            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, "Lỗi toggleSaveItem insert: ", ex);
            }
        }
        return false;
    }

    @Override
    public List<Item> getSavedItemsByUserId(int userId) {
        List<Item> list = new ArrayList<>();
        String sql = "SELECT i.*, c.name AS category_name, l.name AS location_name, " +
                     "u.name AS author_name, u.phone AS author_phone, u.code AS author_code, u.email AS author_email " +
                     "FROM saved_items s " +
                     "JOIN items i ON s.item_id = i.id " +
                     "JOIN categories c ON i.category_id = c.id " +
                     "JOIN locations l ON i.location_id = l.id " +
                     "JOIN users u ON i.user_id = u.id " +
                     "WHERE s.user_id = ? " +
                     "ORDER BY s.created_at DESC";
        try (Connection con = MySQLDriver.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new Item(rs));
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Lỗi getSavedItemsByUserId: ", ex);
        }
        return list;
    }

    @Override
    public boolean removeSavedItem(int userId, int itemId) {
        String sql = "DELETE FROM saved_items WHERE user_id = ? AND item_id = ?";
        try (Connection con = MySQLDriver.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, itemId);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Lỗi removeSavedItem: ", ex);
        }
        return false;
    }
}
