public class GroupCountVisitor implements TwitterElementVisitor {
    private int counter;

    public GroupCountVisitor() {
        this.setCounter(0);
    }

    public int getCounter() {
        return this.counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    public void visitTwitterUser(TwitterUser u) {
    }

    public void visitGroup(UserGroup g) {
        this.setCounter(this.getCounter() + 1);
    }
}
