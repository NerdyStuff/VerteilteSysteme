
import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client
{
    private final String hostname = "localhost"; //Konstante für den Hostname
    private final int port = 1337; //Konstante für den Port
    private String nachricht = "Hello World!";

    public Client() //Konstruktor
    {
        this.connectToServer(); //rufe Methode connectToServer auf
    }

    private void connectToServer() //Methode connectToServer
    {
        Socket server = null; //neuer Socket mit Wert "null"

        try //versuche
        {
            server = new Socket(hostname, port); //neuer Socket zum Server
            Scanner scanner = new Scanner(server.getInputStream()); //neuer Scanner

            PrintWriter printWriter = new PrintWriter(server.getOutputStream(), true);

            printWriter.println(nachricht);

            String input = scanner.nextLine(); //öffne scanner und schreibe in String bis \n kommt

            System.out.println(input); //schreibe Serverdaten
        }

        catch(UnknownHostException lException) //fange Exception ab
        {
            lException.printStackTrace(); //schreibe Exception
        }

        catch(IOException lException) //fange Exception ab
        {
            lException.printStackTrace(); //schreibe Exception
        }

        finally
        {
            if(server != null) //wenn Server nicht null
            {
                try //versuche
                {
                    server.close(); //schließe Verbindung
                }

                catch(IOException lException) //fange Exception ab
                {
                    lException.printStackTrace(); //schreibe Exception
                }
            }
        }
    }
}

