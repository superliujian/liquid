package liquid.process.web.domain;

/**
 * Created by Tao Ma on 12/31/14.
 */
public class TaskForm {
    private String definitionKey;
    private Long orderId;

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

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{Class=TaskForm");
        sb.append(", definitionKey='").append(definitionKey).append('\'');
        sb.append(", orderId=").append(orderId);
        sb.append('}');
        return sb.toString();
    }
}
