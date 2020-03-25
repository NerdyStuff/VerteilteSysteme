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
     * The Message Object is created with the String which was received by the network. It fills all the fields for easier access. String pattern: sender#password#receiver#timestamp#message
     * @param inputString this is the input string which is received from the network. This String will be dissected by the constructor of the message. It needs to be of this format: sender#password#receiver#timestamp#message
     */
    public Message (String inputString) throws WrongMessageInput {
       //sender#passwort#receiver#timestamp#message
       String[] splitMessage = inputString.split("#");
       //if the message is up normal, fill the fields
       if(splitMessage.length == 5) {
           setSender(splitMessage[0]);
           setPassword(splitMessage[1]);
           setReceiver(splitMessage[2]);
           setTimestamp(splitMessage[3]);
           setMessage(splitMessage[4]);
       //this is for messages which contain no message body, being only update requests
       }else if(splitMessage.length == 4) {
           setSender(splitMessage[0]);
           setPassword(splitMessage[1]);
           setReceiver(splitMessage[2]);
           setTimestamp(splitMessage[3]);
           message = "";
       //this for messages that contain one or more hashtags in the message body
       }else if(splitMessage.length >= 6){
           setSender(splitMessage[0]);
           setPassword(splitMessage[1]);
           setReceiver(splitMessage[2]);
           setTimestamp(splitMessage[3]);
           StringBuilder messageStuffed;
           messageStuffed = new StringBuilder(splitMessage[4]);
           for(int i = 5; i < splitMessage.length; i++) {
               messageStuffed.append("#");
               messageStuffed.append(splitMessage[i]);
           }
           setMessage(messageStuffed.toString());
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
