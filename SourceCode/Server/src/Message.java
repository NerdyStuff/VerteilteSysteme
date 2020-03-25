import java.util.Date;

/**
 * Server;
 */
public class Message extends Package{

    private String message;
    private Date timestamp;

    /**
     * The Message Object is created with the String which was received by the network. It fills all the fields for easier access. String pattern: sender#password#receiver#timestamp#message
     *
     */
    public Message (String sender, String password, String receiver, Date timestamp, String message) throws WrongMessageInput {
        super(sender, password, receiver);
        this.message = message;
        this.timestamp = timestamp;
        /*
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
         */
    }

    public String getMessage() {
        return message;
    }

    private void setMessage(String message) {
        this.message = message;
    }

}
