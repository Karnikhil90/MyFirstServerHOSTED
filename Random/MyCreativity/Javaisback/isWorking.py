import socket

try:
    # Client setup
    client = socket.socket()
    server_address = ('192.168.1.5', 8080)
    client.connect(server_address)
    print("Connecting to server")

    # Receive data from the server
    message = client.recv(1024).decode('utf-8')
    print("Received message from the server: \n", message)

    # Send a message to the server
    to_server = "Hello, this is an auto-generated message"
    client.send(to_server.encode('utf-8'))
    
except Exception as e:
    print("An error occurred:", str(e))
finally:
    client.close()
