package Server.SQL_Queries;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import Server.Connect;
import Shared.Game;

public class SELECT {
    public static Connection connection;

    private static String SELECT_GAMES_QUERY = "SELECT * FROM games";
    private static String SELECT_DOWMLOADS_ID_QUERY = "SELECT * FROM games where id=?";
    private static String SELECT_DOWMLOADS_ACCOUNTID_QUERY = "SELECT * FROM Downloads WHERE account_id = ?";
    private static String SELECT_ACCOUNT_QUERY = "SELECT * FROM accounts where username=? AND password=?";
    private static String SELECT_PATH_QUERY= "SELECT file_path FROM Games WHERE game_id = ?";
    private static String SELECT_DOWNLOAD_COUNTS_QUERY = "SELECT download_count FROM Downloads WHERE account_id = ? AND game_id = ?";

    public static List<Game> SELECTGames() throws SQLException {
        PreparedStatement statement = connection.prepareStatement(SELECT_GAMES_QUERY);

        ResultSet rs = statement.executeQuery();

        List<Game> list = new ArrayList<>();

        while (rs.next()){
            Game game = new Game();
            game.setId(rs.getInt("id"));
            game.setTitle(rs.getString("title"));
            game.setDeveloper(rs.getString("developer"));
            game.setGenre(rs.getString("genre"));
            game.setPrice(rs.getInt("price"));
            game.setController_support(rs.getBoolean("controller_support"));
            game.setRelease_year(rs.getInt("release_year"));
            game.setSize(rs.getInt("size"));
            game.setReviews(rs.getInt("reviews"));

            list.add(game);
        }
        return list;
    }

    public static Game SELECTaGame(String id) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(SELECT_DOWMLOADS_ID_QUERY);

        statement.setString(1, id);

        ResultSet rs = statement.executeQuery();



        if(rs.next()){
            Game game = new Game();
            game.setId(rs.getInt("id"));
            game.setTitle(rs.getString("title"));
            game.setDeveloper(rs.getString("developer"));
            game.setGenre(rs.getString("genre"));
            game.setPrice(rs.getInt("price"));
            game.setController_support(rs.getBoolean("controller_support"));
            game.setRelease_year(rs.getInt("release_year"));
            game.setSize(rs.getInt("size"));
            game.setReviews(rs.getInt("reviews"));
            game.setPath_file(rs.getString("file_path"));
            return game;
        }
        return null;
    }

    public static List<Game> SELECTDownloadedGames(String account_id) throws SQLException{
        PreparedStatement statement = connection.prepareStatement(SELECT_DOWMLOADS_ACCOUNTID_QUERY);
        statement.setString(1, account_id);
        ResultSet rs = statement.executeQuery();
        List<Game> list = new ArrayList<>();
        while (rs.next()){
            Game game = new Game();
            game.setId(rs.getInt("id"));
            game.setTitle(rs.getString("title"));
            game.setDeveloper(rs.getString("developer"));
            game.setGenre(rs.getString("genre"));
            game.setPrice(rs.getInt("price"));
            game.setController_support(rs.getBoolean("controller_support"));
            game.setRelease_year(rs.getInt("release_year"));
            game.setSize(rs.getInt("size"));
            game.setReviews(rs.getInt("reviews"));

            list.add(game);
        }
        return list;
    }

    public static boolean authenticateUser(String username, String password) throws SQLException {
        PreparedStatement statement = Connect.connect().prepareStatement(SELECT_ACCOUNT_QUERY);
        statement.setString(1, username);
        statement.setString(2, password);
    
        ResultSet rs = statement.executeQuery();
    
        return rs.getRow() == 1;
    }

    public static String SELECTPath(String game_id){
        try {
            PreparedStatement statement = Connect.connect().prepareStatement(SELECT_PATH_QUERY);
            statement.setString(1, game_id);
            ResultSet rs = statement.executeQuery();
            if (rs.next()){
                return rs.getString("file_path");
            }
        } catch (SQLException e){
            e.getStackTrace();
        }
        return null;
    }

    public static boolean SELECTDownloadCounter(String account_id, String game_id){
        try {
            PreparedStatement statement = Connect.connect().prepareStatement(SELECT_DOWNLOAD_COUNTS_QUERY);
            statement.setString(1, account_id);
            statement.setString(2, game_id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()){
                return true;
            }
        } catch (SQLException e){
            e.getStackTrace();
        }
        return false;
    }

}
