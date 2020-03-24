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

    public static void main(String[] args) {

        // open server socket
        startServerSocket(serverPort);

        // wait for clients
        while (true) {
            Socket clientSocket = null;

            try {
                clientSocket = serverSocket.accept();

                if (clientSocket.isConnected()) {
                    System.out.println("Success: Client connected to server!");
                    System.out.println("Client: " + clientSocket.toString() + " connected to server.");

                    String socketDataString = getSocketData(clientSocket); // Get data from socket

                    // Analyse Data
                    Message dataPakage = null;

                    System.out.println("Data: " + socketDataString);

                    sendSocketData(clientSocket, "TEST");
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
            Scanner scanner = new Scanner(clientSocket.getInputStream());

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
            PrintWriter printWriter = new PrintWriter(clientSocket.getOutputStream());

            printWriter.println(data);
        } catch (IOException ioException) {
            System.out.println("Error: Couldn't send data to client");
            ioException.printStackTrace();
        }
    }
}
