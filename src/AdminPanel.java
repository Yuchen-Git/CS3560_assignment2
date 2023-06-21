import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

public class AdminPanel implements ActionListener {
    private static AdminPanel adminInstance = null;
    private HashMap<String, TwitterUser> userMap = new HashMap();
    private HashMap<String, UserGroup> groupMap = new HashMap();
    private JFrame frame;
    private JPanel treePanel;
    private JPanel buttonPanel;
    private JPanel innerTop;
    private JPanel innerMid;
    private JPanel innerBot;
    private JScrollPane treePane;
    private JButton addUser;
    private JButton addGroup;
    private JButton userView;
    private JButton userTotal;
    private JButton groupTotal;
    private JButton messageTotal;
    private JButton positivePercentage;
    private JButton validateUsers;
    private JButton lastUpdated;
    private JTextField userName;
    private JTextField groupName;
    private JLabel label;
    private JTree tree;
    private DefaultTreeModel modelTree;
    private DefaultMutableTreeNode root;
    private DefaultMutableTreeNode selectedNode;
    private DefaultMutableTreeNode temp;
    private GridBagConstraints c;

    private AdminPanel() {
        this.generateUI();
    }

    public static AdminPanel getInstance() {
        if (adminInstance == null) {
            adminInstance = new AdminPanel();
        }

        return adminInstance;
    }

    public HashMap<String, TwitterUser> getUserMap() {
        return this.userMap;
    }

    public void actionPerformed(ActionEvent a) {
        switch (a.getActionCommand()) {
            case "Create a new user":
                if (this.selectedNode == null || !(this.selectedNode.getUserObject() instanceof TwitterUser)) {
                    this.createUser(this.userName.getText().toLowerCase());
                }
                break;
            case "Create a new group":
                if (this.selectedNode == null || !(this.selectedNode.getUserObject() instanceof TwitterUser)) {
                    this.createGroup(this.groupName.getText().toLowerCase());
                }
                break;
            case "Open user view":
                if (this.selectedNode != null && this.selectedNode.getUserObject() instanceof TwitterUser) {
                    this.openView(this.selectedNode);
                } else {
                    this.label.setText("You must select a user.");
                }
                break;
            case "Show user total":
                UserCountVisitor uv = new UserCountVisitor();
                ((TwitterElement)this.root.getUserObject()).accept(uv);
                this.label.setText("Total # of users: " + Integer.toString(uv.getCounter()));
                break;
            case "Show group total":
                GroupCountVisitor gv = new GroupCountVisitor();
                ((TwitterElement)this.root.getUserObject()).accept(gv);
                this.label.setText("Total # of groups: " + Integer.toString(gv.getCounter()));
                break;
            case "Show message total":
                MessageCountVisitor mv = new MessageCountVisitor();
                ((TwitterElement)this.root.getUserObject()).accept(mv);
                this.label.setText("Total # of messages: " + Integer.toString(mv.getCounter()));
                break;
            case "Show positive percentage":
                PositiveCountVisitor pv = new PositiveCountVisitor();
                ((TwitterElement)this.root.getUserObject()).accept(pv);
                this.label.setText("% of positive messages: " + Double.toString(pv.getPercentage()));
                break;
            case "Validate Users":
                this.validateUsers();
                break;
            case "Find last updated user":
                this.userUpdateTime();
        }

    }

    public void createUser(String name) {
        if (name.length() < 2) {
            this.label.setText("Username is too short, must be at least 2 characters.");
        } else if (this.userMap.get(name) == null) {
            this.userMap.put(name, new TwitterUser(name));
            this.temp = new DefaultMutableTreeNode(this.userMap.get(name));
            this.temp.setAllowsChildren(false);

            try {
                this.selectedNode.add(this.temp);
                ((UserGroup)this.selectedNode.getUserObject()).add((TwitterElement)this.userMap.get(name));
            } catch (NullPointerException var3) {
                this.root.add(this.temp);
                ((UserGroup)this.root.getUserObject()).add((TwitterElement)this.userMap.get(name));
                this.modelTree.reload(this.root);
            }

            this.modelTree.reload(this.selectedNode);
            this.label.setText("User: " + name + " has been created.");
        } else {
            this.label.setText("User: " + name + " already exists.");
        }

    }

