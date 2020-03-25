import java.io.*;
import java.net.*;
import java.util.Date;

public class Client {
    private String hostname; //Konstante für den Hostname
    private int port; //Konstante für den Port
    private String username, password;
    private String newMessages;

    /**
     * Creates a client that can log in to the server program
     * @param hostname hostname of the server. For local use, localhost
     * @param port portnumber of the server
     * @param username username of the user
     * @param password password of the user
     */
    public Client(String hostname, int port, String username, String password) //Konstruktor
    {
        this.username = username;
        this.password = password;
        this.hostname = hostname;
        this.port = port;
        newMessages = "";
    }

    public void writeMessage(String to, String message) {
        Socket server = null;

        try
        {
            server = new Socket(hostname, port); //neuer Socket zum Server
            PrintWriter printWriter = new PrintWriter(server.getOutputStream(), true);
            printWriter.println(constructMessageString(to, message));

            BufferedReader reader = new BufferedReader(new InputStreamReader(server.getInputStream()));
            String input = "";
            while((input = reader.readLine())!= null) {
                System.out.println(input);
                newMessages += input;
            }
        }
        catch (IOException exception)
        {
            exception.printStackTrace();
        } finally {
            if (server != null)
            {
                try
                {
                    server.close();
                } catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

    private String constructMessageString(String to, String message) {
        Date date = new Date();

        return (username + "#" + password + "#" + to + "#" + date + "#" + message);
    }

    public String getNewMessages(){
        return newMessages;
    }

    public void deleteNewMessages(){
        newMessages = "";
    }
}

