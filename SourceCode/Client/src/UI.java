import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class UI {

    private static JFrame frame;
    private static JTextField textFieldUsername, textFieldChat;
    private static JPasswordField textFieldPw;
    private static JTextField textFieldChatpartner;
    private static JEditorPane status;
    private static JTextArea chatIncoming;

    private static String username;
    private static String passwort;
    private static String to;
    private static Client c;

    public UI() {
        generateUI();
        while (true) {
            try {
                TimeUnit.SECONDS.sleep(1);
                // TODO: CHANGED TO UPDATE!
                List<Message> newMessages = c.getUpdates();
                printMessages(newMessages);
            } catch (Exception e) {

            }

        }
    }

    public static void generateUI() {

        //Frame
        frame = new JFrame();
        frame.setBounds(100, 100, 730, 489);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        //Username
        textFieldUsername = new JTextField();
        textFieldUsername.setBounds(128, 28, 86, 20);
        frame.getContentPane().add(textFieldUsername);
        textFieldUsername.setColumns(10);

        JLabel lblName = new JLabel("Username");
        lblName.setBounds(65, 31, 70, 14);
        frame.getContentPane().add(lblName);

        //Password
        textFieldPw = new JPasswordField();
        textFieldPw.setBounds(128, 65, 86, 20);
        frame.getContentPane().add(textFieldPw);
        textFieldPw.setColumns(10);

        JLabel lblPw = new JLabel("Password");
        lblPw.setBounds(65, 68, 70, 14);
        frame.getContentPane().add(lblPw);

        //Login Button
        JButton btnLogin = new JButton("Login");
        btnLogin.setBounds(230, 63, 89, 23);
        frame.getContentPane().add(btnLogin);

        //Login Button
        JButton btnRegister = new JButton("Register");
        btnRegister.setBounds(230, 27, 89, 23);
        frame.getContentPane().add(btnRegister);


        //Chat Partner
        textFieldChatpartner = new JTextField();
        textFieldChatpartner.setBounds(150, 112, 247, 20);
        frame.getContentPane().add(textFieldChatpartner);
        textFieldChatpartner.setColumns(10);

        JLabel lblchatpartner = new JLabel("Chat Partner");
        lblchatpartner.setBounds(65, 115, 140, 14);
        frame.getContentPane().add(lblchatpartner);

        //Chat now Button
        JButton btnChat = new JButton("Chat");
        btnChat.setBounds(400, 110, 89, 23);
        frame.getContentPane().add(btnChat);

        //Chat Field
        chatIncoming = new JTextArea(2, 20);
        chatIncoming.setEditable(false);
        //container drumherum
        JScrollPane scroll = new JScrollPane(chatIncoming);
        scroll.setBounds(65, 157, 400, 200);
        frame.getContentPane().add(scroll);


        //Message Field
        textFieldChat = new JTextField();
        textFieldChat.setBounds(65, 358, 400, 20);
        frame.getContentPane().add(textFieldChat);
        textFieldChat.setColumns(10);

        //Editor Pane for Status updates
        status = new JEditorPane();
        status.setBackground(SystemColor.control);
        status.setEditable(false);
        status.setFont(new Font("Courier New", Font.PLAIN, 11));
        //scroll
        JScrollPane scrollStatus = new JScrollPane(status);
        scrollStatus.setBounds(530, 30, 170, 370);
        scrollStatus.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        scrollStatus.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        frame.getContentPane().add(scrollStatus);

        //Clear Button
        JButton btnClear = new JButton("Clear");
        btnClear.setBounds(312, 387, 89, 23);
        frame.getContentPane().add(btnClear);

        //Send button
        JButton btnSend = new JButton("Send");
        btnSend.setBounds(65, 387, 89, 23);
        frame.getContentPane().add(btnSend);
        frame.setVisible(true);

        //Initialise the Fields
        chatIncoming.setEnabled(false);
        textFieldChatpartner.setEnabled(false);
        textFieldChat.setEnabled(false);
        btnChat.setEnabled(false);
        btnSend.setEnabled(false);
        btnChat.setEnabled(false);
        btnClear.setEnabled(false);

        status.setText("");

        //Eventlisteners
        btnLogin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (btnLogin.getText() == "Login") {
                    if (!textFieldUsername.getText().equals("") && !textFieldPw.getText().equals("")) {
                        username = textFieldUsername.getText();
                        passwort = textFieldPw.getText();
                        textFieldUsername.setBorder(new LineBorder(Color.gray, 1));
                        textFieldPw.setBorder(new LineBorder(Color.gray, 1));
                        textFieldUsername.setEnabled(false);
                        textFieldPw.setEnabled(false);
                        c = new Client(username, passwort);
                        List<Message> history = c.login();
                        printMessages(history);
                        btnLogin.setText("Logout");
                        textFieldChatpartner.setEnabled(true);
                        btnChat.setEnabled(true);
                        btnRegister.setEnabled(false);
                        log("Login data saved");
                    } else {
                        textFieldUsername.setBorder(new LineBorder(Color.red, 1));
                        textFieldPw.setBorder(new LineBorder(Color.red, 1));
                        log("Please enter a username and a password");
                    }
                } else if (btnLogin.getText() == "Logout") {
                    username = "";
                    passwort = "";
                    textFieldUsername.setText("");
                    textFieldPw.setText("");
                    chatIncoming.setText("");
                    textFieldChat.setText("");
                    textFieldChatpartner.setText("");
                    c = null;
                    textFieldUsername.setEnabled(true);
                    textFieldPw.setEnabled(true);
                    btnLogin.setText("Login");
                    chatIncoming.setEnabled(false);
                    textFieldChatpartner.setEnabled(false);
                    textFieldChat.setEnabled(false);
                    btnChat.setEnabled(false);
                    btnSend.setEnabled(false);
                    btnChat.setEnabled(false);
                    btnClear.setEnabled(false);
                    btnRegister.setEnabled(true);
                    log("Logged out");
                } else {
                    log("What is happening?");
                }
            }
        });

        btnRegister.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (!textFieldUsername.getText().equals("") && !textFieldPw.getText().equals("")) {
                    username = textFieldUsername.getText();
                    passwort = textFieldPw.getText();
                    textFieldUsername.setBorder(new LineBorder(Color.gray, 1));
                    textFieldPw.setBorder(new LineBorder(Color.gray, 1));
                    textFieldUsername.setEnabled(false);
                    textFieldPw.setEnabled(false);
                    c = new Client(username, passwort);
                    String registration = c.register();
                    log(registration);
                    btnLogin.setText("Logout");
                    textFieldChatpartner.setEnabled(true);
                    textFieldChat.setEnabled(true);
                    btnChat.setEnabled(true);
                    btnRegister.setEnabled(false);
                } else {
                    textFieldUsername.setBorder(new LineBorder(Color.red, 1));
                    textFieldPw.setBorder(new LineBorder(Color.red, 1));
                    log("Please enter a username and a password");
                }
            }
        });

        btnSend.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String message = textFieldChat.getText();
                String messageStatus = c.sendMessage(to, message);
                chatIncoming.append(" " + username + ": " + message + "\n");
                textFieldChat.setText("");
                log(messageStatus);
            }
        });
        btnClear.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (textFieldChat.getText().equals("")) {
                    chatIncoming.setText("");
                } else {
                    textFieldChat.setText("");
                }
            }
        });

        btnChat.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (btnChat.getText().equals("Chat")) {
                    if (!textFieldChatpartner.getText().equals("")) {
                        to = textFieldChatpartner.getText();
                        chatIncoming.setEnabled(true);
                        btnSend.setEnabled(true);
                        btnClear.setEnabled(true);
                        textFieldChat.setEnabled(true);
                        log("Chat partner saved");
                        btnChat.setText("Leave");
                    } else {
                        log("please enter chat partner");
                    }
                } else if (btnChat.getText().equals("Leave")) {
                    to = null;
                    chatIncoming.setText("");
                    chatIncoming.setEnabled(false);
                    btnClear.setEnabled(false);
                    textFieldChat.setEnabled(false);
                    textFieldChat.setText("");
                    btnSend.setEnabled(false);
                    textFieldChatpartner.setText("");
                    btnChat.setText("Chat");
                    log("You left the chat");
                } else {
                    log("What is going on?");
                }
            }
        });

        textFieldChat.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String message = textFieldChat.getText();
                String messageStatus = c.sendMessage(to, message);
                chatIncoming.append(" " + username + ": " + message + "\n");
                textFieldChat.setText("");
                log(messageStatus);
            }
        });

    }


    private static void log(String log) {
        status.setText(status.getText() + log + "\n");
    }

    private static void printMessages(List<Message> newMessages) {
        if (newMessages == null || newMessages.isEmpty()) {
            log("Network error");
        } else {
            Iterator iterator = newMessages.iterator();
            Message firstMessage = (Message) iterator.next();
            if (firstMessage.getSender().equals("")) {
                System.out.println("No new updates");
            } else {
                chatIncoming.append("[" + firstMessage.getTimestamp() + "] " + firstMessage.getSender() + ": " + firstMessage.getText() + "\n");
                while (iterator.hasNext()) {
                    Message message = (Message) iterator.next();
                    chatIncoming.append("[" + message.getTimestamp() + "] " + message.getSender() + ": " + message.getText() + "\n");
                }
            }
        }
    }
}
