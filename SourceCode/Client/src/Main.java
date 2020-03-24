import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

public class Main {

    private static JFrame frame;
    private static JTextField textField;
    private static JTextField textField_1;
    private static JTextField textField_2;

    private static String username;
    private static String passwort;
    private static String to;
    private static Client c;

    public static void main(String[] args) throws IOException {
        UI();
        System.out.println("Bitte gib deinen Username ein");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String username = reader.readLine();
        System.out.println("Bitte gib dein Passwort ein");
        String passwort = reader.readLine();
        System.out.println("Bitte gib den Username deines Chatpartners ein");
        String to = reader.readLine();
        Client c = new Client("localhost",1337,username,passwort);
        System.out.println("Du kannst jetzt chatten:");
        while(true) {
            String message = "";
            if((message = reader.readLine())!= null) {
                if (message.equals("\\exit")) {
                    break;
                }
            }
            c.writeMessage(to, message);
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException interruptedExeption) {
                System.out.println("Error: Could not sleep for one second...");
                interruptedExeption.printStackTrace();
            }
        }
    }
    public static void UI(){
        frame = new JFrame();
        frame.setBounds(100, 100, 730, 489);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        textField = new JTextField();
        textField.setBounds(128, 28, 86, 20);
        frame.getContentPane().add(textField);
        textField.setColumns(10);

        JLabel lblName = new JLabel("Username");
        lblName.setBounds(65, 31, 70, 14);
        frame.getContentPane().add(lblName);

        JLabel lblPw = new JLabel("Password");
        lblPw.setBounds(65, 68, 70, 14);
        frame.getContentPane().add(lblPw);

        textField_1 = new JTextField();
        textField_1.setBounds(128, 65, 86, 20);
        frame.getContentPane().add(textField_1);
        textField_1.setColumns(10);

        JButton btnLogin = new JButton("Login");
        btnLogin.setBounds(230, 68, 89, 23);
        frame.getContentPane().add(btnLogin);

        JLabel lblchatpartner = new JLabel("Chat Partner");
        lblchatpartner.setBounds(65, 115, 140, 14);
        frame.getContentPane().add(lblchatpartner);

        textField_2 = new JTextField();
        textField_2.setBounds(150, 112, 247, 20);
        frame.getContentPane().add(textField_2);
        textField_2.setColumns(10);

        JTextArea textArea_1 = new JTextArea();
        textArea_1.setBounds(80, 157, 400, 200);
        frame.getContentPane().add(textArea_1);

        JButton btnClear = new JButton("Clear");
        btnClear.setBounds(312, 387, 89, 23);
        frame.getContentPane().add(btnClear);

        JButton btnSubmit = new JButton("Send");
        btnSubmit.setBounds(65, 387, 89, 23);
        frame.getContentPane().add(btnSubmit);
        frame.setVisible(true);

        btnLogin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                if(textField.getText()!= "" && textField_1.getText() != "")
                    username = textField.getText();
                    passwort = textField_1.getText();
                    textField.setEnabled(false);
                    textField_1.setEnabled(false);
                    c = new Client("localhost",1337,username,passwort);
            }
        });

        btnSubmit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                c.writeMessage(textField_2.getText(),textArea_1.getText());
            }
        });

    }
}
