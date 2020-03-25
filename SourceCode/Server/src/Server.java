import java.net.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class Server {

    // server port and socket
    private int serverPort = 1337;
    private ServerSocket serverSocket;

    // Hashmap to store Users
    private HashMap<String, User> users;

    public Server() {

        users = new HashMap<String, User>();

        // wait till a server socket is established
        while (serverSocket == null) {
            try {
                serverSocket = new ServerSocket(serverPort);
            } catch (IOException e) {
                System.out.println("Error: Socket creation failed... Retrying...");

                // Sleep one second
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException interruptedExeption) {
                    System.out.println("Error: Could not sleep for one second...");
                }
            }
        }
    }

    private void acceptClientConnections() {

        Socket clientSocket = null;

        // Do forever....
        while (true) {

            try {
                // Accept client connections
                clientSocket = serverSocket.accept();

                if (clientSocket.isConnected()) {
                    // Client connected to server
                    System.out.println("Success: Client connected to server!");
                    System.out.println("Client: " + clientSocket.toString() + " connected to server.");

                    // Get DataPackage-object from Socket
                    DataPackage dataPackage = getSocketData(clientSocket);

                    if (dataPackage.getFlag() == 1) {

                        // Client sent registration:
                        // Send return-list to user
                        this.sendSocketData(clientSocket, this.handleRegistration(dataPackage));
                    } else if (dataPackage.getFlag() == 2) {

                        // Client sent message:
                        // Send return-list to user
                        this.sendSocketData(clientSocket, this.handleIncomingMessage(dataPackage));
                    } else if (dataPackage.getFlag() == 3) {

                        // Client sent Update-Request:
                        // Send return-list to user
                        this.sendSocketData(clientSocket, this.handleUpdateRequest(dataPackage));
                    } else {

                        // Send fail to client
                        List<DataPackage> failedReturnList = new LinkedList<DataPackage>();

                        // Add return-object to return list
                        failedReturnList.add(new DataPackage(-4, "Error!"));

                        // Send list to user
                        this.sendSocketData(clientSocket, failedReturnList);
                    }
                }

            } catch (IOException ioException) {
                // Error occured
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

    private DataPackage getSocketData(Socket clientSocket) {
        DataPackage data = null;

        try {
            InputStream inputStream = clientSocket.getInputStream();
            // create a DataInputStream so we can read data from it.
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);

            Object object = objectInputStream.readObject();
            if (object instanceof DataPackage) {
                data = (DataPackage) object;
            } else {
                WrongDataPackageException wrongDataPackageException = new WrongDataPackageException("Error: Not a valid DataPackage-Object");
                throw wrongDataPackageException;
            }
        } catch (IOException ioException) {
            System.out.println("Error: Couldn't get data from socket...");
            ioException.printStackTrace();
        } catch (WrongDataPackageException e) {
            System.out.println("Error: Received data is not a DataPackage-object...");
        } catch (ClassNotFoundException e) {
            System.out.println("Error: Class not found...");
            e.printStackTrace();
        }

        return data;
    }

    private void sendSocketData(Socket clientSocket, List<DataPackage> dataPackageList) {
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());
            objectOutputStream.writeObject(dataPackageList);
        } catch (IOException ioException) {
            System.out.println("Error: Couldn't send Message list to client");
            ioException.printStackTrace();
        }
    }

    private List<DataPackage> handleRegistration(DataPackage dataPackage) {

        List<DataPackage> registrationReturnList = new LinkedList<DataPackage>();

        // Check if user already exsists
        if (users.get(dataPackage.getUsername()) != null) {
            // If user exists, return fail
            registrationReturnList.add(new DataPackage(-1, "Registration failed: User already exists!"));

            return registrationReturnList;
        } else {
            //TODO: Synchronise between servers

            // If user does not exist, return success
            registrationReturnList.add(new DataPackage(6, "Registration successfull"));
            return registrationReturnList;
        }
    }

    private List<DataPackage> handleIncomingMessage(DataPackage dataPackage) {
        List<DataPackage> messageReturnList = new LinkedList<DataPackage>();

        // TODO: Synchronise between servers
        // Authentificate User
        User user = authenticateUser(dataPackage.getUsername(), dataPackage.getPassword());

        if (user == null) {
            messageReturnList.add(new DataPackage(-2, "Username or password is incorrect!"));

            return messageReturnList;
        } else {

            // Check if Receiver is user self
            if (dataPackage.getReceiver().equals(user.getUsername())) {
                messageReturnList.add(new DataPackage(-5, "Sender can not be the receiver!"));

                return messageReturnList;
            } else {
                // Check if receiver is a user of the system
                if (users.get(dataPackage.getReceiver()) != null) {

                    // Add received message to message-queue of receiver
                    users.get(dataPackage.getReceiver())
                            .addMessage(new Message(dataPackage.getUsername(),
                                            dataPackage.getMessage(),
                                            dataPackage.getTimestamp()));
                    messageReturnList.add(new DataPackage(7, "Message accepted"));
                    return messageReturnList;
                } else {
                    // Receiver is not a user of the system
                    messageReturnList.add(new DataPackage(-3, "Receiver was not found!"));

                    return messageReturnList;
                }
            }
        }
    }

    private List<DataPackage> handleUpdateRequest(DataPackage dataPackage) {
        List<DataPackage> updateReturnList = new LinkedList<DataPackage>();

        // TODO: Synchronise between servers

        // Authentificate User
        User user = authenticateUser(dataPackage.getUsername(), dataPackage.getPassword());

        if (user == null) {
            updateReturnList.add(new DataPackage(-2, "Username or password is incorrect!"));

            return updateReturnList;
        } else {

            if (user.hasNoMessages()) {
                // Check if message-queue is empty

                updateReturnList.add(new DataPackage(5, "No new messages"));

                return  updateReturnList;
            }

            while (!user.hasNoMessages()) {
                Message message = user.removeMessage();

                updateReturnList.add(
                        new DataPackage(4, message.getSender(),
                                        message.getText(),
                                        message.getTimestamp()));
            }

            return updateReturnList;
        }
    }

    private User authenticateUser(String username, String password) {

        User user = users.get(username);

        if (user != null) {
            // Username exists
            if (user.getPassword().equals(password)) {
                // Authentification successfull

                return user;
            }
        }

        // No user found or password is not correct
        return null;
    }
}