import java.io.*;
import java.net.*;

public class Client {
    public static void main(String[] args) {
        // Define the server's IP address and port
        String serverIP = "192.168.1.5";
        int serverPort = 8080;

        try {
            // Create a socket and connect to the server
            Socket socket = new Socket(serverIP, serverPort);

            // Create input and output streams for communication
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            // Send a message to the server
            String message = "Hello, Server!";
            out.println(message);

            // Read and print the server's response
            String serverResponse = in.readLine();
            System.out.println("Server response: \n" + serverResponse);

            // Close the socket
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
