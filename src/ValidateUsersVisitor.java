import java.util.Vector;

public class ValidateUsersVisitor implements TwitterElementVisitor {
    Vector<String> invalids = new Vector();

    public ValidateUsersVisitor() {
    }

    public void visitTwitterUser(TwitterUser u) {
        if (u.getID().contains(" ") || u.getID().contains("\t")) {
            this.invalids.add(u.getID());
        }

    }

    public void visitGroup(UserGroup g) {
        if (g.getGroupID().contains(" ") || g.getGroupID().contains("\t")) {
            this.invalids.add(g.getGroupID());
        }

    }

    public Vector<String> results() {
        if (this.invalids.isEmpty()) {
            this.invalids.add("No invalid users found.");
        } else {
            this.invalids.add(0, "List of invalid users: ");
        }

        return this.invalids;
    }
}
