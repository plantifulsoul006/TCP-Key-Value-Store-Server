import java.io.*;
import java.net.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.Map;

public class TCPServer {
    private static final int PORT = 2006;
    private static final int THREAD_POOL_SIZE = 10; 

    private static Map<String, String> keyValueMap = new ConcurrentHashMap<>();
    private static ExecutorService executorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE);

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server started on port " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected: " + clientSocket);
                executorService.execute(new ClientHandler(clientSocket));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            executorService.shutdown();
        }
    }

    static class ClientHandler implements Runnable {
        private final Socket clientSocket;

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        @Override
        public void run() {
            try (
                DataInputStream inputStream = new DataInputStream(clientSocket.getInputStream());
                DataOutputStream outputStream = new DataOutputStream(clientSocket.getOutputStream())
            ) {
                String inputLine;
                while ((inputLine = inputStream.readUTF()) != null) {
                    System.out.println("Received from client: " + inputLine);
                    try {
                        String response = handleCommand(inputLine);
                        outputStream.writeUTF(response);
                        System.out.println("Responded to client: " + response);
                    } catch (Exception e) {
                        outputStream.writeUTF("Error: " + e.getMessage());
                        System.out.println("Error responding to client: " + e.getMessage());
                    }
                }
            } catch (EOFException e) {
                System.out.println("Client disconnected");
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        private String handleCommand(String inputLine) throws Exception {
            String[] tokens = inputLine.split("\\s+", 3);
            String command = tokens[0];

            switch (command) {
                case "GET":
                case "PUT":
                case "DELETE":
                case "KEYS":
                case "TABLE":
                case "QUIT":
                    return executeCommand(command, tokens);
                default:
                    throw new Exception("Invalid command. Please try again.");
            }
        }

        private String executeCommand(String command, String[] tokens) throws Exception {
            switch (command) {
                case "GET":
                    if (tokens.length != 2) {
                        throw new Exception("Invalid GET command format");
                    }
                    String key = tokens[1];
                    String value = keyValueMap.get(key);
                    if (value == null) {
                        throw new Exception("Key not found");
                    }
                    return value;
                case "PUT":
                    if (tokens.length != 3) {
                        throw new Exception("Invalid PUT command format");
                    }
                    String putKey = tokens[1];
                    String putValue = tokens[2];
                    keyValueMap.put(putKey, putValue);
                    return "Key-value pair added/updated";
                case "DELETE":
                    if (tokens.length != 2) {
                        throw new Exception("Invalid DELETE command format");
                    }
                    String deleteKey = tokens[1];
                    if (keyValueMap.remove(deleteKey) == null) {
                        throw new Exception("Key not found");
                    }
                    return "Key deleted";
                case "KEYS":
                    if (keyValueMap.isEmpty()) {
                        return "No keys available";
                    } else {
                        StringBuilder keysList = new StringBuilder("Keys:\n");
                        for (String kkey : keyValueMap.keySet()) {
                            keysList.append(kkey).append("\n");
                        }
                        return keysList.toString();
                    }
                case "TABLE":
                    if (keyValueMap.isEmpty()) {
                        return "No keys or values available";
                    } else {
                        StringBuilder entries = new StringBuilder();
                        entries.append(String.format("%n"));
                        entries.append(String.format("%n"));
                        entries.append(String.format("+--------+------------+%n"));
                        entries.append(String.format("| Key    | Value      |%n"));
                        entries.append(String.format("+--------+------------+%n"));
                        keyValueMap.forEach((k, v) -> entries.append(String.format("| %-6s | %-10s |%n", k, v)));
                        entries.append(String.format("+--------+------------+%n"));
                        return entries.toString();
                    }
                case "QUIT":
                    return "Goodbye!";
                default:
                    throw new Exception("Invalid command");
            }
        }
    }
}
