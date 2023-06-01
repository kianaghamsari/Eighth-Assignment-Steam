package Server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connect {
    public static Connection connect() throws SQLException{
        String url = "jdbc:postgresql://localhost/database";
        Connection connection = null;
        try {
            String user = "postgres";
            String password = "123456";

            connection = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw e;
        }

        return connection;
    }
}
