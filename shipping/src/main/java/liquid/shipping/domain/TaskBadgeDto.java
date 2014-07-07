package liquid.shipping.domain;

import java.io.Serializable;

/**
 * TODO: Comments.
 * User: tao
 * Date: 10/26/13
 * Time: 2:58 PM
 */
public class TaskBadgeDto implements Serializable {
    private int queueSize;

    private int myTasksQty;

    private int warningQty;

    public int getQueueSize() {
        return queueSize;
    }

    public void setQueueSize(int queueSize) {
        this.queueSize = queueSize;
    }

    public int getMyTasksQty() {
        return myTasksQty;
    }

    public void setMyTasksQty(int myTasksQty) {
        this.myTasksQty = myTasksQty;
    }

    public int getWarningQty() {
        return warningQty;
    }

    public void setWarningQty(int warningQty) {
        this.warningQty = warningQty;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder(super.toString());
        sb.append("TaskBadgeDto{");
        sb.append("queueSize=").append(queueSize);
        sb.append(", myTasksQty=").append(myTasksQty);
        sb.append(", warningQty=").append(warningQty);
        sb.append('}');
        return sb.toString();
    }
}
