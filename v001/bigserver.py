import socket
import threading

# Server configuration
host = '192.168.0.100' # use your pc ip 
port = 9999 # add anynumber like this {example: '0000','8888','8080'.......}

# Function to read and send an HTML file
def send_html_file(client, file_path):
    try:
        with open(file_path, 'rb') as file:
            data = file.read()
            response = b"HTTP/1.1 200 OK\r\n"
            response += b"Content-Type: text/html\r\n"
            response += b"Content-Length: " + str(len(data)).encode() + b"\r\n"
            response += b"\r\n"  # End of headers
            response += data
            client.send(response)
    except FileNotFoundError:
        error_message = "File not found"
        response = b"HTTP/1.1 404 Not Found\r\n"
        response += b"Content-Type: text/plain\r\n"
        response += b"Content-Length: " + str(len(error_message)).encode() + b"\r\n"
        response += b"\r\n"  # End of headers
        response += error_message.encode()
        client.send(response)

def send_image_file(client, file_path):
    try:
        with open(file_path, 'rb') as file:
            data = file.read()
            response = b"HTTP/1.1 200 OK\r\n"
            response += b"Content-Type: image/jpeg\r\n"  # Change the content type as needed
            response += b"Content-Length: " + str(len(data)).encode() + b"\r\n"
            response += b"\r\n"  # End of headers
            response += data
            client.send(response)
    except FileNotFoundError:
        error_message = "Image file not found"
        response = b"HTTP/1.1 404 Not Found\r\n"
        response += b"Content-Type: text/plain\r\n"
        response += b"Content-Length: " + str(len(error_message)).encode() + b"\r\n"
        response += b"\r\n"  # End of headers
        response += error_message.encode()
        client.send(response)

# Function to send a CSS file
def send_css_file(client, file_path):
    try:
        with open(file_path, 'rb') as file:
            data = file.read()
            response = b"HTTP/1.1 200 OK\r\n"
            response += b"Content-Type: text/css\r\n"
            response += b"Content-Length: " + str(len(data)).encode() + b"\r\n"
            response += b"\r\n"  # End of headers
            response += data
            client.send(response)
    except FileNotFoundError:
        error_message = "CSS file not found"
        response = b"HTTP/1.1 404 Not Found\r\n"
        response += b"Content-Type: text/plain\r\n"
        response += b"Content-Length: " + str(len(error_message)).encode() + b"\r\n"
        response += b"\r\n"  # End of headers
        response += error_message.encode()
        client.send(response)

# Modify the handle_client function to check for image and CSS file requests
def handle_client(client, address):
    print(f"Connected to {address}")

    
    file_path = 'v001\\index.html'  # Replace with your HTML file's path
    file_image = 'v001\\nikhil.jpg'  # Replace with the path to your image file
    file_css = 'v001\\styles.css'  # Replace with the path to your CSS file
    
    # Receive the HTTP request from the client
    request = client.recv(1024).decode()

    if request.startswith("GET /nikhil"):
        send_image_file(client, file_image)
    elif request.startswith("GET /styles.css"):
        send_css_file(client, file_css)
    elif request.startswith("GET"):
        send_html_file(client, file_path)
    else:
        error_message = "Bad Request"
        response = b"HTTP/1.1 400 Bad Request\r\n"
        response += b"Content-Type: text/plain\r\n"
        response += b"Content-Length: " + str(len(error_message)).encode() + b"\r\n"
        response += b"\r\n"  # End of headers
        response += error_message.encode()
        client.send(response)

    client.close()
    print(f"Connection with {address} closed")
# Create a socket
server = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

# Bind the socket to the address and port
server.bind((host, port))

# Listen for incoming connections
server.listen()

print(f"Server is listening on {host}:{port}")

while True:
    # Accept incoming connections
    client, address = server.accept()

    # Create a new thread to handle the client connection
    client_thread = threading.Thread(target=handle_client, args=(client, address))
    client_thread.start()


# comments add by me but modifiy by CHATGPT


# Created by nikhil karmkar
# discord UID is "karnikhil"
# Twitter UID is "karnikhil"
