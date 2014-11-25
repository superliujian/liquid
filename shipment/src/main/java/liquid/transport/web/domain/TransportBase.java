package liquid.transport.web.domain;

/**
 * Created by redbrick9 on 9/13/14.
 */
abstract public class TransportBase {
    private String source;
    private String destination;

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }
}
