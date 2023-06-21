public class MessageCountVisitor implements TwitterElementVisitor {
    private int counter = 0;

    public MessageCountVisitor() {
        this.setCounter(0);
    }

    public int getCounter() {
        return this.counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    public void visitTwitterUser(TwitterUser u) {
        this.setCounter(this.getCounter() + u.getNewsfeed().size());
    }

    public void visitGroup(UserGroup g) {
    }
}
