import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

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
