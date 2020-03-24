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
    public Message (String inputString) throws WrongMessageInput {
       //<sender>#passwort#receiver#timestamp#message
       String[] splitMessage = inputString.split("#");
       if(splitMessage.length == 5) {
           setSender(splitMessage[0]);
           setPassword(splitMessage[1]);
           setReceiver(splitMessage[2]);
           setTimestamp(splitMessage[3]);
           setMessage(splitMessage[4]);
       }else{
           throw new WrongMessageInput("Something was wrong with the Message input");
       }
    }

    public String getSender() {
        return sender;
    }

    private void setSender(String sender) {
        this.sender = sender;
    }

    public String getPassword() {
        return password;
    }

    private void setPassword(String password) {
        this.password = password;
    }

    public String getReceiver() {
        return receiver;
    }

    private void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getTimestamp() {
        return timestamp;
    }

    private void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    private void setMessage(String message) {
        this.message = message;
    }
}
