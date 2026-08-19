package model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import data.utils.HtmlEscaper;

public class User {
    private int id;
    private String code;
    private String name;
    private String email;
    private String phone;
    private String password;
    private String role;
    private int status = 1;
    private Timestamp createdAt;

    public User() {
    }

    public User(int id, String code, String name, String email, String phone, String password, String role, int status, Timestamp createdAt) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.role = role;
        this.status = status;
        this.createdAt = createdAt;
    }

    public User(ResultSet rs) throws SQLException {
        this.id = rs.getInt("id");
        this.code = rs.getString("code");
        this.name = rs.getString("name");
        this.email = rs.getString("email");
        this.phone = rs.getString("phone");
        this.password = rs.getString("password");
        this.role = rs.getString("role");
        try {
            this.status = rs.getInt("status");
        } catch (SQLException e) {
            this.status = 1;
        }
        this.createdAt = rs.getTimestamp("created_at");
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return HtmlEscaper.escape(code);
    }

    public String getRawCode() { return code; }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return HtmlEscaper.escape(name);
    }

    public String getRawName() { return name; }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return HtmlEscaper.escape(email);
    }

    public String getRawEmail() { return email; }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return HtmlEscaper.escape(phone);
    }

    public String getRawPhone() { return phone; }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public boolean isActive() {
        return this.status == 1;
    }

    public boolean isBanned() {
        return this.status == 0;
    }

    public boolean isAdmin() {
        return "admin".equalsIgnoreCase(this.role);
    }
}
