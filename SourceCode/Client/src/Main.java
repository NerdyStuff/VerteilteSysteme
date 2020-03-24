import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) throws IOException {
        System.out.println("Bitte gib deinen Username ein");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String username = reader.readLine();
        System.out.println("Bitte gib dein Passwort ein");
        String passwort = reader.readLine();
        System.out.println("Bitte gib den Username deines Chatpartners ein");
        String to = reader.readLine();
        Client c = new Client("localhost",1337,username,passwort);
        System.out.println("Du kannst jetzt chatten:");
        while(true) {
            String message = "";
            if((message = reader.readLine())!= null) {
                if (message.equals("\\exit")) {
                    break;
                }
            }
            c.writeMessage(to, message);
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException interruptedExeption) {
                System.out.println("Error: Could not sleep for one second...");
                interruptedExeption.printStackTrace();
            }
        }
    }
}
