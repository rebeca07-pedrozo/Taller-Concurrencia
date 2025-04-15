package servidor;

public class User implements Observer {
    private String username;

    public User(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public void update(String message) {
        System.out.println(username + " received message: " + message);
    }
}