    public void createGroup(String name) {
        if (name.length() < 2) {
            this.label.setText("Group name is too short, must be at least 2 characters.");
        } else if (this.groupMap.get(name) == null) {
            this.groupMap.put(name, new UserGroup(name));
            this.temp = new DefaultMutableTreeNode(this.groupMap.get(name));

            try {
                this.selectedNode.add(this.temp);
                ((UserGroup)this.selectedNode.getUserObject()).add((TwitterElement)this.groupMap.get(name));
            } catch (NullPointerException var3) {
                this.root.add(this.temp);
                ((UserGroup)this.root.getUserObject()).add((TwitterElement)this.groupMap.get(name));
                this.modelTree.reload(this.root);
            }

            this.modelTree.reload(this.selectedNode);
            this.label.setText("Group: " + name + " has been created.");
        } else {
            this.label.setText("Group: " + name + " already exists.");
        }

    }

    public void openView(DefaultMutableTreeNode n) {
        ((TwitterUser)n.getUserObject()).buildGUI();
        this.label.setText(n.toString() + "'s user view opened.");
    }

    public void validateUsers() {
        ValidateUsersVisitor vv = new ValidateUsersVisitor();
        ((TwitterElement)this.root.getUserObject()).accept(vv);
        JFrame vFrame = new JFrame("User Validation");
        JList<String> vList = new JList(vv.results());
        vFrame.setPreferredSize(new Dimension(300, 200));
        vFrame.getContentPane().setLayout(new GridLayout());
        vFrame.getContentPane().add(vList);
        vFrame.setDefaultCloseOperation(2);
        vFrame.pack();
        vFrame.setVisible(true);
    }

    public void userUpdateTime() {
        LastUpdateVisitor lv = new LastUpdateVisitor();
        ((TwitterElement)this.root.getUserObject()).accept(lv);
        JFrame vFrame = new JFrame("Last updated user");
        JLabel timeLabel = new JLabel(lv.getId());
        vFrame.setPreferredSize(new Dimension(300, 200));
        vFrame.getContentPane().setLayout(new GridLayout());
        vFrame.getContentPane().add(timeLabel);
        vFrame.setDefaultCloseOperation(2);
        vFrame.pack();
        vFrame.setVisible(true);
    }

