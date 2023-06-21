public class PositiveCountVisitor implements TwitterElementVisitor {
    private int total;
    private int count;

    public PositiveCountVisitor() {
        this.setTotal(0);
        this.setCount(0);
    }

    public int getTotal() {
        return this.total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getCount() {
        return this.count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public double getPercentage() {
        return (double)this.count / (double)this.total;
    }

    public void visitTwitterUser(TwitterUser u) {
        Object[] var2 = u.getNewsfeed().toArray();
        int var3 = var2.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            Object o = var2[var4];
            if (o.toString().toLowerCase().contains("good") || o.toString().toLowerCase().contains("excellent")) {
                this.setCount(this.getCount() + 1);
            }

            this.setTotal(this.getTotal() + 1);
        }

    }

    public void visitGroup(UserGroup g) {
    }
}
