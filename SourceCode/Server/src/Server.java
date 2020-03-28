import java.net.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class Server {

    // server port and socket
    private int serverPort = 1337;
    private ServerSocket serverSocket;

    private String syncHostname = "192.168.188.31";

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

    public void acceptClientConnections() {

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

                    // Corrupted dataPackage received
                    if (dataPackage == null) {
                        // Send fail to client
                        List<DataPackage> failedReturnList = new LinkedList<DataPackage>();

                        // Add return-object to return list
                        failedReturnList.add(new DataPackage(-4, "Error!"));

                        // Send list to user
                        this.sendSocketData(clientSocket, failedReturnList);

                        clientSocket.close();
                    } else {

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
                        } else if (dataPackage.getFlag() == 8) {
                            // User requests chathistory e.g. after login

                            this.sendSocketData(clientSocket, this.handleChatHistoryRequest(dataPackage));
                        } else if (dataPackage.getFlag() == 20) {
                            // Other Server sent commit request (prepare for commit)

                            System.out.println("Got flag 20");

                        } else if (dataPackage.getFlag() == 21) {
                            // Other Server sent ready message
                        } else if (dataPackage.getFlag() == 22) {
                            // Other Server sent commit
                        } else if (dataPackage.getFlag() == 23) {
                            // Other Server sent acknowledge
                        } else {

                            // Send fail to client
                            List<DataPackage> failedReturnList = new LinkedList<DataPackage>();

                            // Add return-object to return list
                            failedReturnList.add(new DataPackage(-4, "Error!"));

                            // Send list to user
                            this.sendSocketData(clientSocket, failedReturnList);

                            clientSocket.close(); // Close client socket
                        }
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
        boolean errorOccured = false;

        try {
            InputStream inputStream = clientSocket.getInputStream();
            // create a DataInputStream so we can read data from it.

            ObjectInputStream objectInputStream = null;
            Object object = null;

            try {
                objectInputStream = new ObjectInputStream(inputStream);
            } catch (StreamCorruptedException e) {
                errorOccured = true;
                System.out.println("Error: StreamCorruptedException");
            }

            if (!errorOccured) {
                try {
                    object = objectInputStream.readObject();
                } catch (NullPointerException e) {
                    errorOccured = true;
                    System.out.println("Error: NullPointerException");
                }
            }

            if (!errorOccured && object != null) {
                if (object instanceof DataPackage) {
                    data = (DataPackage) object;
                } else {
                    throw new WrongDataPackageException("Error: Not a valid DataPackage-Object");
                }
            } else {
                System.out.println("Error: Object is null");
            }

        } catch (StreamCorruptedException e) {
            System.out.println("Error: Did not receive an object...");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Error: Couldn't get data from socket...");
            e.printStackTrace();
        } catch (WrongDataPackageException e) {
            System.out.println("Error: Received data is not a DataPackage-object...");
            e.getMessage();
        } catch (ClassNotFoundException e) {
            System.out.println("Error: Class not found...");
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("Error: An error occured...");
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

    private void sendServerSocketData(Socket syncServerSocket, DataPackage dataPackage) {
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(syncServerSocket.getOutputStream());
            objectOutputStream.writeObject(dataPackage);
        } catch (IOException ioException) {
            System.out.println("Error: Couldn't send data to other server");
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

            users.put(dataPackage.getUsername(),
                    new User(dataPackage.getUsername(), dataPackage.getPassword()));
            //TODO: Synchronise between servers

            Socket syncSocket = null;

            try {
                syncSocket = new Socket(syncHostname, serverPort);
            } catch (UnknownHostException e) {
                System.out.println("Error: Host not known");

                return null;

            } catch (IOException e) {
                System.out.println("Error: IOException");

                return null;
            }

            if(syncSocket != null) {
                System.out.println("Socket to sync Server established");

                DataPackage sendSyncData = new DataPackage(20, new Test());

                this.sendServerSocketData(syncSocket, sendSyncData);

                //////////////////////////////////////////
                //while ()

                //wait for response
                /////////////////////////////////////////
            } else {
                //Error handling
            }

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

                return updateReturnList;
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

    private List<DataPackage> handleChatHistoryRequest(DataPackage dataPackage) {

        List<DataPackage> updateReturnList = new LinkedList<DataPackage>();

        // Authentificate User
        User user = authenticateUser(dataPackage.getUsername(), dataPackage.getPassword());

        if (user == null) {
            updateReturnList.add(new DataPackage(-2, "Username or password is incorrect!"));

            return updateReturnList;
        } else {

            if (user.getChathistory() == null) {
                // User has no chat history

                updateReturnList.add(new DataPackage(-6, "No chat history found / error"));

                return updateReturnList;
            } else {

                Iterator iterator = user.getChathistory().iterator();
                while (iterator.hasNext()) {
                    DataPackage tempData = (DataPackage) iterator.next();
                    updateReturnList.add(
                            new DataPackage(9,
                                    tempData.getUsername(),
                                    tempData.getMessage(),
                                    tempData.getTimestamp()));
                }
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