import java.io.Serializable;
import java.util.Date;

public class DataPackage implements Serializable {

    // TODO: implement Serializable
    private int flag;
    private String username, password, receiver, message;
    private Date timestamp;

    // Constructor for whole DataPackage for Messages from Client to Server
    public DataPackage(int flag, String username, String password, String receiver, String message, Date timestamp) {
        this.flag = flag;
        this.username = username;
        this.password = password;
        this.receiver = receiver;
        this.message = message;
        this.timestamp = timestamp;
    }

    // Constructor for Update and Registration
    public DataPackage(int flag, String username, String password) {
        this.flag = flag;
        this.username = username;
        this.password = password;
    }

    // Constructor for StatusCodes from Server
    public DataPackage(int flag, String message) {
        this.flag = flag;
        this.message = message;
    }

    // Constructor for Messages from Server
    public DataPackage(int flag, String username, String message, Date timestamp) {
        this.flag = flag;
        this.username = username;
        this.message = message;
        this.timestamp = timestamp;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
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

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
