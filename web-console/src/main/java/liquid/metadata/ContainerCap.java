package liquid.metadata;

/**
 * TODO: Comments.
 * User: tao
 * Date: 9/30/13
 * Time: 10:31 PM
 */
public enum ContainerCap {
    FT20(0, "20-ft"),
    FT40(1, "40-ft");

    private final int type;

    private final String name;

    private ContainerCap(int type, String name) {
        this.type = type;
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder(super.toString());
        sb.append("ContainerCap{");
        sb.append("type=").append(type);
        sb.append(", name='").append(name).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
