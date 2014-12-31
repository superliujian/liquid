package liquid.task.domain;

import java.io.Serializable;

/**
 * TODO: Comments.
 * User: tao
 * Date: 10/18/13
 * Time: 10:24 PM
 */
public class Task implements Serializable {
    private String id;

    private String definitionKey;

    private Long orderId;

    private String orderNo;

    private String name;

    private String description;

    private String assignee;

    private String createDate;

    private String prompt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDefinitionKey() {
        return definitionKey;
    }

    public void setDefinitionKey(String definitionKey) {
        this.definitionKey = definitionKey;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{Class=Task");
        sb.append(", id='").append(id).append('\'');
        sb.append(", definitionKey='").append(definitionKey).append('\'');
        sb.append(", orderId=").append(orderId);
        sb.append(", orderNo='").append(orderNo).append('\'');
        sb.append(", name='").append(name).append('\'');
        sb.append(", description='").append(description).append('\'');
        sb.append(", assignee='").append(assignee).append('\'');
        sb.append(", createDate='").append(createDate).append('\'');
        sb.append(", prompt='").append(prompt).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
