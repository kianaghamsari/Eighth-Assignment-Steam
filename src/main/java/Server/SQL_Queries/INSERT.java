package Server.SQL_Queries;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import Client.User;
import Server.Connect;

public class INSERT {

    public static Connection connection;
    private static String INSERT_GAME_QUERY = "INSERT INTO Games (game_id, title, developer, genre, price, release_year, controller_support, reviews, size, file_path) VALUES (?,?,?,?,?,?,?,?,?,?)";
    private static String INSERT_DOWNLOAD_QUERY = "INSERT INTO Downloads (account_id, game_id, download_count) VALUES (?,?,?)";
    private static String INSERT_ACCOUNT_QUERY = "INSERT INTO accounts (username, password, date_of_birth) VALUES (?,?,?)";
 
    public static void INSERTFileToDB(List<File> files, String path) {
        try (Connection connection = Connect.connect()) {
            for (File file : files) {
                List<String> array = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);
                String game_id = array.get(0);
                String title = array.get(1);
                String developer = array.get(2);
                String genre = array.get(3);
                double price = Double.parseDouble(array.get(4));
                int release_year = Integer.parseInt(array.get(5));
                boolean controller_support = Boolean.parseBoolean(array.get(6));
                int reviews = Integer.parseInt(array.get(7));
                int size = Integer.parseInt(array.get(8));
                path = path.replaceAll("txt", "png");

                PreparedStatement statement = connection.prepareStatement(INSERT_GAME_QUERY);
                statement.setString(1, game_id);
                statement.setString(2, title);
                statement.setString(3, developer);
                statement.setString(4, genre);
                statement.setDouble(5, price);
                statement.setInt(6, release_year);
                statement.setBoolean(7, controller_support);
                statement.setInt(8, reviews);
                statement.setInt(9, size);
                statement.setString(10, path);
                statement.executeUpdate();
            }
            System.out.println("Successfully inserted all text files in " + path + " into database");
        } catch (IOException | SQLException e) {
            throw new RuntimeException("Failed to insert text files to database", e);
        }
    }

    public static void INSERTToDownloads(String account_id, String game_id){
        try {
            PreparedStatement statement = connection.prepareStatement(INSERT_DOWNLOAD_QUERY);
            statement.setString(1, account_id);
            statement.setString(2, game_id);
            statement.setInt(3, 1);
            statement.executeUpdate();
        } catch (SQLException sqlException){
            sqlException.getStackTrace();
        }
    }

    public static void INSERTUserToAccounts(User user) throws SQLException {
        PreparedStatement statement = Connect.connect().prepareStatement(INSERT_ACCOUNT_QUERY);
        statement.setString(1, user.getUsername());
        statement.setString(2, user.getPassword());
        statement.setDate(3, Date.valueOf(user.getDate_of_birth())); 
        statement.executeUpdate();
    }

}
