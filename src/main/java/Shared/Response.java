package Shared;

import com.google.gson.JsonObject;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class Response {

    public static void signUpResponse(Socket serverSocket, Boolean bool) {
        try {
            OutputStream outputStream = serverSocket.getOutputStream();
            JSONObject jsonResponse = new JSONObject();
            jsonResponse.put("responseType", "SIGN UP");
            if (!bool) {
                jsonResponse.put("response", "ACCOUNT HAS BEEN SUCCESSFULLY SIGNED UP");
            } else {
                jsonResponse.put("response", "THIS USERNAME ALREADY EXISTS");
            }
            System.out.println("SENDING: " + jsonResponse);
            PrintWriter out = new PrintWriter(outputStream, true);
            out.println(jsonResponse);
        } catch (IOException e) {
            throw new RuntimeException("Failed to send sign up response", e);
        }
    }

    public static void signInResponse(Socket serverSocket, Boolean bool, JSONObject jsonObject){
        PrintWriter out = null;
        try {
            OutputStream outputStream = serverSocket.getOutputStream();
            out = new PrintWriter(outputStream);
            JSONObject jsonResponse = new JSONObject();
            jsonResponse.put("responseType", "SIGN IN");
            if (bool){
                jsonResponse.put("response", "VALID LOGIN");
                jsonResponse.put("Account", jsonObject);
            } else {
                jsonResponse.put("response", "INVALID LOGIN");
            }
            System.out.println("SENDING: " + jsonResponse);
            out.println(jsonResponse);
            out.flush();
        }
        catch (IOException io){
            io.printStackTrace();
        }
    }

    public static void showGamesResponse(Socket serverSocket, JsonObject jsonResponse){
        PrintWriter out = null;
        try {
            OutputStream outputStream = serverSocket.getOutputStream();
            out = new PrintWriter(outputStream);
            jsonResponse.addProperty("response", "SELECTED ALL AVAILABLE Games");
            System.out.println("SENDING: " + jsonResponse);
            out.println(jsonResponse);
            out.flush();
        }
        catch (IOException io){
            io.printStackTrace();
        }
    }

    public static void showAGameResponse(Socket serverSocket, Boolean bool, JsonObject jsonResponse){
        PrintWriter out = null;
        try {
            OutputStream outputStream = serverSocket.getOutputStream();
            out = new PrintWriter(outputStream);
            jsonResponse.addProperty("response", "SELECTED SPECIFIED GAME");
            System.out.println("SENDING: " + jsonResponse);
            out.println(jsonResponse);
            out.flush();
        }
        catch (IOException io){
            io.printStackTrace();
        }
    }

    public static void showDownloadedGamesResponse(Socket serverSocket, Boolean bool, JsonObject jsonResponse){
        PrintWriter out = null;
        try {
            OutputStream outputStream = serverSocket.getOutputStream();
            out = new PrintWriter(outputStream);
            jsonResponse.addProperty("response", "SELECTED DOWNLOADED GAMES");
            System.out.println("SENDING: " + jsonResponse);
            out.println(jsonResponse);
            out.flush();
        }
        catch (IOException io){
            io.printStackTrace();
        }
    }

    public static void downloadGameResponse(Socket serverSocket, String game_id){
        PrintWriter out = null;
        try {
            OutputStream outputStream = serverSocket.getOutputStream();
            out = new PrintWriter(outputStream);
            JsonObject jsonResponse = new JsonObject();
            jsonResponse.addProperty("response", "SELECTED DOWNLOADED GAME");
            jsonResponse.addProperty("game_id", game_id);
            System.out.println("SENDING: " + jsonResponse);
            out.println(jsonResponse);
            out.flush();
        }
        catch (IOException io){
            io.printStackTrace();
        }
    }

    public static void exitResponse(Socket serverSocket){
        PrintWriter out = null;
        try {
            OutputStream outputStream = serverSocket.getOutputStream();
            out = new PrintWriter(outputStream);
            JsonObject jsonResponse = new JsonObject();
            jsonResponse.addProperty("response", "EXITED SUCCESSFULLY");
            System.out.println("SENDING: " + jsonResponse);
            out.println(jsonResponse);
            out.flush();
        }
        catch (IOException io){
            io.printStackTrace();
        }
    }
}
