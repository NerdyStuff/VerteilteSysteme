import java.net.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

// TODO: Add Messages to chat history!!

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

                            if (dataPackage.getSyncFlag() == 1) {
                                // registration
                                if (users.get(dataPackage.getUsername()) != null) {
                                    this.sendServerSocketData(clientSocket, new DataPackage(-20, "Failed"));
                                } else {
                                    this.sendServerSocketData(clientSocket, new DataPackage(21, "Ready"));

                                    // wait for commit
                                    boolean gotResponse = false;
                                    while (!gotResponse) {

                                        DataPackage responseData = null;

                                        if ((responseData = getSocketData(clientSocket)) != null) {

                                            if (responseData.getFlag() == 22) {
                                                // Send commit to server
                                                // push user to own HashMap
                                                this.sendServerSocketData(clientSocket, new DataPackage(23, "Acknowledge"));

                                                users.put(((User) dataPackage.getObject()).getUsername(), ((User) dataPackage.getObject()));

                                                gotResponse = true;
                                            } else {
                                                // Send Failed and do not save user
                                                this.sendServerSocketData(clientSocket, new DataPackage(-20, "Failed"));

                                                gotResponse = true;
                                            }
                                        }
                                    }
                                }
                            } else if (dataPackage.getSyncFlag() == 2) {
                                // incomming message sync

                                // TODO: Can Errors occur????

                                this.sendServerSocketData(clientSocket, new DataPackage(21, "Ready"));

                                // wait for commit
                                boolean gotResponse = false;
                                while (!gotResponse) {

                                    DataPackage responseData = null;

                                    if ((responseData = getSocketData(clientSocket)) != null) {

                                        if (responseData.getFlag() == 22) {
                                            // Send commit to server
                                            // push user to own HashMap
                                            this.sendServerSocketData(clientSocket, new DataPackage(23, "Acknowledge"));

                                            // update user message
                                            users.replace(((User) dataPackage.getObject()).getUsername(), ((User) dataPackage.getObject()));

                                            gotResponse = true;
                                        } else {
                                            // Send Failed and do not update user
                                            this.sendServerSocketData(clientSocket, new DataPackage(-20, "Failed"));

                                            gotResponse = true;
                                        }
                                    }
                                }
                            } else if (dataPackage.getSyncFlag() == 3) {
                                // update request sync

                                // TODO: Can Errors occur????

                                this.sendServerSocketData(clientSocket, new DataPackage(21, "Ready"));

                                // wait for commit
                                boolean gotResponse = false;
                                while (!gotResponse) {

                                    DataPackage responseData = null;

                                    if ((responseData = getSocketData(clientSocket)) != null) {

                                        if (responseData.getFlag() == 22) {
                                            // Send commit to server
                                            // push user to own HashMap
                                            this.sendServerSocketData(clientSocket, new DataPackage(23, "Acknowledge"));

                                            // update user message
                                            users.replace(((User) dataPackage.getObject()).getUsername(), ((User) dataPackage.getObject()));

                                            gotResponse = true;
                                        } else {
                                            // Send Failed and do not update user
                                            this.sendServerSocketData(clientSocket, new DataPackage(-20, "Failed"));

                                            gotResponse = true;
                                        }
                                    }
                                }
                            }
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

            User user = new User(dataPackage.getUsername(), dataPackage.getPassword());

            Socket syncSocket = null;

            try {
                syncSocket = new Socket(syncHostname, serverPort);
            } catch (UnknownHostException e) {
                System.out.println("Error: Host not known");

                registrationReturnList.add(new DataPackage(-1, "Registration failed"));
                return registrationReturnList;

            } catch (IOException e) {
                System.out.println("Error: IOException");

                registrationReturnList.add(new DataPackage(-1, "Registration failed"));
                return registrationReturnList;
            }

            if (syncSocket != null) {
                System.out.println("Socket to sync Server established");

                DataPackage sendSyncData = new DataPackage(20, 1, user);

                this.sendServerSocketData(syncSocket, sendSyncData);

                boolean gotResponse = false;
                int retryCounter = 0;

                while (!gotResponse && retryCounter < 10) {

                    DataPackage responseData = null;

                    if ((responseData = getSocketData(syncSocket)) != null) {

                        if (responseData.getFlag() == 21) {
                            // Send commit to server

                            this.sendServerSocketData(syncSocket, new DataPackage(22, "Commit"));

                            gotResponse = true;
                        } else if (responseData.getFlag() == -20) {

                            // Send Abort and do not save user
                            this.sendServerSocketData(syncSocket, new DataPackage(-21, "Abort"));

                            registrationReturnList.add(new DataPackage(-1, "Registration failed"));
                            gotResponse = false;

                            break;
                        } else {
                            // If an error occured
                            // Send Abort and do not save user
                            this.sendServerSocketData(syncSocket, new DataPackage(-21, "Abort"));

                            registrationReturnList.add(new DataPackage(-1, "Registration failed"));
                            gotResponse = false;
                            break;
                        }
                    }

                    // Wait a second
                    try {
                        System.out.println("Retrying...");
                        TimeUnit.SECONDS.sleep(1);
                        retryCounter ++;
                    } catch (InterruptedException interruptedExeption) {
                        System.out.println("Error: Could not sleep for one second...");
                    }
                }

                // timeout
                if(retryCounter >= 10 || !gotResponse) {
                    this.sendServerSocketData(syncSocket, new DataPackage(-21, "Abort"));

                    registrationReturnList.add(new DataPackage(-1, "Registration failed"));
                }

                gotResponse = false;
                retryCounter = 0;
                while (!gotResponse && retryCounter < 10) {

                    DataPackage responseData = null;

                    if ((responseData = getSocketData(syncSocket)) != null) {

                        if (responseData.getFlag() == 23) {
                            users.put(user.getUsername(), user);

                            gotResponse = true;
                        } else {

                            registrationReturnList.add(new DataPackage(-1, "Registration failed"));
                            gotResponse = false;

                            break;
                        }
                    }

                    // Wait a second
                    try {
                        System.out.println("Retrying...");
                        TimeUnit.SECONDS.sleep(1);
                        retryCounter ++;
                    } catch (InterruptedException interruptedExeption) {
                        System.out.println("Error: Could not sleep for one second...");
                    }
                }

                if(retryCounter >= 10 || !gotResponse) {
                    this.sendServerSocketData(syncSocket, new DataPackage(-21, "Abort"));

                    registrationReturnList.add(new DataPackage(-1, "Registration failed"));

                    this.closeSocket(syncSocket);

                    return registrationReturnList;
                }

            } else {
                //Error handling
                this.closeSocket(syncSocket);

                registrationReturnList.add(new DataPackage(-1, "Registration failed"));
                return registrationReturnList;
            }

            // If user does not exist, return success
            registrationReturnList.add(new DataPackage(6, "Registration successfull"));
            return registrationReturnList;
        }
    }

    private List<DataPackage> handleIncomingMessage(DataPackage dataPackage) {
        List<DataPackage> messageReturnList = new LinkedList<DataPackage>();

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

                    // Sync between servers
                    Socket syncSocket = null;

                    try {
                        syncSocket = new Socket(syncHostname, serverPort);
                    } catch (UnknownHostException e) {
                        System.out.println("Error: Host not known");

                        // Error
                        messageReturnList.add(new DataPackage(-4, "Error: Host unknown"));

                        return messageReturnList;

                    } catch (IOException e) {
                        System.out.println("Error: IOException");

                        // Error
                        messageReturnList.add(new DataPackage(-4, "Error: IOException"));

                        return messageReturnList;
                    }

                    if (syncSocket != null) {
                        System.out.println("Socket to sync Server established");


                        User updateUser = users.get(dataPackage.getReceiver());

                        updateUser.addMessage(new Message(dataPackage.getUsername(),
                                dataPackage.getMessage(),
                                dataPackage.getTimestamp()));

                        DataPackage sendSyncData = new DataPackage(20, 1, updateUser); // Request Commit

                        this.sendServerSocketData(syncSocket, sendSyncData);

                        boolean gotResponse = false;
                        int retryCounter = 0;

                        while (!gotResponse && retryCounter < 10) {

                            DataPackage responseData = null;

                            if ((responseData = getSocketData(syncSocket)) != null) {

                                if (responseData.getFlag() == 21) {
                                    // Send commit to server

                                    this.sendServerSocketData(syncSocket, new DataPackage(22, "Commit"));

                                    gotResponse = true;
                                } else if (responseData.getFlag() == -20) {

                                    // Send Abort and do not save user
                                    this.sendServerSocketData(syncSocket, new DataPackage(-21, "Abort"));

                                    // Error
                                    messageReturnList.add(new DataPackage(-4, "Error: Abort"));

                                    gotResponse = false;
                                    break;
                                } else {

                                    // If an error occured
                                    // Send Abort and do not update messages
                                    this.sendServerSocketData(syncSocket, new DataPackage(-21, "Abort"));

                                    messageReturnList.add(new DataPackage(-4, "Error: Abort"));

                                    gotResponse = false;
                                    break;
                                }
                            }

                            // Wait a second
                            try {
                                System.out.println("Retrying...");
                                TimeUnit.SECONDS.sleep(1);
                                retryCounter ++;
                            } catch (InterruptedException interruptedExeption) {
                                System.out.println("Error: Could not sleep for one second...");
                            }
                        }

                        // timeout
                        if(retryCounter >= 10 || !gotResponse) {
                            this.sendServerSocketData(syncSocket, new DataPackage(-21, "Abort"));

                            messageReturnList.add(new DataPackage(-4, "Error: Abort"));
                        }

                        gotResponse = false;
                        retryCounter = 0;
                        while (!gotResponse && retryCounter < 10) {

                            DataPackage responseData = null;

                            if ((responseData = getSocketData(syncSocket)) != null) {

                                if (responseData.getFlag() == 23) {

                                    // replace user
                                    users.replace(updateUser.getUsername(), updateUser);

                                    gotResponse = true;
                                } else {

                                    // Error
                                    messageReturnList.add(new DataPackage(-4, "Error: Did not get acknowledge from other server"));

                                    gotResponse = false;
                                    break;
                                }
                            }

                            // Wait a second
                            try {
                                System.out.println("Retrying...");
                                TimeUnit.SECONDS.sleep(1);
                                retryCounter ++;
                            } catch (InterruptedException interruptedExeption) {
                                System.out.println("Error: Could not sleep for one second...");
                            }
                        }

                        if(retryCounter >= 10 || !gotResponse) {
                            this.sendServerSocketData(syncSocket, new DataPackage(-21, "Abort"));

                            messageReturnList.add(new DataPackage(-4, "Error: Did not get acknowledge from other server"));

                            this.closeSocket(syncSocket);

                            return messageReturnList;
                        }
                    }

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
        List<Message> messageList = new LinkedList<Message>();

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

                messageList.add(message);

                updateReturnList.add(
                        new DataPackage(4, message.getSender(),
                                message.getText(),
                                message.getTimestamp()));
            }

            // Sync between servers
            Socket syncSocket = null;

            try {
                syncSocket = new Socket(syncHostname, serverPort);
            } catch (UnknownHostException e) {
                System.out.println("Error: Host not known");

                // Error
                updateReturnList.add(new DataPackage(-4, "Error: Host unknown"));

                return updateReturnList;

            } catch (IOException e) {
                System.out.println("Error: IOException");

                // Error
                updateReturnList.add(new DataPackage(-4, "Error: IOException"));

                return updateReturnList;
            }

            if (syncSocket != null) {
                System.out.println("Socket to sync Server established");

                DataPackage sendSyncData = new DataPackage(20, 1, user); // Request Commit

                this.sendServerSocketData(syncSocket, sendSyncData);

                boolean gotResponse = false;
                int retryCounter = 0;
                while (!gotResponse && retryCounter < 10) {

                    DataPackage responseData = null;

                    if ((responseData = getSocketData(syncSocket)) != null) {

                        if (responseData.getFlag() == 21) {
                            // Send commit to server

                            this.sendServerSocketData(syncSocket, new DataPackage(22, "Commit"));

                            gotResponse = true;
                        } else if (responseData.getFlag() == -20) {

                            // Send Abort and do not save user
                            this.sendServerSocketData(syncSocket, new DataPackage(-21, "Abort"));

                            // Error
                            updateReturnList.add(new DataPackage(-4, "Error: Abort"));
                            gotResponse = false;
                            break;
                        } else {
                            // If an error occured
                            // Send Abort and do not save user
                            this.sendServerSocketData(syncSocket, new DataPackage(-21, "Abort"));

                            updateReturnList.add(new DataPackage(-4, "Error: Abort"));
                            gotResponse = false;
                            break;
                        }
                    }

                    // Wait a second
                    try {
                        System.out.println("Retrying...");
                        TimeUnit.SECONDS.sleep(1);
                        retryCounter ++;
                    } catch (InterruptedException interruptedExeption) {
                        System.out.println("Error: Could not sleep for one second...");
                    }
                }

                // timeout
                if(retryCounter >= 10 || !gotResponse) {
                    this.sendServerSocketData(syncSocket, new DataPackage(-21, "Abort"));

                    updateReturnList.add(new DataPackage(-4, "Error: Abort"));
                }

                gotResponse = false;
                retryCounter = 0;
                while (!gotResponse && retryCounter < 10) {

                    DataPackage responseData = null;

                    if ((responseData = getSocketData(syncSocket)) != null) {

                        if (responseData.getFlag() == 23) {

                            //Messages are removed from user, so nothing to do here;
                            // Rollback if error occured see code below

                            gotResponse = true;
                        } else {
                            // Error

                            // Rollback
                            while (!messageList.isEmpty()) {
                                Message tempMessage = ((LinkedList<Message>) messageList).remove();

                                user.addMessage(tempMessage);
                            }

                            updateReturnList.add(new DataPackage(-4, "Error: Did not get acknowledge from other server"));

                            gotResponse = false;

                            break;
                        }
                    }

                    // Wait a second
                    try {
                        System.out.println("Retrying...");
                        TimeUnit.SECONDS.sleep(1);
                        retryCounter ++;
                    } catch (InterruptedException interruptedExeption) {
                        System.out.println("Error: Could not sleep for one second...");
                    }
                }

                if(retryCounter >= 10 || !gotResponse) {
                    this.sendServerSocketData(syncSocket, new DataPackage(-21, "Abort"));

                    updateReturnList.add(new DataPackage(-4, "Error: Did not get acknowledge from other server"));

                    this.closeSocket(syncSocket);
                }
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

    private void closeSocket(Socket socket) {
        try {
            socket.close();
        } catch (IOException e) {
            System.out.println("Error: Could not close connection");
            e.printStackTrace();
        }
    }
}