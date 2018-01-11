package assist;

import java.sql.*;

public class JdbcTemplate {
    private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://localhost:33060/onlineChat";

    private static final String USER = "root";
    private static final String PASS = "root";

    Connection connection = null;
    Statement statement = null;

    public JdbcTemplate() {
        try {
            Class.forName(JDBC_DRIVER);
            System.out.println("连接数据库中");
            connection = DriverManager.getConnection(DB_URL, USER, PASS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean register(String username, String password) {
        try {
            String sql = "insert into users(username, password) values(?, ?)";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.execute();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean checkHasRegister(String username) {
        try {
            String sql = "select username from users where username=?";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, username);
            ResultSet resultSet = pstmt.executeQuery();
            return resultSet.next();
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }
    }

    public boolean checkUser(String username, String password) {
        try {
            String sql = "select * from users where username = ? and password = ?";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet resultSet = pstmt.executeQuery();
            return resultSet.next();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
