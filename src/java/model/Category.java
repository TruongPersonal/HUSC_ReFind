package model;

import java.sql.ResultSet;
import java.sql.SQLException;
import data.utils.HtmlEscaper;

public class Category {
    private int id;
    private String name;

    public Category() {
    }

    public Category(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Category(ResultSet rs) throws SQLException {
        this.id = rs.getInt("id");
        this.name = rs.getString("name");
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return HtmlEscaper.escape(name);
    }

    public String getRawName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
