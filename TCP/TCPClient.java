import java.io.*;
import java.net.*;
import java.util.LinkedList;
import java.util.Queue;

public class TCPClient {
    private static final String SERVER_HOST = "127.0.0.1";
    private static final int SERVER_PORT = 2006;

    public static void main(String[] args) {
        try (
            Socket socket = new Socket(SERVER_HOST, SERVER_PORT);
            BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
            DataInputStream inputStream = new DataInputStream(socket.getInputStream());
            DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream())
        ) {
            System.out.println("Connected to server.");
            commandsMenu();

            String inputLine;
            while ((inputLine = userInput.readLine()) != null) {
                String[] tokens = inputLine.split("\\s+", 3);
                String command = tokens[0]; 

                if (isValidCommand(command)) {
                    outputStream.writeUTF(inputLine); 
                    outputStream.flush();
                } else {
                    System.out.println("Invalid command. Please try again.");
                    commandsMenu();
                    continue; 
                }

                String response = inputStream.readUTF(); 
                System.out.println();
                System.out.println("Server response: " + response);

                if ("Goodbye!".equals(response)) {
                    break;
                }
                commandsMenu();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static boolean isValidCommand(String command) {
        String[] validCommands = {"GET", "PUT", "DELETE", "KEYS", "TABLE", "QUIT"};

        for (String validCommand : validCommands) {
            if (validCommand.equals(command.trim())) {
                return true;
            }
        }
        return false;
    }

    private static void commandsMenu() {
        System.out.println();
        System.out.println("Available commands:");
        System.out.println();

        String[] COMMANDS = {"GET <key>", "PUT <key> <value>", "DELETE <key>", "KEYS", "TABLE", "QUIT"};
        Queue<String> commandsQueue = new LinkedList<>();
        for (String command : COMMANDS) {
            commandsQueue.offer(command);
        }

        while (!commandsQueue.isEmpty()) {
            System.out.println(commandsQueue.poll());
        }

        System.out.println();
        System.out.println("Type a command: ");
    }
}