    public void generateUI() {
        this.frame = new JFrame("Admin Control Panel.");
        this.frame.setPreferredSize(new Dimension(800, 500));
        this.frame.setDefaultCloseOperation(3);
        this.frame.getContentPane().setLayout(new GridBagLayout());
        this.treePanel = new JPanel();
        this.buttonPanel = new JPanel();
        this.innerTop = new JPanel();
        this.innerMid = new JPanel();
        this.innerBot = new JPanel();
        this.treePanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        this.buttonPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        this.treePanel.setLayout(new GridLayout());
        this.buttonPanel.setLayout(new GridBagLayout());
        this.innerTop.setLayout(new GridBagLayout());
        this.innerMid.setLayout(new GridBagLayout());
        this.innerBot.setLayout(new GridBagLayout());
        this.addUser = new JButton("Create a new user");
        this.addUser.addActionListener(this);
        this.addGroup = new JButton("Create a new group");
        this.addGroup.addActionListener(this);
        this.userTotal = new JButton("Show user total");
        this.userTotal.addActionListener(this);
        this.groupTotal = new JButton("Show group total");
        this.groupTotal.addActionListener(this);
        this.messageTotal = new JButton("Show message total");
        this.messageTotal.addActionListener(this);
        this.positivePercentage = new JButton("Show positive percentage");
        this.positivePercentage.addActionListener(this);
        this.userView = new JButton("Open user view");
        this.userView.addActionListener(this);
        this.validateUsers = new JButton("Validate Users");
        this.validateUsers.addActionListener(this);
        this.lastUpdated = new JButton("Find last updated user");
        this.lastUpdated.addActionListener(this);
        this.root = new DefaultMutableTreeNode(new UserGroup("root"));
        this.modelTree = new DefaultTreeModel(this.root);
        this.tree = new JTree(this.modelTree);
        this.tree.setCellRenderer(new CustomRenderer());
        this.tree.getSelectionModel().setSelectionMode(1);
        this.tree.addTreeExpansionListener(new TreeExpansionListener() {
            public void treeExpanded(TreeExpansionEvent event) {
                TreePath tp = event.getPath();
                AdminPanel.this.label.setText("Expansion: " + String.valueOf(tp.getLastPathComponent()));
            }

            public void treeCollapsed(TreeExpansionEvent event) {
                TreePath tp = event.getPath();
                AdminPanel.this.label.setText("Collapse: " + String.valueOf(tp.getLastPathComponent()));
            }
        });
        this.tree.addTreeSelectionListener(new TreeSelectionListener() {
            public void valueChanged(TreeSelectionEvent e) {
                AdminPanel.this.selectedNode = (DefaultMutableTreeNode)AdminPanel.this.tree.getLastSelectedPathComponent();
            }
        });
        this.treePane = new JScrollPane(this.tree);
        this.treePanel.add(this.treePane);
        this.userName = new JTextField(20);
        this.groupName = new JTextField(20);
        this.label = new JLabel("Hello.");
        this.label.setHorizontalAlignment(0);
        this.c = new GridBagConstraints();
        this.c.fill = 1;
        this.c.weightx = 0.7;
        this.c.weighty = 1.0;
        this.c.gridx = 0;
        this.c.gridy = 0;
        this.frame.getContentPane().add(this.treePanel, this.c);
        this.c.gridx = 1;
        this.c.weightx = 0.5;
        this.frame.getContentPane().add(this.buttonPanel, this.c);
        this.c.weighty = 0.2;
        this.c.weightx = 1.0;
        this.c.gridx = 0;
        this.c.gridy = 0;
        this.buttonPanel.add(this.innerTop, this.c);
        this.c.weighty = 0.3;
        this.c.gridy = 2;
        this.buttonPanel.add(this.innerBot, this.c);
        this.c.weighty = 0.6;
        this.c.gridy = 1;
        this.buttonPanel.add(this.innerMid, this.c);
        this.c.insets = new Insets(1, 1, 1, 2);
        this.c.gridx = 0;
        this.c.gridy = 0;
        this.c.weightx = 0.6;
        this.c.weighty = 0.5;
        this.innerTop.add(this.userName, this.c);
        this.innerBot.add(this.userTotal, this.c);
        this.c.gridx = 1;
        this.c.weightx = 0.4;
        this.innerTop.add(this.addUser, this.c);
        this.innerBot.add(this.groupTotal, this.c);
        this.c.gridy = 1;
        this.c.weighty = 0.5;
        this.innerTop.add(this.addGroup, this.c);
        this.innerBot.add(this.positivePercentage, this.c);
        this.c.gridx = 0;
        this.c.weightx = 0.6;
        this.innerTop.add(this.groupName, this.c);
        this.innerBot.add(this.messageTotal, this.c);
        this.c.gridy = 0;
        this.c.weightx = 0.5;
        this.c.weighty = 0.3;
        this.innerMid.add(this.userView, this.c);
        this.c.gridx = 1;
        this.c.weightx = 0.5;
        this.c.weighty = 0.3;
        this.innerMid.add(this.validateUsers, this.c);
        this.c.gridx = 0;
        this.c.gridy = 1;
        this.c.weighty = 0.3;
        this.c.weightx = 0.6;
        this.innerMid.add(this.label, this.c);
        this.c.gridx = 1;
        this.c.weightx = 0.5;
        this.innerMid.add(this.lastUpdated, this.c);
        this.frame.pack();
        this.frame.setVisible(true);
    }

    private static class CustomRenderer extends DefaultTreeCellRenderer {
        private CustomRenderer() {
        }

        public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
            super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
            if (value instanceof DefaultMutableTreeNode n) {
                if (n.getUserObject() instanceof UserGroup) {
                    this.setIcon(UIManager.getIcon("Tree.closedIcon"));
                }
            }

            return this;
        }
    }
}
