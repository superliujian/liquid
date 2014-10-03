package liquid.domain;

/**
 * Created by redbrick9 on 5/8/14.
 */
public enum Currency {
    CNY(0, "CNY"), USD(1, "USD");

    private final int value;

    private final String name;

    Currency(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public static Currency valueOf(int type) {
        switch (type) {
            case 0:
                return CNY;
            case 1:
                return USD;
            default:
                throw new IllegalArgumentException(String.format("%s should be from %s and %s.", type, 0, 1));
        }
    }
}
