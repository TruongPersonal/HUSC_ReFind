package model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import data.utils.HtmlEscaper;

public class Item {
    private int id;
    private String title;
    private int categoryId;
    private int locationId;
    private String description;
    private String image;
    private int status;
    private String adminNote;
    private Integer userId;
    private Timestamp createdAt;

    private String categoryName;
    private String locationName;
    private String authorName;
    private String authorPhone;
    private String authorCode;
    private String authorEmail;
    private String authorRole;

    public Item() {
    }

    public Item(int id, String title, int categoryId, int locationId, String description, String image, int status, String adminNote, Integer userId, Timestamp createdAt) {
        this.id = id;
        this.title = title;
        this.categoryId = categoryId;
        this.locationId = locationId;
        this.description = description;
        this.image = image;
        this.status = status;
        this.adminNote = adminNote;
        this.userId = userId;
        this.createdAt = createdAt;
    }

    public Item(ResultSet rs) throws SQLException {
        this.id = rs.getInt("id");
        this.title = rs.getString("title");
        this.categoryId = rs.getInt("category_id");
        this.locationId = rs.getInt("location_id");
        this.description = rs.getString("description");
        this.image = rs.getString("image");
        this.status = rs.getInt("status");
        this.adminNote = rs.getString("admin_note");

        int uId = rs.getInt("user_id");
        this.userId = rs.wasNull() ? null : uId;

        this.createdAt = rs.getTimestamp("created_at");

        try {
            this.categoryName = rs.getString("category_name");
        } catch (SQLException ignored) {}
        try {
            this.locationName = rs.getString("location_name");
        } catch (SQLException ignored) {}
        try {
            this.authorName = rs.getString("author_name");
        } catch (SQLException ignored) {}
        try {
            this.authorPhone = rs.getString("author_phone");
        } catch (SQLException ignored) {}
        try {
            this.authorCode = rs.getString("author_code");
        } catch (SQLException ignored) {}
        try {
            this.authorEmail = rs.getString("author_email");
        } catch (SQLException ignored) {}
        try {
            this.authorRole = rs.getString("author_role");
        } catch (SQLException ignored) {}
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return HtmlEscaper.escape(title);
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getLocationId() {
        return locationId;
    }

    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }

    public String getDescription() {
        return HtmlEscaper.escape(description);
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getAdminNote() {
        return HtmlEscaper.escape(adminNote);
    }

    public String getRawAdminNote() { return adminNote; }

    public void setAdminNote(String adminNote) {
        this.adminNote = adminNote;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public String getCategoryName() {
        return HtmlEscaper.escape(categoryName);
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getLocationName() {
        return HtmlEscaper.escape(locationName);
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getAuthorName() {
        if (authorName != null && !authorName.trim().isEmpty()) {
            return HtmlEscaper.escape(authorName);
        }
        return isAdminPost() ? "Quản trị viên" : "";
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getAuthorPhone() {
        return HtmlEscaper.escape(authorPhone);
    }

    public void setAuthorPhone(String authorPhone) {
        this.authorPhone = authorPhone;
    }

    public String getAuthorCode() {
        return HtmlEscaper.escape(authorCode);
    }

    public void setAuthorCode(String authorCode) {
        this.authorCode = authorCode;
    }

    public String getAuthorEmail() {
        return HtmlEscaper.escape(authorEmail);
    }

    public void setAuthorEmail(String authorEmail) {
        this.authorEmail = authorEmail;
    }

    public String getAuthorRole() {
        if (authorRole != null && !authorRole.trim().isEmpty()) {
            return authorRole;
        }
        return isAdminPost() ? "admin" : "student";
    }

    public void setAuthorRole(String authorRole) {
        this.authorRole = authorRole;
    }

    public boolean isStudentPost() {
        return this.userId != null && this.userId > 0;
    }

    public boolean isAdminPost() {
        return this.userId == null || this.userId <= 0;
    }

    public boolean isLost() {
        return this.status == 1;
    }

    public boolean isFound() {
        return this.status == 2;
    }

    public String getStatusText() {
        if (status == 0) return "Đã trả đồ";
        if (status == 2) return "Đang tiếp nhận";
        return "Chờ xử lý";
    }

    public String getStatusBadgeClass() {
        if (status == 0) return "badge-success";
        if (status == 2) return "badge-warning";
        return "badge-danger";
    }
}
