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
    private static JTextField textFieldUsername,textFieldChat;
    private static JTextField textFieldPw;
    private static JTextField textFieldChatpartner;

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
    public static void UI() {
        frame = new JFrame();
        frame.setBounds(100, 100, 730, 489);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        textFieldUsername = new JTextField();
        textFieldUsername.setBounds(128, 28, 86, 20);
        frame.getContentPane().add(textFieldUsername);
        textFieldUsername.setColumns(10);

        JLabel lblName = new JLabel("Username");
        lblName.setBounds(65, 31, 70, 14);
        frame.getContentPane().add(lblName);

        JLabel lblPw = new JLabel("Password");
        lblPw.setBounds(65, 68, 70, 14);
        frame.getContentPane().add(lblPw);

        textFieldPw = new JTextField();
        textFieldPw.setBounds(128, 65, 86, 20);
        frame.getContentPane().add(textFieldPw);
        textFieldPw.setColumns(10);

        JButton btnLogin = new JButton("Login");
        btnLogin.setBounds(230, 68, 89, 23);
        frame.getContentPane().add(btnLogin);

        JLabel lblchatpartner = new JLabel("Chat Partner");
        lblchatpartner.setBounds(65, 115, 140, 14);
        frame.getContentPane().add(lblchatpartner);

        textFieldChatpartner = new JTextField();
        textFieldChatpartner.setBounds(150, 112, 247, 20);
        frame.getContentPane().add(textFieldChatpartner);
        textFieldChatpartner.setColumns(10);

        JTextArea chatIncoming = new JTextArea();
        chatIncoming.setBounds(80, 157, 400, 200);
        frame.getContentPane().add(chatIncoming);
        chatIncoming.setEditable(false);

        textFieldChat = new JTextField();
        textFieldChat.setBounds(80, 358, 400, 20);
        frame.getContentPane().add(textFieldChat);
        textFieldChat.setColumns(10);

        JButton btnClear = new JButton("Clear");
        btnClear.setBounds(312, 387, 89, 23);
        frame.getContentPane().add(btnClear);

        JButton btnSubmit = new JButton("Send");
        btnSubmit.setBounds(65, 387, 89, 23);
        frame.getContentPane().add(btnSubmit);
        frame.setVisible(true);

        btnLogin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                if(textFieldUsername.getText()!= "" && textFieldPw.getText() != "")
                    username = textFieldUsername.getText();
                    passwort = textFieldPw.getText();
                    textFieldUsername.setEnabled(false);
                    textFieldPw.setEnabled(false);
                    c = new Client("localhost",1337,username,passwort);
            }
        });

        btnSubmit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                c.writeMessage(textFieldChatpartner.getText(),textFieldChat.getText());
                chatIncoming.append(username + ": " + textFieldChat.getText() + "\n");
                String messages = c.getNewMessages();
                if(!messages.equals("")) {
                    chatIncoming.append(textFieldChatpartner.getText() + ": " + messages + "\n");
                }
                textFieldChat.setText("");
            }
        });
        btnClear.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                textFieldChat.setText("");
                chatIncoming.setText("");
            }
        });

        while(true){
            try {
                TimeUnit.SECONDS.sleep(1);
                c.writeMessage(textFieldChatpartner.getText(),"");
                String messages = c.getNewMessages();
                if(!messages.equals("")) {
                    chatIncoming.append(textFieldChatpartner.getText() + ": " + messages + "\n");
                }
            }catch (Exception e){

            }

        }

    }
}
