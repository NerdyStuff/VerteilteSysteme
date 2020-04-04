import java.io.IOException;

public class Main {

    private static final boolean USE_GUI = true;

    public static void main(String[] args) {

        if (USE_GUI) {
            UI ui = new UI();
        } else {
            try {
                CLI cli = new CLI();
            } catch (IOException e) {
                System.out.println("Error: Could not create CLI...");
                e.printStackTrace();
            }
        }
    }

}
