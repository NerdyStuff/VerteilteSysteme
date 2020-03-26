import javax.xml.crypto.Data;
import java.io.*;
import java.net.*;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Client {

    private String hostname; //Hostname
    private int port; //port
    private String username, password;

    public Client(String hostname, int port, String username, String password) {
        this.username = username;
        this.password = password;
        this.hostname = hostname;
        this.port = port;
    }

    public void sendMessage(String receiver, String message) {

        Socket socketToServer = null;

        while (socketToServer != null) {
            try {
                socketToServer = new Socket(hostname, port);
            } catch (UnknownHostException e) {
                System.out.println("Error: Host not known");

                // Sleep one second
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException interruptedExeption) {
                    System.out.println("Error: Could not sleep for one second...");
                }

            } catch (IOException e) {
                System.out.println("Error: IOException");

                // Sleep one second
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException interruptedExeption) {
                    System.out.println("Error: Could not sleep for one second...");
                }
            }
        }

        if(socketToServer != null) {

            try {
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(socketToServer.getOutputStream());

                DataPackage dataPackage = new DataPackage(2, username, password, receiver, message,  new Date());

                objectOutputStream.writeObject(dataPackage);
            } catch (IOException e) {
                System.out.println("Error: Could not send data...");
                e.printStackTrace();
            }
        }
    }

    public void getUpdates() {
/*
        Socket socketToServer = null;

        while (socketToServer != null) {
            try {
                socketToServer = new Socket(hostname, port);
            } catch (UnknownHostException e) {
                System.out.println("Error: Host not known");

                // Sleep one second
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException interruptedExeption) {
                    System.out.println("Error: Could not sleep for one second...");
                }

            } catch (IOException e) {
                System.out.println("Error: IOException");

                // Sleep one second
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException interruptedExeption) {
                    System.out.println("Error: Could not sleep for one second...");
                }
            }
        }

        if(socketToServer != null) {

            try {
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(socketToServer.getOutputStream());

                DataPackage dataPackage = new DataPackage(2, username, password  new Date());

                objectOutputStream.writeObject(dataPackage);
            } catch (IOException e) {
                System.out.println("Error: Could not send data...");
                e.printStackTrace();
            }
        }
    } */
}
