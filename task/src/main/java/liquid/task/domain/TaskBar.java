package liquid.task.domain;

import java.io.Serializable;

/**
 * TODO: Comments.
 * User: tao
 * Date: 10/26/13
 * Time: 2:58 PM
 */
public class TaskBar implements Serializable {
    private int taskQueueSize;

    private int myTasksSize;

    private int warningsSize;

    private String title;

    public int getTaskQueueSize() {
        return taskQueueSize;
    }

    public void setTaskQueueSize(int taskQueueSize) {
        this.taskQueueSize = taskQueueSize;
    }

    public int getMyTasksSize() {
        return myTasksSize;
    }

    public void setMyTasksSize(int myTasksSize) {
        this.myTasksSize = myTasksSize;
    }

    public int getWarningsSize() {
        return warningsSize;
    }

    public void setWarningsSize(int warningsSize) {
        this.warningsSize = warningsSize;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("TaskBar{");
        sb.append("taskQueueSize=").append(taskQueueSize);
        sb.append(", myTasksSize=").append(myTasksSize);
        sb.append(", warningsSize=").append(warningsSize);
        sb.append(", title='").append(title).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
