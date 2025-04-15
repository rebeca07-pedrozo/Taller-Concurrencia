package servidor;

import java.util.ArrayList;
import java.util.List;

public class ChatServer {
    private List<Observer> users;

    public ChatServer() {
        users = new ArrayList<>();
    }

    public void addUser(Observer user) {
        users.add(user);
    }

    public void removeUser(Observer user) {
        users.remove(user);
    }

    public void notifyUsers(String message) {
        for (Observer user : users) {
            user.update(message);
        }
    }

    public void userConnected(User user) {
        addUser(user);
        notifyUsers(user.getUsername() + " has connected.");
    }

    public void userDisconnected(User user) {
        removeUser(user);
        notifyUsers(user.getUsername() + " has disconnected.");
    }
}
