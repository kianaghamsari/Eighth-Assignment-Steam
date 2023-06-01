package Server;

import Client.User;
import Server.SQL_Queries.INSERT;
import Server.SQL_Queries.SELECT;
import Server.SQL_Queries.UPDATE;
import Shared.Response;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.json.JSONObject;

import java.io.*;
import java.net.Socket;
import java.time.LocalDate;
import java.util.Scanner;

public class Services implements Runnable {
    private Socket serverSocket;
    private Scanner in;

    public Services(Socket serverSocket) {
        this.serverSocket = serverSocket;
    }

    @Override
    public void run() {
        try {
            try {
                in = new Scanner(serverSocket.getInputStream());
                doService();
            } finally {
                in.close();
            }
        } catch (IOException ioException){
            ioException.printStackTrace();
        } finally {
            System.out.println("CLIENT " + serverSocket.getRemoteSocketAddress() + " HAS BEEN DISCONNECTED");
        }
    }

    public void doService() {
        while (true) {
            String jsonString = in.nextLine();
            System.out.println("RECEIVING " + jsonString);

            JsonObject jsonRequest = new Gson().fromJson(jsonString, JsonObject.class);
            String requestType = jsonRequest.get("requestType").getAsString();
            System.out.println("requesttype :" + requestType);
            if (requestType.equals("QUIT")) {
                Response.exitResponse(serverSocket);
                return;
            } else {
                executeRequest(jsonRequest);
            }
        }
    }

    public void executeRequest(JsonObject jsonRequest) {
        String requestType = jsonRequest.get("requestType").getAsString();

        switch (requestType) {
            case "SIGN UP" -> {
                    new User(jsonRequest.get("username").getAsString(), jsonRequest.get("password").getAsString(), LocalDate.parse(jsonRequest.get("date_of_birth").getAsString()));
                    Response.signUpResponse(serverSocket, false);
            }
            case "SIGN IN" -> {
                String username = jsonRequest.get("username").getAsString();
                String password = jsonRequest.get("password").getAsString();
                User account = SELECT.authenticateUser(username, password);
                if (account != null) {
                    JSONObject jsonAccount = new JSONObject();
                    jsonAccount.put("account_id", account.getAccount_id());
                    jsonAccount.put("username", account.getUsername());
                    jsonAccount.put("password", account.getPassword());
                    jsonAccount.put("date_of_birth", account.getDate_of_birth());
                    Response.signInResponse(serverSocket, true, jsonAccount);
                } else {
                    Response.signInResponse(serverSocket, false, null);
                }
            }
            case "SHOW ALL AVAILABLE GAMES" -> {
                JsonObject jsonResponse = SELECT.SELECTGames();
                Response.showGamesResponse(serverSocket, jsonResponse);
            }
            case "SHOW AN SPECIFIC GAME" -> {
                String game_id = jsonRequest.get("game_id").getAsString();
                JsonObject jsonResponse = SELECT.SELECTaGame(game_id);
                if (jsonResponse != null){
                    Response.showAGameResponse(serverSocket, true,jsonResponse);
                }
                else {
                    Response.showAGameResponse(serverSocket, false, jsonResponse);
                }
            }
            case "SHOW DOWNLOADED GAMES" -> {
                String account_id = jsonRequest.get("account_id").getAsString();
                JsonObject jsonResponse = SELECT.SELECTDownloadedGames(account_id);
                if (jsonResponse != null){
                    Response.showDownloadedGamesResponse(serverSocket, true, jsonResponse);
                }
                else {
                    Response.showDownloadedGamesResponse(serverSocket, false, jsonResponse);
                }
            }
            case "DOWNLOAD GAME" -> {
                String game_id = jsonRequest.get("game_id").getAsString();
                String account_id = jsonRequest.get("account_id").getAsString();
                String file_path = SELECT.SELECTPath(game_id);
                if (file_path != null){
                    if (SELECT.SELECTDownloadCounter(account_id, game_id)){
                        System.out.println("has downloaded");
                        UPDATE.downloadGame(account_id, game_id);
                    } else {
                        INSERT.INSERTToDownloads(account_id, game_id);
                        System.out.println("has not downloaded");
                    }
                    Response.downloadGameResponse(serverSocket, game_id);
                    try {
                        sendPNG(file_path);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
                else {
                    Response.downloadGameResponse(serverSocket, game_id);
                }
            }
        }
    }

    private void sendPNG(String path) throws Exception {
        File file = new File(path);
        FileInputStream fileInputStream = new FileInputStream(file);
        OutputStream outputStream = serverSocket.getOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
        long fileSize = file.length();
        dataOutputStream.writeLong(fileSize);
        byte[] buffer = new byte[4096];
        int bytesRead;
        while ((bytesRead = fileInputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }
        dataOutputStream.flush();
        outputStream.flush();
        fileInputStream.close();
    }
}