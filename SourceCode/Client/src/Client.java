
import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client
{
    private final String hostname = "localhost"; //Konstante für den Hostname
    private final int port = 1337; //Konstante für den Port
    private String nachricht = "klaus55#superTollesPw#peter123#123456#Hallo Peter";

    public static void main(String[] args) {
        Client c = new Client();
    }

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
            PrintWriter printWriter = new PrintWriter(server.getOutputStream(), true);
            printWriter.println(nachricht);

            Scanner scanner = new Scanner(server.getInputStream()); //neuer Scanner
            String input = scanner.nextLine(); //öffne scanner und schreibe in String bis \n kommt

            System.out.println(input); //schreibe Serverdaten
        }

        catch(UnknownHostException exception) //fange Exception ab
        {
            exception.printStackTrace(); //schreibe Exception
        }

        catch(IOException e) //fange Exception ab
        {
           e.printStackTrace(); //schreibe Exception
        }

        finally
        {
            if(server != null) //wenn Server nicht null
            {
                try //versuche
                {
                    server.close(); //schließe Verbindung
                }

                catch(IOException e) //fange Exception ab
                {
                    e.printStackTrace(); //schreibe Exception
                }
            }
        }
    }
}

