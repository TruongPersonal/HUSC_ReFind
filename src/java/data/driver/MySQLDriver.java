package data.driver;

import data.utils.Constants;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MySQLDriver {
    public static Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(Constants.DB_URL, Constants.USER, Constants.PASS);
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(MySQLDriver.class.getName()).log(Level.SEVERE, "Lỗi kết nối CSDL MySQL: ", ex);
        }
        return null;
    }
}
