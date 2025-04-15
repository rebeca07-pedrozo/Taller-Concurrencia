package cliente;

import servidor.ChatServer;
import servidor.User;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class NewJFrame extends JFrame {
    private ChatServer chatServer;
    private JTextArea chatArea;

    public NewJFrame() {
        chatServer = new ChatServer();
        chatArea = new JTextArea();
        chatArea.setEditable(false);

        JTextField usernameField = new JTextField();
        JButton connectButton = new JButton("Connect");

        connectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                User newUser = new User(username);
                chatServer.userConnected(newUser);
            }
        });

        add(usernameField, "North");
        add(connectButton, "South");
        add(new JScrollPane(chatArea), "Center");

        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public static void main(String[] args) {
        new NewJFrame();
    }
}
