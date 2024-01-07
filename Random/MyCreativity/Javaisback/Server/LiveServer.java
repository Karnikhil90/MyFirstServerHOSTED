package Server;

// Importing
import java.io.*;
import java.net.*;
import java.nio.file.Files;

public class LiveServer {
    // Constant variable for 'ADDRESS'
    private static final String IP = "192.168.1.5";
    private static final short PORT = 8080;
    // Folder Address
    public static final String file_ = "C:\\Users\\USER\\Desktop\\PlayStore\\index.html";
    public static final String file_javascript = "C:\\Users\\USER\\Desktop\\PlayStore\\script.js";
    public static final String file_css = "C:\\Users\\USER\\Desktop\\PlayStore\\styles.css";
    public static final String file_download = "C:\\Users\\USER\\Desktop\\PlayStore\\MusicApp.apk";
    public static final String file_image = "";

    public static void startServer() {
        try {
            ServerSocket SERVER = new ServerSocket(PORT, 0, InetAddress.getByName(IP));

            while (true) {
                Socket CLIENT = SERVER.accept();
                System.out.println("Connected to: " + CLIENT.getInetAddress());
                handleClient(CLIENT);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // SERVER.close();
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
                } else {
                    System.out.println("Request out of list");
                    send501NotImplemented(out);
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
            send404NotFound(out);
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
            send404NotFound(out);
        }
    }

    private static void send404NotFound(OutputStream out) throws IOException {
        String response = "HTTP/1.1 404 Not Found\r\n\r\n";
        out.write(response.getBytes());
    }

    private static void send501NotImplemented(OutputStream out) throws IOException {
        String response = "HTTP/1.1 501 Not Implemented\r\n\r\n";
        out.write(response.getBytes());
    }

    // Driver Code
    public static void main(String[] args) {
        System.out.println("Server is online address is" + IP + ':' + PORT);
        startServer();
    }

}
