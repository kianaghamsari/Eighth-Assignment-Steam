package Client;

import Shared.Request;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.*;
import java.net.Socket;
import java.time.LocalDate;
import java.util.Scanner;
import java.util.UUID;

public class ClientMain {
    static Scanner input = new Scanner(System.in);
    private Socket clientSocket;
    private Scanner in;

    public ClientMain(Socket clientSocket) {
        this.clientSocket = clientSocket;
        try {
            this.in = new Scanner(clientSocket.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public static void main(String[] args) {
        try {
            Socket clientSocket = new Socket("localhost", 8080);
            ClientMain clientMain = new ClientMain(clientSocket);
            clientMain.runMenu();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void runMenu() throws InterruptedException, IOException {
        System.out.println("1: Sign Up\n2L Sign in\n3: QUIT");
        int optionMenu = input.nextInt();
        input.nextLine();
        switch (optionMenu) {
            case 1: {
                System.out.println("ENTER Username:");
                String username = input.nextLine();
                System.out.println("ENTER Password:");
                String password = input.nextLine();
                System.out.println("ENTER Date of your birth(yyyy-mm-dd):");
                String date_of_birth = input.nextLine();

                JsonObject jsonRequest = new JsonObject();
                jsonRequest.addProperty("requestType", "SIGN UP");
                jsonRequest.addProperty("username", username);
                jsonRequest.addProperty("password", password);
                jsonRequest.addProperty("date_of_birth", date_of_birth);

                Request.signUpRequest(clientSocket, jsonRequest);

                String response = in.nextLine();
                JsonObject jsonResponse = new Gson().fromJson(response, JsonObject.class);
                System.out.println("RECEIVING: " + jsonResponse.get("response").getAsString());
            }
            case 2: {
                System.out.println("ENTER username:");
                String username = input.nextLine();
                System.out.println("ENTER password:");
                String password = input.nextLine();
                JsonObject jsonRequest = new JsonObject();
                jsonRequest.addProperty("requestType", "SIGN IN");
                jsonRequest.addProperty("username", username);
                jsonRequest.addProperty("password", password);
                Request.signInRequest(clientSocket, jsonRequest);

                String response = in.nextLine();
                JsonObject jsonResponse = new Gson().fromJson(response, JsonObject.class);
                String validation = jsonResponse.get("response").getAsString();
                System.out.println("RECEIVING: " + jsonResponse.get("response").getAsString());

                if (validation.equals("VALID LOGIN")) {
                    JsonObject jsonAccount = jsonResponse.get("Account").getAsJsonObject();
                    User account = new User(UUID.fromString(jsonAccount.get("account_id").getAsString()),
                            jsonAccount.get("username").getAsString(),
                            jsonAccount.get("password").getAsString(),
                            LocalDate.parse(jsonAccount.get("date_of_birth").getAsString()));
                    userPage(account);
                }
            }
            case 3: {
                JsonObject jsonRequest = new JsonObject();
                jsonRequest.addProperty("requestType", "QUIT");
                Request.signUpRequest(clientSocket, jsonRequest);
                String response = in.nextLine();
                JsonObject jsonResponse = new Gson().fromJson(response, JsonObject.class);
                System.out.println("RECEIVING: " + jsonResponse.get("response").getAsString());
                if (jsonResponse.get("response").getAsString().equals("EXITED SUCCESSFULLY")){
                    return;
                }
            }
        }

        runMenu();
    }
    public void userPage(User user) throws InterruptedException {
        System.out.println("1: SHOW ALL AVAILABLE GAMES\n2: SHOW AN SPECIFIC GAME\n3: MANAGE DOWNLOAD\n\t-SHOW DOWNLOADED GAMES\n\t-DOWNLOAD A GAME");
        int optionMenu = input.nextInt();
        input.nextLine();
        switch (optionMenu) {
            case 1: {

                JsonObject jsonRequest = new JsonObject();
                jsonRequest.addProperty("requestType", "SHOW ALL AVAILABLE GAMES");
                Request.showGamesRequest(clientSocket, jsonRequest);
                String response = in.nextLine();
                JsonObject jsonResponse = new Gson().fromJson(response, JsonObject.class);
                System.out.println("RECEIVING :" + jsonResponse.get("response").getAsString());

                showGameTable(jsonResponse);
            }
            case 2: {


                System.out.println("ENTER game_id");
                String game_id = input.nextLine();

                JsonObject jsonRequest = new JsonObject();
                jsonRequest.addProperty("requestType", "SHOW AN SPECIFIC GAME");
                jsonRequest.addProperty("game_id", game_id);

                Request.showAGameRequest(clientSocket, jsonRequest);


                String response = in.nextLine();
                JsonObject jsonResponse = new Gson().fromJson(response, JsonObject.class);
                System.out.println("RECEIVING :" + jsonResponse.get("response").getAsString());

                showGameTable(jsonResponse);
            }
            case 3: {
                System.out.println("1: SHOW DOWNLOADED GAMES\n2: DOWNLOAD A GAME");
                optionMenu = input.nextInt();
                input.nextLine();
                switch (optionMenu) {
                    case 1: {

                        JsonObject jsonRequest = new JsonObject();
                        jsonRequest.addProperty("requestType", "SHOW DOWNLOADED GAMES");
                        jsonRequest.addProperty("account_id", user.getAccount_id().toString());
                        Request.showDownloadedGamesRequest(clientSocket, jsonRequest);
                        String response = in.nextLine();
                        JsonObject jsonResponse = new Gson().fromJson(response, JsonObject.class);
                        System.out.println("RECEIVING :" + jsonResponse.get("response").getAsString());

                        showDownloadsTable(jsonResponse);
                    }
                    case 2: {

                        System.out.println("ENTER game_id:");
                        String game_id = input.nextLine();

                        JsonObject jsonRequest = new JsonObject();
                        jsonRequest.addProperty("requestType", "DOWNLOAD GAME");
                        jsonRequest.addProperty("account_id", user.getAccount_id().toString());
                        jsonRequest.addProperty("game_id", game_id);
                        Request.downloadGameRequest(clientSocket, jsonRequest);
                        String response = in.nextLine();
                        System.out.println(response);
                        JsonObject jsonResponse = new Gson().fromJson(response, JsonObject.class);
                        System.out.println("RECEIVING :" + jsonResponse.get("response").getAsString());

                        try {
                            receivePNG(game_id);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        }
        userPage(user);
    }

    public static void showGameTable(JsonObject table) {
        int rowCount = table.get("rowCount").getAsInt();
        int columnCount = table.get("columnCount").getAsInt();

        System.out.println("Games Table:");
        System.out.printf("%-50s%-50s%-50s%-50s%-50s%-50s%-50s%-50s%-50s%-50s", "|game_id|", "|title|", "|developer|", "|genre|", "|price|", "|release_year|", "|controller_support|", "|reviews|", "|size|", "|file_path|");

        for (int i = 1; i <= rowCount; i++) {
            JsonArray row = table.get("row" + i).getAsJsonArray();
            System.out.println();

            for (int j = 0; j < columnCount; j++) {
                System.out.printf("%-50s", "|" + row.get(j) + "|");
            }
            System.out.println();
        }
    }

    public static void showDownloadsTable(JsonObject table) {
        int rowCount = table.get("rowCount").getAsInt();
        int columnCount = table.get("columnCount").getAsInt();

        System.out.println("Downloads Table:");
        System.out.printf("%-50s%-50s%-50s", "|account_id|", "|game_id|", "|download_count|");

        for (int i = 1; i <= rowCount; i++) {
            JsonArray row = table.get("row" + i).getAsJsonArray();
            System.out.println();

            for (int j = 0; j < columnCount; j++) {
                System.out.printf("%-50s", "|" + row.get(j) + "|");
            }
            System.out.println();
        }
    }

    private void receivePNG(String game_id) throws Exception {
        FileOutputStream fileOutputStream = new FileOutputStream("src\\main\\java\\Client\\Downloads\\" + game_id + ".png");
        InputStream inputStream = clientSocket.getInputStream();

        DataInputStream dataInputStream = new DataInputStream(inputStream);
        long fileSize = dataInputStream.readLong();

        byte[] buffer = new byte[4096];
        int bytesRead;
        long totalBytesRead = 0;
        while (totalBytesRead < fileSize && (bytesRead = inputStream.read(buffer)) != -1) {
            fileOutputStream.write(buffer, 0, bytesRead);
            totalBytesRead += bytesRead;
        }
        fileOutputStream.flush();
        fileOutputStream.close();
    }
}