import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

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
            String message = reader.readLine();
            if(message.equals("\\exit")) {
                break;
            }else{
                c.writeMessage(to, message);
            }
        }
    }
}
