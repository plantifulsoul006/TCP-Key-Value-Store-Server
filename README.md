# TCP-Key-Value-Store-Server

Project Overview

The TCP Key-Value Store Server is a Java-based server application that provides a simple and reliable distributed key-value store, allowing clients to perform CRUD (Create, Read, Update, Delete) operations. The server communicates with clients over TCP (Transmission Control Protocol), ensuring reliable, ordered, and error-checked delivery of messages. The server can handle multiple client connections concurrently using multithreading, providing scalability and responsiveness.

Key-Value Store Operations

	•	PUT <key> <value>: Adds a key-value pair to the store.
	•	GET <key>: Retrieves the value associated with a given key.
	•	DELETE <key>: Removes a key-value pair from the store.
	•	KEYS: Lists all keys stored in the key-value store.
	•	QUIT: Closes the client connection.

Features

	•	Reliable Communication: Uses TCP to guarantee the reliable, ordered, and error-checked transmission of messages between client and server.
	•	Concurrent Clients: Handles multiple client requests simultaneously using multithreading, ensuring high scalability.
	•	In-Memory Storage: Key-value pairs are stored in memory, enabling fast read and write operations.
	•	Error Handling: The server provides robust error handling for invalid commands, packet size limitations, and unexpected client behavior.
	•	Efficient Data Structures: Utilizes Maps for fast storage and retrieval of key-value pairs, ensuring uniqueness and optimal performance.

TCP Client-Server Communication

	•	The client sends requests to the server over a TCP connection.
	•	The server processes the requests (such as GET, PUT, DELETE) and responds with appropriate results.
	•	TCP ensures that the messages are delivered reliably and in the correct order.

How to Run

	1.	Clone the repository:

git clone https://github.com/your-username/tcp-key-value-store.git


	2.	Navigate to the project directory:

cd tcp-key-value-store


	3.	Compile the server and client classes:

javac TCPServer.java TCPClient.java


	4.	Start the TCP Server:

java TCPServer


	5.	Run the TCP Client:

java TCPClient


	6.	Use the following commands from the client:
	•	PUT <key> <value>: Store a key-value pair.
	•	GET <key>: Retrieve the value for a specific key.
	•	DELETE <key>: Remove a key-value pair.
	•	KEYS: Retrieve all stored keys.
	•	QUIT: Exit the client.

Future Enhancements

	•	Persistent Storage: Add support for saving key-value pairs to a database or a file system.
	•	Security: Implement encryption and authentication for secure communication.
	•	Load Balancing: Distribute client requests across multiple servers to improve performance.

