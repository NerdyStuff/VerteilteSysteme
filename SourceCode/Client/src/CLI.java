import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

public class CLI {

    public CLI() throws IOException {
        Client c = null;
        System.out.println("Hast du bereits ein Profil?");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String answer = reader.readLine();
        if(answer.equals("y")) {
            System.out.println("Bitte gib deinen Username ein");
            String username = reader.readLine();
            System.out.println("Bitte gib dein Passwort ein");
            String passwort = reader.readLine();
            c = new Client(username, passwort);
            System.out.println(c.login());
        }else if(answer.equals("n")){
            System.out.println("Bitte registriere dich.");
            System.out.println("Bitte gib deinen Username ein");
            String username = reader.readLine();
            System.out.println("Bitte gib dein Passwort ein");
            String passwort = reader.readLine();
            c = new Client(username, passwort);
            System.out.println(c.register());
        }else{
            System.out.println("???");
        }
        System.out.println("Bitte gib den Username deines Chatpartners ein");
        String to = reader.readLine();
        System.out.println(c.getChatHistory(to));
        System.out.println("Du kannst jetzt chatten:");
        NonBlockingBufferedReader nbbr = new NonBlockingBufferedReader(reader);
        while (true) {
            String message = "";
            if ((message = nbbr.readLine()) != null) {
                if (message.equals("/exit")) {
                    break;
                }
                if (message.equals("/chatpartner")){
                    System.out.println("Bitte gib den Username deines Chatpartners ein");
                    to = reader.readLine();
                    System.out.println(c.getChatHistory(to));
                    System.out.println("Du kannst jetzt chatten:");
                }
                if (message.equals("/help")){
                    System.out.println("/exit beendet das programm" +
                            "/chatpartner wechselt deinen chatpartner");
                }
            }
            c.getUpdates(to);
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException interruptedExeption) {
                System.out.println("Error: Could not sleep for one second...");
                interruptedExeption.printStackTrace();
            }
        }
    }
}
