import javax.print.DocFlavor;
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class Client {

    private HashMap<Integer, Host> hosts;

    private String username, password;

    public Client(String username, String password) {
        this.username = username;
        this.password = password;

        hosts = new HashMap<Integer, Host>();
        hosts.put(0, new Host("localhost", 1337));
        hosts.put(1, new Host("192.168.188.31", 1337));
    }

    public void addHost(String hostname, int port) {
        hosts.put(hosts.size(), new Host(hostname, port));
    }

    public String login() {
        Host host = selectHost();
        Socket socket = null;
        String returnString = "";

        try {
            socket = new Socket(host.getHostname(), host.getPort());
        } catch (UnknownHostException e) {
            System.out.println("Error: Host not known");

            return "Error: Host not known";

        } catch (IOException e) {
            System.out.println("Error: IOException");

            return "Error: IOException";
        }

        DataPackage dataPackage = new DataPackage(10, username, password);
        int returnValue = sendDataPackage(socket, dataPackage);

        int retryCounter = 0;

        while (returnValue == -1 && retryCounter < 10) {
            // retry if error occured

            // Wait a second
            this.waitMillis(1000);
            retryCounter++;

            returnValue = sendDataPackage(socket, dataPackage);
        }

        if (retryCounter >= 10) {
            // Close connection
            this.closeSocket(socket);
            return "Error: Could not send message";
        }


        // TODO: Retry + Error Handling
        List<DataPackage> serverResponse = getDataPackageList(socket);

        if (serverResponse == null) {
            // Error
            // Close connection
            this.closeSocket(socket);
            return "Error: Could not login";
        } else {
            if (!serverResponse.isEmpty()) {
                DataPackage responsePackage = serverResponse.remove(0);

                int responseFlag = responsePackage.getFlag();

                switch (responseFlag) {
                    case -2:
                        System.out.println(responseFlag);
                        returnString = "Wrong username or password";
                        break;
                    case -7:
                        System.out.println(responseFlag);
                        returnString = "Login failed";
                        break;
                    case 11:
                        System.out.println(responseFlag);
                        returnString = "Login successfull";
                        break;
                    default:
                        System.out.println(responseFlag);
                        returnString = "An error occured";
                }

            } else {
                // Error
                // Close connection
                this.closeSocket(socket);
                return "Error: Server response was empty";
            }
        }

        // Close connection
        this.closeSocket(socket);

        return returnString;
    }

    public List<Message> getChatHistory(String chatPartner) {

        Host host = selectHost();
        Socket socket = null;
        List<Message> messagesList = new LinkedList<Message>();

        try {
            socket = new Socket(host.getHostname(), host.getPort());
        } catch (UnknownHostException e) {
            System.out.println("Error: Host not known");

            return null;

        } catch (IOException e) {
            System.out.println("Error: IOException");

            return null;
        }

        DataPackage dataPackage = new DataPackage(8, username, password);
        int returnValue = sendDataPackage(socket, dataPackage);

        int retryCounter = 0;

        while (returnValue == -1 && retryCounter < 10) {
            // retry if error occured

            // Wait a second
            this.waitMillis(1000);
            retryCounter++;

            returnValue = sendDataPackage(socket, dataPackage);
        }

        if (retryCounter >= 10) {
            // Close connection
            this.closeSocket(socket);
            return null;
        }

        // TODO: Retry + Error Handling
        List<DataPackage> serverResponse = getDataPackageList(socket);

        // TODO: handle other cases than messages for user exist

        if (serverResponse == null) {
            // Error
            this.closeSocket(socket);
            return null;
        } else {
            if (!serverResponse.isEmpty()) {

                DataPackage responsePackage = serverResponse.remove(0);

                System.out.println(responsePackage.getFlag());

                if (responsePackage.getFlag() == -6) {
                    // No chathistory found on server

                    messagesList.add(new Message("", "", null)); // Empty message object

                } else if (responsePackage.getFlag() == 9) {

                    System.out.println("ChatPartner: " + chatPartner);
                    System.out.println("Username: " + username);

                    // Add messages to messages list
                    if ((responsePackage.getUsername().equals(username) && responsePackage.getReceiver().equals(chatPartner)) || (responsePackage.getUsername().equals(chatPartner) && responsePackage.getReceiver().equals(username))) {

                        System.out.println("Added: Username: " + responsePackage.getUsername() + " Receiver: " + responsePackage.getReceiver());
                        messagesList.add(
                                new Message(responsePackage.getUsername(),
                                        responsePackage.getMessage(),
                                        responsePackage.getTimestamp()));
                    }
                    Iterator iterator = serverResponse.iterator();
                    while (iterator.hasNext()) {
                        DataPackage tempData = (DataPackage) iterator.next();
                        if ((tempData.getUsername().equals(username) && tempData.getReceiver().equals(chatPartner)) || (tempData.getUsername().equals(chatPartner) && tempData.getReceiver().equals(username))) {

                            System.out.println("Added: Username: " + tempData.getUsername() + " Receiver: " + tempData.getReceiver());
                            messagesList.add(
                                    new Message(tempData.getUsername(),
                                            tempData.getMessage(),
                                            tempData.getTimestamp()));
                        }
                    }
                } else if (responsePackage.getFlag() == -2) {
                    // Wrong password or username

                } else if (responsePackage.getFlag() == -4) {
                    // General Error

                } else {
                    // Unknown Error
                }
            } else {
                // error
            }
        }

        return messagesList;
    }

    public String sendMessage(String receiver, String message) {

        Host host = selectHost();
        Socket socket = null;
        String returnString = "";

        try {
            socket = new Socket(host.getHostname(), host.getPort());
        } catch (UnknownHostException e) {
            System.out.println("Error: Host not known");

            return "Error: Host not known";

        } catch (IOException e) {
            System.out.println("Error: IOException");

            return "Error: IOException";
        }

        DataPackage dataPackage = new DataPackage(2, username, password, receiver, message, new Date());
        int returnValue = sendDataPackage(socket, dataPackage);

        int retryCounter = 0;

        while (returnValue == -1 && retryCounter < 10) {
            // retry if error occured

            // Wait a second
            this.waitMillis(1000);
            retryCounter++;

            returnValue = sendDataPackage(socket, dataPackage);
        }

        if (retryCounter >= 10) {
            // Close connection
            this.closeSocket(socket);
            return "Error: Could not send message";
        }


        // TODO: Retry + Error Handling
        List<DataPackage> serverResponse = getDataPackageList(socket);

        if (serverResponse == null) {
            // Error
            // Close connection
            this.closeSocket(socket);
            return "Error: Could not send message";
        } else {
            if (!serverResponse.isEmpty()) {
                DataPackage responsePackage = serverResponse.remove(0);

                int responseFlag = responsePackage.getFlag();

                switch (responseFlag) {
                    case 7:
                        System.out.println(responseFlag);
                        returnString = "Message accepted";
                        break;
                    case -2:
                        System.out.println(responseFlag);
                        returnString = "Wrong Username or Password";
                        break;
                    case -3:
                        System.out.println(responseFlag);
                        returnString = "Receiver does not exists";
                        break;
                    case -4:
                        System.out.println(responseFlag);
                        returnString = "An error occured";
                        break;
                    case -5:
                        System.out.println(responseFlag);
                        returnString = "You can not be the receiver";
                        break;

                    default:
                        System.out.println(responseFlag);
                        returnString = "An error occured";
                }

            } else {
                // Error
                // Close connection
                this.closeSocket(socket);
                return "Error: Server response was empty";
            }
        }

        // Close connection
        this.closeSocket(socket);

        return returnString;
    }

    public List<Message> getUpdates() {

        Host host = selectHost();
        Socket socket = null;
        List<Message> messagesList = new LinkedList<Message>();

        try {
            socket = new Socket(host.getHostname(), host.getPort());
        } catch (UnknownHostException e) {
            System.out.println("Error: Host not known");

            return null;

        } catch (IOException e) {
            System.out.println("Error: IOException");

            return null;
        }

        DataPackage dataPackage = new DataPackage(3, username, password);
        int returnValue = sendDataPackage(socket, dataPackage);

        int retryCounter = 0;

        while (returnValue == -1 && retryCounter < 10) {
            // retry if error occured

            // Wait a second
            this.waitMillis(1000);
            retryCounter++;

            returnValue = sendDataPackage(socket, dataPackage);
        }

        if (retryCounter >= 10) {
            // Close connection
            this.closeSocket(socket);
            return null;
        }


        // TODO: Retry + Error Handling
        List<DataPackage> serverResponse = getDataPackageList(socket);

        // TODO: handle other cases than messages for user exist

        if (serverResponse == null) {
            // Error
        } else {
            if (!serverResponse.isEmpty()) {

                DataPackage responsePackage = serverResponse.remove(0);

                if (responsePackage.getFlag() == 5) {
                    // No new Messages

                    messagesList.add(new Message("", "", null)); // Empty message object

                } else if (responsePackage.getFlag() == 4) {
                    // Add messages to messages list
                    messagesList.add(
                            new Message(responsePackage.getUsername(),
                                    responsePackage.getMessage(),
                                    responsePackage.getTimestamp()));

                    Iterator iterator = serverResponse.iterator();
                    while (iterator.hasNext()) {
                        DataPackage tempData = (DataPackage) iterator.next();
                        messagesList.add(
                                new Message(tempData.getUsername(),
                                        tempData.getMessage(),
                                        tempData.getTimestamp()));
                    }
                } else if (responsePackage.getFlag() == -2) {
                    // Wrong password or username

                } else if (responsePackage.getFlag() == -4) {
                    // General Error

                } else {
                    // Unknown Error
                }
            } else {
                // Error
            }
        }

        // Close connection
        this.closeSocket(socket);

        return messagesList;
    }

    public String register() {

        Host host = selectHost();
        Socket socket = null;

        try {
            socket = new Socket(host.getHostname(), host.getPort());
        } catch (UnknownHostException e) {
            System.out.println("Error: Host not known");

            return "Error: Host not known";

        } catch (IOException e) {
            System.out.println("Error: IOException");

            return "Error: IOException";
        }

        String returnString = "";

        DataPackage dataPackage = new DataPackage(1, username, password);
        int returnValue = sendDataPackage(socket, dataPackage);

        int retryCounter = 0;

        while (returnValue == -1 && retryCounter < 10) {
            // retry if error occured

            // Wait a second
            this.waitMillis(1000);
            retryCounter++;

            returnValue = sendDataPackage(socket, dataPackage);
        }

        if (retryCounter >= 10) {
            // Close connection
            this.closeSocket(socket);
            return "Error: Could not send message";
        }

        // TODO: Retry + Error Handling
        List<DataPackage> serverResponse = getDataPackageList(socket);

        if (serverResponse == null) {
            // Error
            this.closeSocket(socket);
            return "Error: Server response is null";

        } else {
            if (!serverResponse.isEmpty()) {
                DataPackage responsePackage = serverResponse.remove(0);

                int responseFlag = responsePackage.getFlag();

                System.out.println(responseFlag);

                switch (responseFlag) {
                    case -1:
                        returnString = "Registration failed";
                        break;
                    case 6:
                        returnString = "Registration successful";
                        break;

                    default:
                        returnString = "An error occured";
                }

            } else {
                // Error
                // Close connection
                this.closeSocket(socket);
                returnString = "An error occured";
            }
        }

        // Close connection
        this.closeSocket(socket);

        return returnString;
    }

    private List<DataPackage> getDataPackageList(Socket socketFromServer) {

        List<DataPackage> data = null;
        boolean errorOccured = false;

        if (socketFromServer != null) {

            try {
                InputStream inputStream = socketFromServer.getInputStream();

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
                    if (object instanceof List) {
                        data = (List<DataPackage>) object;
                    } else {
                        throw new Exception("Error: Not a valid DataPackage-List");
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
            } catch (ClassNotFoundException e) {
                System.out.println("Error: Class not found...");
                e.printStackTrace();
            } catch (Exception e) {
                System.out.println("Error: An error occured...");
                e.printStackTrace();
            }
        }

        return data;
    }

    private int sendDataPackage(Socket socketToServer, DataPackage dataPackage) {

        int returnFlag = 0;

        if (socketToServer != null) {

            try {
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(socketToServer.getOutputStream());

                objectOutputStream.writeObject(dataPackage);
            } catch (IOException e) {
                System.out.println("Error: Could not send data...");
                e.printStackTrace();

                return -1; // Could not send Data
            }
        }

        return returnFlag;
    }

    private void closeSocket(Socket socket) {
        try {
            socket.close();
        } catch (IOException e) {
            System.out.println("Error: Could not close connection");
            e.printStackTrace();
        }
    }

    private Host selectHost() {
        return hosts.get(this.randomNumber(hosts.size()));
    }

    private int randomNumber(int max) {
        return (int) (Math.random() * max);
    }

    private void waitMillis(int millis) {
        try {
            System.out.println("Retrying...");
            TimeUnit.MILLISECONDS.sleep(millis);
        } catch (InterruptedException interruptedExeption) {
            System.out.println("Error: Could not sleep for " + millis + " milliseconds...");
        }
    }
}
