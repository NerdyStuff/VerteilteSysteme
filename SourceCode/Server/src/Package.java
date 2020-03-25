import java.util.Date;

public abstract class Package {
    private String sender,password,receiver;
    private Date timestamp;

    public Package(String sender, String password, String receiver, Date timestamp) {
        this.sender = sender;
        this.password = password;
        this.receiver = receiver;
        this.timestamp = timestamp;
    }

    public Package(){}

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

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
