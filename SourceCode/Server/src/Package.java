import java.util.Date;

public abstract class Package {
    private String sender,password,receiver;

    public Package(String sender, String password, String receiver) {
        this.sender = sender;
        this.password = password;
        this.receiver = receiver;
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

}
