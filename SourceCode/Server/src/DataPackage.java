import java.util.Date;

public class DataPackage {

    private int flag;
    private String username, password, receiver, message;
    private Date timestamp;

    public DataPackage(int flag, String username, String password, String receiver, String message, Date timestamp) {
        this.flag = flag;
        this.username = username;
        this.password = password;
        this.receiver = receiver;
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
