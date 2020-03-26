import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.TimeUnit;

public class UI {

    private static JFrame frame;
    private static JTextField textFieldUsername,textFieldChat;
    private static JTextField textFieldPw;
    private static JTextField textFieldChatpartner;

    private static String username;
    private static String passwort;
    private static String to;
    private static Client c;

    public UI() {
        generateUI();
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
        textFieldPw = new JTextField();
        textFieldPw.setBounds(128, 65, 86, 20);
        frame.getContentPane().add(textFieldPw);
        textFieldPw.setColumns(10);

        JLabel lblPw = new JLabel("Password");
        lblPw.setBounds(65, 68, 70, 14);
        frame.getContentPane().add(lblPw);

        //Login Button
        JButton btnLogin = new JButton("Login");
        btnLogin.setBounds(230, 68, 89, 23);
        frame.getContentPane().add(btnLogin);

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
        JTextArea chatIncoming = new JTextArea();
        chatIncoming.setBounds(65, 157, 400, 200);
        frame.getContentPane().add(chatIncoming);
        chatIncoming.setEditable(false);

        //Message Field
        textFieldChat = new JTextField();
        textFieldChat.setBounds(65, 358, 400, 20);
        frame.getContentPane().add(textFieldChat);
        textFieldChat.setColumns(10);

        //Editor Pane for Status updates
        JEditorPane status = new JEditorPane();
        status.setBounds(530,30,130,370);
        status.setBackground(SystemColor.control);
        status.setEditable(false);
        frame.getContentPane().add(status);

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

        //Eventlisteners
        btnLogin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(!textFieldUsername.getText().equals("") && !textFieldPw.getText().equals("")) {
                    username = textFieldUsername.getText();
                    passwort = textFieldPw.getText();
                    textFieldUsername.setBorder(new LineBorder(Color.gray,1));
                    textFieldPw.setBorder(new LineBorder(Color.gray,1));
                    textFieldUsername.setEnabled(false);
                    textFieldPw.setEnabled(false);
                    c = new Client("localhost",1337,username,passwort);
                }else{
                    textFieldUsername.setBorder(new LineBorder(Color.red,1));
                    textFieldPw.setBorder(new LineBorder(Color.red,1));
                }
            }
        });

        btnSend.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

            }
        });
        btnClear.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

            }
        });

        btnChat.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

            }
        });

        //While schleife
        while(true){
            try {
                TimeUnit.SECONDS.sleep(1);
                c.sendMessage(textFieldChatpartner.getText(),"");
                //String messages = c.sendMessage();
                //if(!messages.equals("")) {
                //    chatIncoming.append(textFieldChatpartner.getText() + ": " + messages + "\n");
                //}
            }catch (Exception e){

            }

        }

    }
}
