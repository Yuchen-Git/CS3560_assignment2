import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.Vector;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class TwitterUser implements ActionListener, User, TwitterElement {
    private String id;
    private String tweetMsg;
    private Vector<User> subscribers;
    private DefaultListModel<String> newsfeed;
    private DefaultListModel<String> subscriptions;
    private JFrame frame;
    private JFrame timeFrame;
    private JPanel topPanel;
    private JPanel bottomPanel;
    private JTextField userID;
    private JTextField msgBox;
    private JButton follow;
    private JButton tweet;
    private JButton timeButton;
    private JScrollPane userPane;
    private JScrollPane newsfeedPane;
    private JList<String> userList;
    private JList<String> newsBox;
    private JList<String> timeInfo;
    private GridBagConstraints c;
    private long creationTime;
    private long lastUpdateTime;

    public TwitterUser(String name) {
        this.setCreationTime(System.currentTimeMillis());
        this.setID(name);
        this.subscribers = new Vector();
        this.subscriptions = new DefaultListModel();
        long milliseconds = this.creationTime;
        Instant instant = Instant.ofEpochMilli(milliseconds);
        LocalDateTime creationDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        this.getSubscriptions().addElement("User created: " + creationDateTime.toString());
        this.newsfeed = new DefaultListModel();
    }

    public Vector<User> getSubscribers() {
        return this.subscribers;
    }

    public void subscribe(User target) {
        target.getSubscribers().add(this);
        this.getSubscriptions().addElement(target.getID());
        this.update(target);
        if (this.userList != null) {
            this.userList.setModel(this.subscriptions);
        }

    }

    public void tweet(String msg) {
        this.setTweetMsg(msg);
        this.update(this);
        this.notifySubscribers();
    }

    public void notifySubscribers() {
        Iterator var1 = this.subscribers.iterator();

        while(var1.hasNext()) {
            User subscriber = (User)var1.next();
            subscriber.update(this);
        }

    }

    public void update(User user) {
        DefaultListModel var10000 = this.newsfeed;
        String var10002 = user.getID();
        var10000.add(0, var10002 + ": " + user.getTweetMsg());
        if (this.newsBox != null) {
            this.newsBox.setModel(this.newsfeed);
        }

        this.setLastUpdateTime(System.currentTimeMillis());
        this.newsfeed.add(0, "Last updated: " + Long.toString(this.getLastUpdateTime()));
    }

    public String getTweetMsg() {
        return this.tweetMsg;
    }

    public void accept(TwitterElementVisitor v) {
        v.visitTwitterUser(this);
    }

    public String getID() {
        return this.id;
    }

    public void setID(String id) {
        this.id = id;
    }

    public void setTweetMsg(String tweetMsg) {
        this.tweetMsg = tweetMsg;
    }

    public DefaultListModel<String> getNewsfeed() {
        return this.newsfeed;
    }

    public DefaultListModel<String> getSubscriptions() {
        return this.subscriptions;
    }

    public long getCreationTime() {
        return this.creationTime;
    }

    public void setCreationTime(long creationTime) {
        this.creationTime = creationTime;
    }

    public long getLastUpdateTime() {
        return this.lastUpdateTime;
    }

    public void setLastUpdateTime(long lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public JFrame buildGUI() {
        this.frame = new JFrame(this.getID() + " 's User View");
        this.frame.setPreferredSize(new Dimension(400, 450));
        this.frame.getContentPane().setLayout(new GridLayout(2, 1, 3, 4));
        this.frame.setDefaultCloseOperation(2);
        this.topPanel = new JPanel();
        this.topPanel.setLayout(new GridBagLayout());
        this.bottomPanel = new JPanel();
        this.bottomPanel.setLayout(new GridBagLayout());
        this.userID = new JTextField(20);
        this.msgBox = new JTextField(20);
        this.follow = new JButton("Subscribe to user");
        this.follow.addActionListener(this);
        this.tweet = new JButton("Post tweet");
        this.tweet.addActionListener(this);
        this.userList = new JList(this.subscriptions);
        this.newsBox = new JList(this.newsfeed);
        this.userPane = new JScrollPane(this.userList);
        this.newsfeedPane = new JScrollPane(this.newsBox);
        this.c = new GridBagConstraints();
        this.c.fill = 1;
        this.c.weightx = 0.5;
        this.c.weighty = 0.5;
        this.c.gridx = 0;
        this.c.gridy = 0;
        this.topPanel.add(this.userID, this.c);
        this.bottomPanel.add(this.msgBox, this.c);
        this.c.gridx = 1;
        this.topPanel.add(this.follow, this.c);
        this.bottomPanel.add(this.tweet, this.c);
        this.c.gridx = 0;
        this.c.gridy = 1;
        this.c.gridwidth = 2;
        this.topPanel.add(this.userPane, this.c);
        this.bottomPanel.add(this.newsfeedPane, this.c);
        this.frame.getContentPane().add(this.topPanel);
        this.frame.getContentPane().add(this.bottomPanel);
        this.frame.pack();
        this.frame.setVisible(true);
        return this.frame;
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Subscribe to user")) {
            if (!this.subscriptions.contains(this.userID.getText()) && AdminPanel.getInstance().getUserMap().get(this.userID.getText().toLowerCase()) != null) {
                this.subscribe((User)AdminPanel.getInstance().getUserMap().get(this.userID.getText().toLowerCase()));
            }
        } else {
            if (!e.getActionCommand().equals("Post tweet")) {
                return;
            }

            this.tweet(this.msgBox.getText());
        }

    }

    public String toString() {
        return this.getID();
    }
}
