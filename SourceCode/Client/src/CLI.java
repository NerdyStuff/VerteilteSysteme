import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class CLI {

    Client c;
    BufferedReader reader;

    public CLI() throws IOException {
        c = null;
        reader = new BufferedReader(new InputStreamReader(System.in));
        login();
        System.out.println("Bitte gib den Username deines Chatpartners ein");
        String to = reader.readLine();
        printMessages(c.getChatHistory(to));
        System.out.println("Du kannst jetzt chatten:");
        NonBlockingBufferedReader nbbr = new NonBlockingBufferedReader(System.in);
        while (true) {
            String message = null;
            while ((message = nbbr.readLine()) != null) {
                if (message.equals("/exit")) {
                    return;
                }
                if (message.equals("/chatpartner")) {
                    System.out.println("Bitte gib den Username deines Chatpartners ein");
                    to = reader.readLine();
                    System.out.println(c.getChatHistory(to));
                    System.out.println("Du kannst jetzt chatten:");
                }
                if (message.equals("/logout")) {
                    login();
                }
                if (message.equals("/help")) {
                    System.out.println("/exit beendet das programm\n/chatpartner wechselt deinen chatpartner\n/logout loggt dich aus");
                }
                c.sendMessage(to, message);
            }
            printMessages(c.getUpdates(to));
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException interruptedExeption) {
                System.out.println("Error: Could not sleep for one second...");
                interruptedExeption.printStackTrace();
            }
        }
    }

    private void login() throws IOException {
        System.out.println("Nutze /help um die Hilfe aufzurufen\n\nHast du bereits ein Profil? (y/n)");
        String answer = reader.readLine();
        if (answer.equals("y")) {
            System.out.println("Bitte gib deinen Username ein");
            String username = reader.readLine();
            System.out.println("Bitte gib dein Passwort ein");
            String passwort = reader.readLine();
            try {
                c = new Client(username, passwort);
                System.out.println(c.login());
            } catch (Exception e) {
                System.out.println("Bei deinem Login ist etwas schiefgelaufen...");
            }
        } else if (answer.equals("n")) {
            System.out.println("Bitte registriere dich.");
            System.out.println("Bitte gib deinen Username ein");
            String username = reader.readLine();
            System.out.println("Bitte gib dein Passwort ein");
            String passwort = reader.readLine();
            try {
                c = new Client(username, passwort);
            } catch (Exception e) {
                System.out.println("Bei deiner Registrierung ist etwas schiefgelaufen...");
            }
            System.out.println(c.register());
        } else {
            System.out.println("???");
        }
    }

    private void printMessages(List<Message> messageList) {
        if(messageList != null) {
            Iterator iterator = messageList.iterator();
            Message firstMessage = (Message) iterator.next();
            if(!firstMessage.getSender().equals("ERROR")) {
                if (!firstMessage.getSender().equals("")) {
                    System.out.println("[" + firstMessage.getTimestamp() + "] " + firstMessage.getSender() + ": " + firstMessage.getText());
                    while (iterator.hasNext()) {
                        Message message = (Message) iterator.next();
                        System.out.println("[" + message.getTimestamp() + "] " + message.getSender() + ": " + message.getText());
                    }
                }
            }else{
                System.out.println("ERROR!");
            }
        }
    }
}
