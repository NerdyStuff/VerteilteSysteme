import java.net.*;
import java.io.*;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

/**
 * Server
 */
public class Server {

    private static int serverPort = 1337;
    private static ServerSocket serverSocket;


    private static User[] users = new User[2];

    public static void main(String[] args) {

        boolean userAuthenticated = false;
        int userPositionInArray = 0;

        users[0] = new User("peter123", "sicher4711");
        users[1] = new User("klaus55", "superTollesPw");

        // open server socket
        startServerSocket(serverPort);

        // wait for clients
        while (true) {
            Socket clientSocket = null;

            try {
                clientSocket = serverSocket.accept();

                sendSocketData(clientSocket, "DATEN");

                if (clientSocket.isConnected()) {
                    System.out.println("Success: Client connected to server!");
                    System.out.println("Client: " + clientSocket.toString() + " connected to server.");


                    sendSocketData(clientSocket, "SEDENDKJHCLDXCH");

                    String socketDataString = getSocketData(clientSocket); // Get data from socket

                    // Analyse Data
                    Message dataPakage = null;

                    try {
                        dataPakage = new Message(socketDataString);
                    } catch (WrongMessageInput wrongMessageInput) {
                        System.out.println("Error while processing Message");
                        wrongMessageInput.printStackTrace();
                        clientSocket.close();
                    }

                    sendSocketData(clientSocket, "LOLOLO");

                    for (int i = 0; i < users.length; i++) {

                        if (dataPakage.getSender().equals(users[i].getUsername())) {
                            if (dataPakage.getPassword().equals(users[i].getPassword())) {
                                System.out.println("User authenticated successfully");
                                userAuthenticated = true;
                                userPositionInArray = i;
                            }
                        }
                    }

                    // If user was not successfully authentificated close connection
                    if (!userAuthenticated) {

                        System.out.println("Closing connection!");
                        clientSocket.close();
                    }


                    for (int i = 0; i < users.length; i++) {

                        if (dataPakage.getReceiver().equals(users[i].getUsername())) {
                            users[i].addMessage(dataPakage.getMessage());
                            System.out.println(users[i].removeMessage());
                        }
                    }

                    sendSocketData(clientSocket, "DATEN ENDE");


                    // Send queued data to client
                    while (!users[userPositionInArray].hasNoMessages()) {
                        sendSocketData(clientSocket, users[userPositionInArray].removeMessage());
                    }
                }

            } catch (IOException ioException) {
                System.out.println("Error: Client could not connect...");
                ioException.printStackTrace();
            } finally {
                // disconnect client
                if (clientSocket != null) {
                    try {
                        clientSocket.close();
                    } catch (IOException ioException) {
                        System.out.println("Error: Socket couldn't be closed...");
                        ioException.printStackTrace();
                    }
                }
            }
        }
    }

    private static void startServerSocket(int serverPort) {

        // establish server socket
        while (serverSocket == null) {
            try {
                serverSocket = new ServerSocket(serverPort);
            } catch (IOException e) {
                System.out.println("Error: Socket creation failed... Retrying...");
                e.printStackTrace();


                // Sleep one second
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException interruptedExeption) {
                    System.out.println("Error: Could not sleep for one second...");
                    interruptedExeption.printStackTrace();
                }
            }
        }
    }

    private static String getSocketData(Socket clientSocket) {

        String data = "";

        try {
            Scanner scanner = new Scanner(new BufferedInputStream (clientSocket.getInputStream()));

            // TODO read whole output!
            data = scanner.nextLine();

        } catch (IOException ioException) {
            System.out.println("Error: Couldn't get data from socket...");
            ioException.printStackTrace();
        }



        return data;
    }

    private static void sendSocketData(Socket clientSocket, String data) {
        try {
            PrintWriter printWriter = new PrintWriter(clientSocket.getOutputStream(), true);

            printWriter.println(data);
        } catch (IOException ioException) {
            System.out.println("Error: Couldn't send data to client");
            ioException.printStackTrace();
        }
    }
}
