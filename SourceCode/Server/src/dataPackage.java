import java.util.Date;

public class dataPackage {
    private int flag; //0: Update 1: Message 2: Registration
    private String username, password, receiver, message;
    private Date timestamp;

    public dataPackage(int flag, String username, String password) {
        this.flag = flag;
        this.username = username;
        this.password = password;
    }

    public dataPackage(String username, String password, String receiver, String message, Date timestamp) {
        this.username = username;
        this.password = password;
        this.receiver = receiver;
        this.message = message;
        this.timestamp = timestamp;
        this.flag = 1;
    }
}
