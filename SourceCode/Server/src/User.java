import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    private String username;
    private String password;
    private LinkedList<Message> messages;
    private LinkedList<Message> chatHistory;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.messages = new LinkedList<Message>();
        this.chatHistory = new LinkedList<Message>();
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
        this.chatHistory.add(message);
    }

    public void addMessageToOwnHistory(Message message) {
        this.chatHistory.add(message);
    }

    public Message removeMessage() {
        return this.messages.remove();
    }

    public boolean hasNoMessages() {
        return this.messages.isEmpty();
    }

    public List<Message> getChathistory() {
        return this.chatHistory;
    }
}
