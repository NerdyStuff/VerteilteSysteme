import org.jetbrains.annotations.Contract;

/**
 * Server;
 */
public class Message {

    private String sender;
    private String password;
    private String receiver;
    private String timestamp;
    private String message;

    /**
     *
     * @param inputString
     */
    public Message (String inputString)
    {
       //<sender>#passwort#receiver#timestamp#message
        
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
