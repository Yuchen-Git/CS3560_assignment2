//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

public class UserCountVisitor implements TwitterElementVisitor {
    private int counter = 0;

    public UserCountVisitor() {
        this.setCounter(0);
    }

    public int getCounter() {
        return this.counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    public void visitTwitterUser(TwitterUser u) {
        this.setCounter(this.getCounter() + 1);
    }

    public void visitGroup(UserGroup g) {
    }
}
