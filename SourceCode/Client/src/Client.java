import java.io.*;
import java.net.*;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class Client {

    // TODO: List of Hostnames and ports
    // TODO: setter for adding hostname and portpair
    // TODO: Close connection!
    private HashMap<Integer, Host> hosts;

    //TODO: use host object as list to choose one


    private String username, password;

    public Client(String username, String password) {
        this.username = username;
        this.password = password;

        hosts = new HashMap<Integer, Host>();
        hosts
    }

    public String sendMessage(String receiver, String message) {

        Host host = selectHost();
        String returnString = "";

        DataPackage dataPackage = new DataPackage(2, username, password, receiver, message, new Date());
        int returnValue = sendDataPackage(dataPackage);

        // TODO : Error handling
        /*switch (returnValue) {
            case 0:
                break;
        }*/

        // TODO: Retry + Error Handling
        List<DataPackage> serverResponse = getDataPackageList(host.getHostname(), host.getPort());

        if (serverResponse == null) {
            // Error
        } else {
            if (!serverResponse.isEmpty()) {
                DataPackage responsePackage = serverResponse.remove(0);

                int responseFlag = responsePackage.getFlag();

                switch (responseFlag) {
                    case 7:
                        returnString = "Message accepted";
                        break;
                    case -2:
                        returnString = "Wrong Username or Password";
                        break;
                    case -3:
                        returnString = "Receiver does not exists";
                        break;
                    case -4:
                        returnString = "An error occured";
                        break;
                    case -5:
                        returnString = "You can not be the receiver";
                        break;

                    default:
                        returnString = "An error occured";
                }

            } else {
                // error
            }
        }


        return returnString;
    }

    public List<Message> getUpdates() {

        // Cases: 4, 5, -2, -4


        return null;
    }

    public String register() {

        Host host = selectHost();


        String returnString = "";

        DataPackage dataPackage = new DataPackage(1, username, password);
        int returnValue = sendDataPackage(dataPackage);

        // TODO : Error handling
        /*switch (returnValue) {
            case 0:
                break;
        }*/

        // TODO: Retry + Error Handling
        List<DataPackage> serverResponse = getDataPackageList(host.getHostname(), host.getPort());

        if (serverResponse == null) {
            // Error
        } else {
            if (!serverResponse.isEmpty()) {
                DataPackage responsePackage = serverResponse.remove(0);

                int responseFlag = responsePackage.getFlag();

                switch (responseFlag) {
                    case -1:
                        returnString = "Registration failed";
                        break;
                    case -6:
                        returnString = "Wrong Username or Password";
                        break;

                    default:
                        returnString = "An error occured";
                }

            } else {
                // error
            }
        }

        return returnString;
    }

    private List<DataPackage> getDataPackageList(String hostname, int port) {

        Socket socketFromServer = null;
        List<DataPackage> data = null;
        boolean errorOccured = false;

        try {
            socketFromServer = new Socket(hostname, port);
        } catch (UnknownHostException e) {
            System.out.println("Error: Host not known");

            return null;

        } catch (IOException e) {
            System.out.println("Error: IOException");

            return null;
        }

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
                    if (object instanceof DataPackage) {
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

    private int sendDataPackage(DataPackage dataPackage) {

        Host host = selectHost();

        int returnFlag = 0;
        Socket socketToServer = null;

        try {
            socketToServer = new Socket(host.getHostname(), host.getPort());
        } catch (UnknownHostException e) {
            System.out.println("Error: Host not known");

            return -1; //unknown Host

        } catch (IOException e) {
            System.out.println("Error: IOException");

            return -2; //IOException
        }

        if (socketToServer != null) {

            try {
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(socketToServer.getOutputStream());

                objectOutputStream.writeObject(dataPackage);
            } catch (IOException e) {
                System.out.println("Error: Could not send data...");
                e.printStackTrace();

                return -3; // Could not send Data
            }
        }

        return returnFlag;
    }

    private Host selectHost() {
        // getRandom host

        return null;
    }
}
