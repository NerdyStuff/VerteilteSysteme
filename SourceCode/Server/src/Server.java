import java.net.*;
import java.io.*;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

/**
 * Important! THIS WAS ONLY A SHORT TEST
 * Server
 */
public class Server {

    private static int serverPort = 1337;
    private static ServerSocket serverSocket;

    public static void main(String[] args) {

        // open server socket
        startServerSocket(serverPort);

        // wait for clients
        while (true)
        {
            Socket clientSocket = null;

            try {
                clientSocket = serverSocket.accept();

                if (clientSocket.isConnected())
                {
                    System.out.println("Success: Client connected to server!");
                    System.out.println("Client: " + clientSocket.toString() +  " connected to server.");

                    String socketDataString = getSocketData(clientSocket); // Get data from socket

                    // Analyse Data
                    Message dataPakage = null;

                    System.out.println("Data: "+ socketDataString);

                    sendSocketData(clientSocket, "TEST");
                }
            }
            catch (IOException ioException){
                System.out.println("Error: Client could not connect...");
            }
            finally {
                // disconnect client
                if (clientSocket != null){
                    try {
                        clientSocket.close();
                    }
                    catch (IOException ioException){
                        System.out.println("Error: Socket couldn't be closed...");
                    }
                }
            }
        }
    }

    private static void startServerSocket(int serverPort){

        // establish server socket
        while (serverSocket == null){
            try {
                serverSocket = new ServerSocket(serverPort);
            }
            catch (IOException e){
                System.out.println("Error: Socket creation failed... Retrying...");


                // Sleep one second
                try {
                    TimeUnit.SECONDS.sleep(1);
                }
                catch (InterruptedException interruptedExeption){
                 System.out.println("Error: Could not sleep for one second...");
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
        }

        return data;
    }

    private static void sendSocketData(Socket clientSocket, String data) {
        try {
            PrintWriter printWriter = new PrintWriter(clientSocket.getOutputStream());

            printWriter.println(data);
        } catch (IOException ioException) {
            System.out.println("Error: Couldn't send data to client");
        }
    }

    private static Message splitToken(String data)
    {
        return null;
    }
}
