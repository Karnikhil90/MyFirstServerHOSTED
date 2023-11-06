import socket
import threading
import os

# Server configuration
host = '192.168.1.17'
port = 8080
served_directory = 'C:\\Users\\USER\\Desktop\\Learning_JavaScript\\firstDay'

class LiveServer:

    def __init__(self, host, port, served_directory):
        self.host = host
        self.port = port
        self.served_directory = served_directory

    def start(self):
        server = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        server.bind((self.host, self.port))
        server.listen()
        print(f"Server is online = '{self.host}:{self.port}'")

        while True:
            client, address = server.accept()
            client_thread = threading.Thread(target=self.handle_client, args=(client, address))
            client_thread.start()

    def handle_client(self, client, address):
        print("\n==========================================================================\n")
        print(f"Connected to {address}")

        request = client.recv(1024).decode()

        if request.startswith("GET /"):
            requested_file = request.split()[1].strip('/')

            if not requested_file:
                file_path = os.path.join(self.served_directory, 'index.html')
                self.send_file(client, file_path)
            else:
                file_path = os.path.join(self.served_directory, requested_file)
                self.send_file(client, file_path)
        else:
            error_message = "Bad Request"
            response = b"HTTP/1.1 400 Bad Request\r\n"
            response += b"Content-Type: text/plain\r\n"
            response += b"Content-Length: " + str(len(error_message)).encode() + b"\r\n"
            response += b"\r\n"
            response += error_message.encode()
            client.send(response)

        user_agent = self.extract_user_agent(request)
        print(f"Connected to {address} from user agent: {user_agent}")

        client.close()
        print(f"Connection with {address} closed")
        print("\n==========================================================================\n")

    def extract_user_agent(self, request):
        # Split the request into headers and content using a double newline as the separator
        parts = request.split('\n\n', 1)
        headers = parts[0]
        if len(parts) > 1:
            content = parts[1]
        else:
            content = ""

        for header in headers.split('\n'):
            if header.startswith('User-Agent:'):
                return header.split(':', 1)[1].strip()
        return 'Unknown'

    def get_content_type(self, file_extension):
        if file_extension == ".html":
            return "text/html"
        elif file_extension == ".css":
            return "text/css"
        elif file_extension == ".js":
            return "application/javascript"
        elif file_extension == ".apk":
            return "application/vnd.android.package-archive"
        else:
            return "application/octet-stream"

    def send_file(self, client, file_path):
        try:
            with open(file_path, 'rb') as file:
                data = file.read()
                file_extension = os.path.splitext(file_path)[1].lower()
                content_type = self.get_content_type(file_extension)
                response = f"HTTP/1.1 200 OK\r\n"
                response += f"Content-Type: {content_type}\r\n"
                response += f"Content-Length: {len(data)}\r\n"
                response += "\r\n"
                client.send(response.encode())
                client.send(data)
        except FileNotFoundError:
            error_message = "File not found"
            response = b"HTTP/1.1 404 Not Found\r\n"
            response += b"Content-Type: text/plain\r\n"
            response += b"Content-Length: " + str(len(error_message)).encode() + b"\r\n"
            response += b"\r\n"
            response += error_message.encode()
            client.send(response)
            print("=============================\n File Not Found \n==============================")

if __name__ == "__main__":
    server = LiveServer(host, port, served_directory)
    server.start()
