package data.impl;

import data.dao.LocationDao;
import data.driver.MySQLDriver;
import model.Location;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LocationImpl implements LocationDao {
    private static final Logger LOGGER = Logger.getLogger(LocationImpl.class.getName());

    @Override
    public List<Location> allLocations() {
        List<Location> list = new ArrayList<>();
        String sql = "SELECT * FROM locations ORDER BY id ASC";
        try (Connection con = MySQLDriver.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new Location(rs));
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Lỗi allLocations: ", ex);
        }
        return list;
    }

    @Override
    public Location findById(int id) {
        String sql = "SELECT * FROM locations WHERE id = ?";
        try (Connection con = MySQLDriver.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Location(rs);
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Lỗi findLocationById: ", ex);
        }
        return null;
    }

    @Override
    public boolean insertLocation(String name) {
        String sql = "INSERT INTO locations (name) VALUES (?)";
        try (Connection con = MySQLDriver.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, name.trim());
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Lỗi insertLocation: ", ex);
        }
        return false;
    }

    @Override
    public boolean updateLocation(int id, String name) {
        String sql = "UPDATE locations SET name = ? WHERE id = ?";
        try (Connection con = MySQLDriver.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, name.trim());
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Lỗi updateLocation: ", ex);
        }
        return false;
    }

    @Override
    public boolean deleteLocation(int id) {
        String sql = "DELETE FROM locations WHERE id = ?";
        try (Connection con = MySQLDriver.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Lỗi deleteLocation: ", ex);
        }
        return false;
    }
}
