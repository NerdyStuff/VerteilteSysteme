import java.util.LinkedList;

public class User {

    private String username;
    private String password;
    private LinkedList<Message> messages;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.messages = new LinkedList<Message>();
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

    public void addMessage(Message message) {
        this.messages.add(message);
    }

    public Message removeMessage() {
        return this.messages.remove();
    }

    public boolean hasNoMessages() {
        return this.messages.isEmpty();
    }
}
