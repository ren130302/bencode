package bencode;

import lombok.NonNull;
import lombok.Value;

@Value(staticConstructor = "valueOf")
public final class BInteger implements BValue<Long>, Comparable<BInteger> {

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
	public int compareTo(BInteger that) {
		return this.getValue().compareTo(that.getValue());
	}

	public int getInt() {
		return this.getValue().intValue();
	}

	public long getLong() {
		return this.getValue().longValue();
	}

	public short getShort() {
		return this.getValue().shortValue();
	}

	@Override
	public BValueType getType() {
		return BValueType.INTEGER;
	}

	@Override
	public String toString() {
		final StringBuffer buffer = new StringBuffer();
		return buffer.append(this.getValue()).toString();
	}

}