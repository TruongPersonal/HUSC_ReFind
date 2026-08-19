package data.impl;

import data.dao.UserDao;
import data.driver.MySQLDriver;
import model.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserImpl implements UserDao {
    private static final Logger LOGGER = Logger.getLogger(UserImpl.class.getName());

    @Override
    public User findUserByCode(String code) {
        String sql = "SELECT * FROM users WHERE code = ?";
        try (Connection con = MySQLDriver.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, code.trim());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new User(rs);
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Lỗi findUserByCode: ", ex);
        }
        return null;
    }

    @Override
    public User findUserById(int id) {
        String sql = "SELECT * FROM users WHERE id = ?";
        try (Connection con = MySQLDriver.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new User(rs);
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Lỗi findUserById: ", ex);
        }
        return null;
    }

    @Override
    public User findUserByEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return null;
        }
        String sql = "SELECT * FROM users WHERE email = ?";
        try (Connection con = MySQLDriver.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, email.trim().toLowerCase());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new User(rs);
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Lỗi findUserByEmail: ", ex);
        }
        return null;
    }

    @Override
    public User findUserByPhone(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            return null;
        }
        String sql = "SELECT * FROM users WHERE phone = ?";
        try (Connection con = MySQLDriver.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, phone.trim());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new User(rs);
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Lỗi findUserByPhone: ", ex);
        }
        return null;
    }

    @Override
    public boolean checkCodeExists(String code) {
        String sql = "SELECT id FROM users WHERE code = ?";
        try (Connection con = MySQLDriver.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, code.trim());
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Lỗi checkCodeExists: ", ex);
        }
        return false;
    }

    @Override
    public boolean checkEmailExists(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        String sql = "SELECT id FROM users WHERE email = ?";
        try (Connection con = MySQLDriver.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, email.trim().toLowerCase());
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Lỗi checkEmailExists: ", ex);
        }
        return false;
    }

    @Override
    public boolean checkPhoneExists(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            return false;
        }
        String sql = "SELECT id FROM users WHERE phone = ?";
        try (Connection con = MySQLDriver.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, phone.trim());
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Lỗi checkPhoneExists: ", ex);
        }
        return false;
    }

    @Override
    public boolean insertUser(String code, String name, String email, String phone, String hashedPassword, String role) {
        String sql = "INSERT INTO users (code, name, email, phone, password, role) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = MySQLDriver.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, code.trim());
            ps.setString(2, name.trim());
            ps.setString(3, (email != null && !email.trim().isEmpty()) ? email.trim().toLowerCase() : null);
            ps.setString(4, (phone != null && !phone.trim().isEmpty()) ? phone.trim() : null);
            ps.setString(5, hashedPassword);
            ps.setString(6, (role != null && !role.trim().isEmpty()) ? role : "student");
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Lỗi insertUser: ", ex);
        }
        return false;
    }

    @Override
    public boolean updateProfile(int userId, String name, String phone, String email) {
        String sql = "UPDATE users SET name = ?, phone = ?, email = ? WHERE id = ?";
        try (Connection con = MySQLDriver.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, name.trim());
            ps.setString(2, phone != null ? phone.trim() : null);
            ps.setString(3, (email != null && !email.trim().isEmpty()) ? email.trim().toLowerCase() : null);
            ps.setInt(4, userId);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Lỗi updateProfile: ", ex);
        }
        return false;
    }

    @Override
    public boolean updatePassword(int userId, String newHashedPassword) {
        String sql = "UPDATE users SET password = ? WHERE id = ?";
        try (Connection con = MySQLDriver.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, newHashedPassword);
            ps.setInt(2, userId);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Lỗi updatePassword: ", ex);
        }
        return false;
    }

    @Override
    public List<User> getAllUsers() {
        List<User> list = new ArrayList<>();
        String sql = "SELECT * FROM users ORDER BY created_at DESC";
        try (Connection con = MySQLDriver.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new User(rs));
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Lỗi getAllUsers: ", ex);
        }
        return list;
    }

    @Override
    public List<User> searchUsers(String keyword, String roleFilter) {
        List<User> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM users WHERE 1=1 ");
        List<Object> params = new ArrayList<>();

        if (roleFilter != null && !roleFilter.trim().isEmpty() && !"ALL".equalsIgnoreCase(roleFilter)) {
            sql.append(" AND role = ? ");
            params.add(roleFilter.trim());
        }

        if (keyword != null && !keyword.trim().isEmpty()) {
            String kw = "%" + keyword.trim() + "%";
            sql.append(" AND (name LIKE ? OR code LIKE ? OR email LIKE ? OR phone LIKE ?) ");
            params.add(kw);
            params.add(kw);
            params.add(kw);
            params.add(kw);
        }

        sql.append(" ORDER BY created_at DESC");

        try (Connection con = MySQLDriver.getConnection();
             PreparedStatement ps = con.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new User(rs));
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Lỗi searchUsers: ", ex);
        }
        return list;
    }

    @Override
    public boolean updateUserByAdmin(int userId, String code, String name, String email, String phone, String role, String newHashedPassword) {
        StringBuilder sql = new StringBuilder("UPDATE users SET code = ?, name = ?, email = ?, phone = ?, role = ?");
        if (newHashedPassword != null && !newHashedPassword.trim().isEmpty()) {
            sql.append(", password = ?");
        }
        sql.append(" WHERE id = ?");

        try (Connection con = MySQLDriver.getConnection();
             PreparedStatement ps = con.prepareStatement(sql.toString())) {
            ps.setString(1, code.trim());
            ps.setString(2, name.trim());
            ps.setString(3, (email != null && !email.trim().isEmpty()) ? email.trim().toLowerCase() : null);
            ps.setString(4, (phone != null && !phone.trim().isEmpty()) ? phone.trim() : null);
            ps.setString(5, (role != null && !role.trim().isEmpty()) ? role : "student");

            int paramIndex = 6;
            if (newHashedPassword != null && !newHashedPassword.trim().isEmpty()) {
                ps.setString(paramIndex++, newHashedPassword);
            }
            ps.setInt(paramIndex, userId);

            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Lỗi updateUserByAdmin: ", ex);
        }
        return false;
    }

    @Override
    public boolean deleteUser(int userId) {
        String sql = "DELETE FROM users WHERE id = ?";
        try (Connection con = MySQLDriver.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Lỗi deleteUser: ", ex);
        }
        return false;
    }

    @Override
    public boolean updateUserStatus(int userId, int status) {
        String sql = "UPDATE users SET status = ? WHERE id = ?";
        try (Connection con = MySQLDriver.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, status);
            ps.setInt(2, userId);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Lỗi updateUserStatus: ", ex);
        }
        return false;
    }

    @Override
    public int countUsersByRole(String role) {
        String sql = (role == null || "ALL".equalsIgnoreCase(role))
                   ? "SELECT COUNT(*) FROM users"
                   : "SELECT COUNT(*) FROM users WHERE role = ?";
        try (Connection con = MySQLDriver.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            if (role != null && !"ALL".equalsIgnoreCase(role)) {
                ps.setString(1, role);
            }
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Lỗi countUsersByRole: ", ex);
        }
        return 0;
    }
}
