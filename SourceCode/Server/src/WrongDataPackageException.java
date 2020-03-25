/**
 * Exception class for wrong data got from socket
 */

public class WrongDataPackageException extends Exception {

    public WrongDataPackageException(String message) {
        super(message);
    }
}
