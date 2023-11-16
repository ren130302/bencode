package bencode;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.Value;

@Value(staticConstructor = "valueOf")
@EqualsAndHashCode(callSuper = false)
public final class BInteger extends Number implements BValue<Long>, Comparable<BInteger> {

	private static final long serialVersionUID = 4188918941802769935L;

	public static BInteger valueOf(@NonNull Number value) {
		return BInteger.valueOf(value.longValue());
	}

	private final @NonNull Long value;

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
		final StringBuffer buffer = new StringBuffer();
		return buffer.append(this.getValue()).toString();
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