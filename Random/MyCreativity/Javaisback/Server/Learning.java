package Server;

import java.io.*;
import java.net.*;

public class Learning {

    private static final String IP = "192.168.1.5";
    private static final short PORT = 8080;

    public static void serverSet() {
        try {
            ServerSocket server = new ServerSocket(PORT, 0, InetAddress.getByName(IP));
            System.out.println("Server is online on " + IP + ":" + PORT);

            while (true) {
                Socket client = server.accept();
                System.out.println("Got a client connection from " + client.getInetAddress());

                try (
                        BufferedReader input = new BufferedReader(new InputStreamReader(client.getInputStream()));
                        PrintWriter output = new PrintWriter(client.getOutputStream(), true);) {

                    String serverMessage = "Server: Welcome To Nikhil's Java Server Testing";
                    output.println(serverMessage);

                    System.out.println("Message is Sent");
                    String clientMessage = input.readLine();

                    if (clientMessage.equalsIgnoreCase("GET / HTTP/1.1"))
                        System.out.println("Got a WEB client Request ");
                    else
                        System.out.println("Client message: " + clientMessage);

                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    client.close();
                }
            }
            // server.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        System.out.println("Starting....");
        serverSet();
    }
}
