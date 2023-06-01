package Shared;

import com.google.gson.JsonObject;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class Request {


    public static void showGamesRequest(Socket clientSocket, JsonObject jsonRequest) {
        try (PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {
            out.println(jsonRequest);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void showAGameRequest(Socket clientSocket, JsonObject jsonRequest){
        PrintWriter out = null;
        try {
            out = new PrintWriter(clientSocket.getOutputStream());
            out.println(jsonRequest);
            out.flush();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void showDownloadedGamesRequest(Socket clientSocket, JsonObject jsonRequest){
        PrintWriter out = null;
        try {
            out = new PrintWriter(clientSocket.getOutputStream());
            out.println(jsonRequest);
            out.flush();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void downloadGameRequest(Socket clientSocket, JsonObject jsonRequest){
        PrintWriter out = null;
        try {
            out = new PrintWriter(clientSocket.getOutputStream());
            out.println(jsonRequest);
            out.flush();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void signUpRequest(Socket clientSocket, JsonObject jsonRequest) {
        PrintWriter out = null;
        try {
            out = new PrintWriter(clientSocket.getOutputStream());
            out.println(jsonRequest);
            out.flush();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void signInRequest(Socket clientSocket, JsonObject jsonRequest){
        PrintWriter out = null;
        try {
            out = new PrintWriter(clientSocket.getOutputStream());
            out.println(jsonRequest);
            out.flush();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void exitRequest(Socket clientSocket, JsonObject jsonRequest){
        PrintWriter out = null;
        try {
            out = new PrintWriter(clientSocket.getOutputStream());
            out.println(jsonRequest);
            out.flush();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}