package com.jenkin.demo01.demo_bio.client;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Client2 {
    private static final String URL = "127.0.0.1";
    private static final int PORT = 22346;
    private static final ExecutorService executor = Executors.newSingleThreadExecutor();
    public static void main(String[] args) {
        startClient();
    }
    private static void startClient() {
        Socket socket = null;
        Scanner input = new Scanner(System.in);
        String message = null;
        try {
            socket = new Socket(URL,PORT);
            BufferedReader reader =
                    new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer =
                    new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
            writer.println("Client2");
            writer.flush();

            System.out.println(reader.readLine());
            executor.submit(()->{
                String line = null;
                try {
                    while ((line = reader.readLine())!=null) {
                        System.out.println(line);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

            });
            while (!"exit".equals((message=input.nextLine())) ){
                writer.println(String.valueOf(message));
                writer.flush();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
