package edu.seg2105.edu.server.backend;

import java.util.Scanner;

import edu.seg2105.client.common.ChatIF;

/**
 * This class constructs the UI for a chat server, allowing the server admin to send messages.
 * It implements the ChatIF interface to activate the display() method.
 */
public class ServerConsole implements ChatIF {
  
    // Instance of EchoServer
    private EchoServer server;

    // Scanner to read from the console
    private Scanner fromConsole; 

    /**
     * Constructs an instance of the ServerConsole UI.
     *
     * @param server The instance of EchoServer this console is attached to.
     */
    public ServerConsole(EchoServer server) {
        this.server = server;
        fromConsole = new Scanner(System.in); // Initialize Scanner for console input
    }

    /**
     * This method waits for input from the server console. Once received,
     * it sends it to all connected clients and displays it on the server console.
     */
    public void accept() {
        try {
            String message;

            while (true) {
                message = fromConsole.nextLine();
                handleServerMessage(message);
            }
        } catch (Exception ex) {
            System.out.println("Unexpected error while reading from server console!");
        }
    }

    /**
     * Processes a message from the server console.
     * @param message The message to be sent to clients.
     */
    private void handleServerMessage(String message) {
        if (message != null && !message.isEmpty()) {
            if (message.startsWith("#")) {
                // Handle command if the message starts with "#"
                handleCommand(message);
            } else {
                display(message);
                server.sendToAllClients("SERVER MSG> " + message); // Send message to all clients
            }
        }
    }
    
    private void handleCommand(String command) {
        if (command.equals("#quit")) {
        	try {
                server.close();
                System.exit(0);
            } catch (Exception e) {
            	System.out.println("Error quitting server: " + e.getMessage());
            }

        } else if (command.equals("#stop")) {
        	server.stopListening();

        } else if (command.equals("#close")) {
        	try {
                server.close();
            } catch (Exception e) {
            	System.out.println("Error closing server: " + e.getMessage());
            }

        } else if (command.startsWith("#setport ")) {
            if (!server.isListening()) { // Only allow if server is closed
                try {
                    server.setPort(Integer.parseInt(command.substring(9)));
                    System.out.println("Port set to " + Integer.parseInt(command.substring(9)));
                } catch (NumberFormatException e) {
                	System.out.println("Invalid port number: " + command.substring(9));
                }
            } else {
            	System.out.println("Cannot change port while server is running.");
            }

        } else if (command.equals("#start")) {
            if (!server.isListening()) { // Only allow if server is stopped
                try {
                    server.listen();
                } catch (Exception e) {
                	System.out.println("Error starting server: " + e.getMessage());
                }
            } else {
            	System.out.println("Server is already running.");
            }

        } else if (command.equals("#getport")) {
        	System.out.println("Current port: " + server.getPort());

        } else {
        	System.out.println("Invalid Command");
        }
    }

    /**
     * This method overrides the display method in the ChatIF interface. It
     * displays a message prefixed with "SERVER MSG>" on the server console.
     *
     * @param message The string to be displayed.
     */
    @Override
    public void display(String message) {
        System.out.println("SERVER MSG> " + message);
    }
}
