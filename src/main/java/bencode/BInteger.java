package bencode;

import lombok.EqualsAndHashCode;
import lombok.NonNull;

@EqualsAndHashCode(callSuper = false)
public final class BInteger extends Number implements BValue<Long>, Comparable<BInteger> {

	private static final long serialVersionUID = 4188918941802769935L;

	public static BInteger valueOf(@NonNull Number value) {
		return new BInteger(value.longValue());
	}

	public static BInteger valueOf(@NonNull Long value) {
		return new BInteger(value);
	}

	private final @NonNull Long value;

	private BInteger(Long value) {
		this.value = value;
	}

	@Override
	public Long getValue() {
		return this.value;
	}

	@Override
	public BInteger clone() {
		try {
			return (BInteger) super.clone();
		} catch (CloneNotSupportedException cnse) {
			final BInteger result = valueOf(Long.valueOf(this.value));

			return result;
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