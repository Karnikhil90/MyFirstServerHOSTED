import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;

public class SimpleHttpServer {
    public static final String file_ = "C:\\Users\\USER\\Desktop\\MyCreativity\\PlayStore\\index.html";
    public static final String file_javascript = "C:\\Users\\USER\\Desktop\\MyCreativity\\\\PlayStore\\script.js";
    public static final String file_css = "C:\\Users\\USER\\Desktop\\MyCreativity\\PlayStore\\styles.css";
    public static final String file_download = "C:\\Users\\USER\\Desktop\\MyCreativity\\PlayStore\\MusicApp.apk";

    public static final String file_image = "";

    public static void main(String[] args) {
        String host = "192.168.1.5";
        int port = 8080;

        try (ServerSocket serverSocket = new ServerSocket(port, 0, InetAddress.getByName(host))) {
            System.out.println("Server is online on port " + host + ":" + port);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                Thread clientThread = new Thread(() -> handleClient(clientSocket));
                clientThread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleClient(Socket clientSocket) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                OutputStream out = clientSocket.getOutputStream()) {

            String request = in.readLine();
            if (request != null) {
                if (request.startsWith("GET /download")) {
                    sendFileResponse(out, file_download, "application/vnd.android.package-archive");
                } else if (request.startsWith("GET /styles.css")) {
                    sendFileResponse(out, file_css, "text/css");
                } else if (request.startsWith("GET /scripts.js")) {
                    sendFileResponse(out, file_javascript, "application/javascript");
                } else if (request.startsWith("GET /images")) {
                    sendImageResponse(out, file_image);
                } else if (request.startsWith("GET /")) {
                    sendFileResponse(out, file_, "text/html");
                    System.out.println("Sent: main file");
                } else {
                    sendFileResponse(out, file_, "text/html");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void sendFileResponse(OutputStream out, String filePath, String contentType) throws IOException {
        File file = new File(filePath);
        if (file.exists()) {
            byte[] fileData = Files.readAllBytes(file.toPath());
            out.write(("HTTP/1.1 200 OK\r\n" +
                    "Content-Type: " + contentType + "\r\n" +
                    "Content-Length: " + fileData.length + "\r\n" +
                    "\r\n").getBytes());
            out.write(fileData);
        } else {
            sendNotFoundResponse(out);
        }
    }

    private static void sendImageResponse(OutputStream out, String filePath) throws IOException {
        File file = new File(filePath);
        if (file.exists()) {
            byte[] fileData = Files.readAllBytes(file.toPath());
            out.write(("HTTP/1.1 200 OK\r\n" +
                    "Content-Type: image/jpeg\r\n" +
                    "Content-Length: " + fileData.length + "\r\n" +
                    "\r\n").getBytes());
            out.write(fileData);
        } else {
            sendNotFoundResponse(out);
        }
    }

    private static void sendNotFoundResponse(OutputStream out) throws IOException {
        String error = "File not found";
        out.write(("HTTP/1.1 404 Not Found\r\n" +
                "Content-Type: text/plain\r\n" +
                "Content-Length: " + error.length() + "\r\n" +
                "\r\n" + error).getBytes());
    }
}
