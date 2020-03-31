import java.io.Serializable;
import java.util.Date;

public class DataPackage implements Serializable {

    private static final long serialVersionUID = 1L;

    private int flag;
    /*flag:
    Mit dem Flag wird den Client oder dem anderen Server mitgeteilt, welche Daten erwartet werden können

    Wert: 1 Registrierung
    Wert: 2 Nachricht
    Wert: 3 Update-Request
    Wert: 4 Neue Nachrichten für den Client
    Wert: 5 Keine neuen Nachrichten für den Client
    Wert: 6 Registrierung erfolgreich
    Wert: 7 Nachricht erfolgreich angenommen
    Wert: 8 Anfrage der Chathistorie
    Wert: 9 Chathistory

    Wert: 20 Commit request (prepare)
    Wert: 21Ready Nachricht von anderen Server(n)
    Wert: 22Commit von Koordinator
    Wert: 23Acknowledge von anderen Servern

    Wert: -1 Registrierung fehlgeschlagen
    Wert: -2 Falsches Passwort oder Nutzername
    Wert: -3 Empfänger existiert nicht
    Wert: -4 Allgemeiner Fehler
    Wert: -5 Empfänger darf nicht gleicher Nutzer sein wie Sender
    Wert: -6 Keine Chathistorie gefunden / Fehler

    Wert: -20 Failed von anderen Servern
    Wert: -21 Abort von Koordinato
    */
    private int syncFlag;
    private String username, password, receiver, message;
    private Date timestamp;
    private Object object;

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

    // Constructor for Messages from Server with Messages for User
    public DataPackage(int flag, String username, String receiver, String message, Date timestamp) {
        this.flag = flag;
        this.username = username;
        this.receiver = receiver;
        this.message = message;
        this.timestamp = timestamp;
    }

    // Constructor for sending data between servers
    public DataPackage(int flag, int syncFlag, Object object) {
        this.flag = flag;
        this.syncFlag = syncFlag;
        this.object = object;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public int getSyncFlag() {
        return syncFlag;
    }

    public void setSyncFlag(int syncFlag) {
        this.syncFlag = syncFlag;
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

    public void setObject(Object object) {
        this.object = object;
    }

    public Object getObject() {
        return this.object;
    }
}
