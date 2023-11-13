package bencode;

import lombok.NonNull;
import lombok.Value;

@Value(staticConstructor = "valueOf")
public final class BInteger implements BValue<Long>, Comparable<BInteger> {

	private static final long serialVersionUID = 4188918941802769935L;
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

	public static BInteger valueOf(@NonNull Number value) {
		return BInteger.valueOf(value.longValue());
	}

	public short getShort() {
		return this.getValue().shortValue();
	}

	public int getInt() {
		return this.getValue().intValue();
	}

	public long getLong() {
		return this.getValue().longValue();
	}

	@Override
	public String toString() {
		final StringBuffer buffer = new StringBuffer();
		return buffer.append(this.getValue()).toString();
	}

	@Override
	public BValueType getType() {
		return BValueType.INTEGER;
	}

	@Override
	public int compareTo(BInteger that) {
		return this.getValue().compareTo(that.getValue());
	}
}