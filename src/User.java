import java.util.List;

public interface User {
    void subscribe(User var1);

    void tweet(String var1);

    void notifySubscribers();

    void update(User var1);

    String getID();

    String getTweetMsg();

    List<User> getSubscribers();
}
