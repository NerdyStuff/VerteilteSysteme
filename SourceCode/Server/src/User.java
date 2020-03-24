import java.util.LinkedList;
import java.util.Queue;

/**
 * Server;
 */
public class User {

    private String username;
    private String password;
    private Queue<String> messages;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.messages = new LinkedList<String>();
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void addMessage(String message) {
        this.messages.add(message);
    }

    public String removeMessage() {
        return this.messages.remove();
    }

    public boolean hasNoMessages() {
        return this.messages.isEmpty();
    }
}
