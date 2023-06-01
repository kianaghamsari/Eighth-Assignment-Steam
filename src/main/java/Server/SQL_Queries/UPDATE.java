package Server.SQL_Queries;

import java.sql.SQLException;
import java.sql.Statement;

import Server.Connect;

public class UPDATE {
    public static void downloadGame(String account_id, String game_id){
        String UPDATE_DOWNLOADS_QUERY = "UPDATE Downloads SET download_count = download_count + 1 WHERE account_id = '" + account_id + "' AND game_id = " + game_id;
        try {
            Statement statement = Connect.connect().createStatement();
            statement.executeUpdate(UPDATE_DOWNLOADS_QUERY);
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }
}
