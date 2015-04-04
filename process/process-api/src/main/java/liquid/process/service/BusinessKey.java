package liquid.process.service;

import java.io.Serializable;

/**
 * Created by Tao Ma on 12/22/14.
 */
public final class BusinessKey implements Serializable {
    private static final String SEPARATOR = ":";

    private Long orderId;
    private String orderNo;
    private String text;

    private BusinessKey(String text) {
        this.text = text;
    }

    private BusinessKey(Long orderId, String orderNo) {
        this.orderId = orderId;
        this.orderNo = orderNo;
    }

    private void decode() {
        if (null == text) throw new NullPointerException();
        String[] fields = text.split(SEPARATOR);
        if (fields.length < 2) throw new IllegalArgumentException();
        orderId = Long.parseLong(fields[0]);
        orderNo = fields[1];
    }

    private void encode() {
        text = orderId + SEPARATOR + orderNo;
    }

    public static BusinessKey decode(String text) {
        BusinessKey businessKey = new BusinessKey(text);
        businessKey.decode();
        return businessKey;
    }

    public static BusinessKey encode(Long orderId, String orderNo) {
        BusinessKey businessKey = new BusinessKey(orderId, orderNo);
        businessKey.encode();
        return businessKey;
    }

    public Long getOrderId() {
        return orderId;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{Class=BusinessKey");
        sb.append(", orderId=").append(orderId);
        sb.append(", orderNo='").append(orderNo).append('\'');
        sb.append(", text='").append(text).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
