package Server;

import java.net.ServerSocket;
import java.net.Socket;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import Server.SQL_Queries.INSERT;

public class ServerMain {
    public static void main(String[] args) {
        try {
            int port = 8000;
            ServerSocket serverSocket = new ServerSocket(port);
            while (true) {
                Socket socket = serverSocket.accept();
                Services services = new Services(socket);
                Thread thread = new Thread(services);
                thread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void loadTextFiles(String directoryPath) {
        File directory = new File(directoryPath);
        File[] fileList = directory.listFiles();
        List<File> textFiles = new ArrayList<>();
        for (File file : fileList) {
          if (file.isFile() && file.getName().endsWith(".txt")) {
            textFiles.add(file);
          }
        } 
        if (textFiles.size() == 0) {
            System.out.println("No text files found in directory");
            return;
        }

        for (File file : textFiles) {
            INSERT.INSERTFileToDB(textFiles,file.getPath());
            System.out.println("Successfully inserted " + file.getName() + " into database");
        }
    }
}
