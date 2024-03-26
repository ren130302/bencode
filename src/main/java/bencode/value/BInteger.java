package bencode.value;

import java.util.Objects;

public final class BInteger extends Number implements BValue<Long>, Comparable<BInteger> {

    private static final long serialVersionUID = 4188918941802769935L;

    public static BInteger valueOf(Number value) {
        return new BInteger(value.longValue());
    }

    public static BInteger valueOf(Long value) {
        return new BInteger(value);
    }

    private final Long value;

    private BInteger(Long value) {
        this.value = value;
    }

    @Override
    public Long getValue() {
        return this.value;
    }

    @Override
    public boolean equals(Object obj) {
        if (Objects.isNull(obj)) {
            return false;
        }
        if (obj instanceof Long v) {
            return this.getValue().equals(v);
        }
        if (obj instanceof BInteger v) {
            return this.getValue().equals(v.getValue());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.getValue().hashCode();
    }

    @Override
    public BInteger clone() {
        try {
            return (BInteger) super.clone();
        } catch (CloneNotSupportedException cnse) {
            return new BInteger(Long.valueOf(this.value));
        }
    }

    @Override
    public String toString() {
        return Long.toString(this.getValue());
    }

    @Override
    public int compareTo(BInteger that) {
        return this.getValue().compareTo(that.getValue());
    }

    @Override
    public int intValue() {
        return this.getValue().intValue();
    }

    @Override
    public long longValue() {
        return this.getValue().longValue();
    }

    @Override
    public float floatValue() {
        return this.getValue().floatValue();
    }

    @Override
    public double doubleValue() {
        return this.getValue().doubleValue();
    }

}
