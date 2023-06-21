import java.util.Iterator;
import java.util.Vector;

public class UserGroup implements TwitterElement {
    private String groupID;
    private Vector<TwitterElement> children;

    public UserGroup(String groupID) {
        this.setGroupID(groupID);
        this.children = new Vector();
    }

    public String getGroupID() {
        return this.groupID;
    }

    public void setGroupID(String groupID) {
        this.groupID = groupID;
    }

    public void add(TwitterElement element) {
        if (!this.children.contains(element)) {
            this.children.add(element);
        }

    }

    public String toString() {
        return this.groupID;
    }

    public void accept(TwitterElementVisitor v) {
        v.visitGroup(this);
        Iterator var2 = this.children.iterator();

        while(var2.hasNext()) {
            TwitterElement element = (TwitterElement)var2.next();
            element.accept(v);
        }

    }
}
