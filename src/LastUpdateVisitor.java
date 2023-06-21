
public class LastUpdateVisitor implements TwitterElementVisitor {
    String id;
    long updateTime;

    public LastUpdateVisitor() {
        this.setId((String)null);
        this.setUpdateTime(0L);
    }

    public void visitTwitterUser(TwitterUser u) {
        if (u.getLastUpdateTime() > this.getUpdateTime()) {
            this.setUpdateTime(u.getLastUpdateTime());
            this.setId(u.getID());
        }

    }

    public void visitGroup(UserGroup g) {
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getUpdateTime() {
        return this.updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }
}
