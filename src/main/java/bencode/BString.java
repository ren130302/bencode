package bencode;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Objects;

import lombok.NonNull;
import lombok.Value;

@Value(staticConstructor = "valueOf")
public final class BString implements IBValue<Byte[]>, Comparable<BString> {

	private static final long serialVersionUID = 2812347755822710497L;
	private final @NonNull Byte[] value;

	@Override
	public BString clone() {
		try {
			return (BString) super.clone();
		} catch (CloneNotSupportedException cnse) {
			final BString result = valueOf(this.getValue().clone());

			return result;
		}
	}

	private static byte[] autoboxing(Byte[] value) {
		final byte[] result = new byte[value.length];

		for (int i = 0; i < value.length; i++) {
			result[i] = value[i];
		}

		return result;
	}

	private static Byte[] unboxing(byte[] value) {
		final Byte[] result = new Byte[value.length];

		for (int i = 0; i < value.length; i++) {
			result[i] = value[i];
		}

		return result;
	}

	public static BString valueOf(byte[] value) {
		return valueOf(unboxing(value));
	}

	public static BString valueOf(String value, Charset encording) {
		return valueOf(unboxing(Objects.requireNonNullElse(value, "").getBytes(encording)));
	}

	public static BString valueOf(String value, String encording) throws UnsupportedEncodingException {
		return valueOf(unboxing(Objects.requireNonNullElse(value, "").getBytes(encording)));
	}

	public static BString valueOf(String value) {
		return valueOf(unboxing(Objects.requireNonNullElse(value, "").getBytes()));
	}

	public String getString() {
		return new String(autoboxing(this.getValue()));
	}

	public String getString(Charset charset) {
		return new String(autoboxing(this.getValue()), charset);
	}

	@Override
	public String toString() {
		final StringBuffer buffer = new StringBuffer();

		buffer.append('\"');
		buffer.append(this.getString());
		buffer.append('\"');

		return buffer.toString();
	}

	@Override
	public BValueType getType() {
		return BValueType.STRING;
	}

	@Override
	public int compareTo(BString that) {
		final int thisLength = this.getValue().length;
		final int thatLength = that.getValue().length;

		if (thisLength == thatLength) {
			for (int index = 0; index < thisLength && index < thatLength; index++) {
				int sum = Byte.toUnsignedInt(this.getValue()[index]) - Byte.toUnsignedInt(that.getValue()[index]);

				if (sum != 0) {
					return sum;
				}
			}
		}

		return thisLength - thatLength;
	}

}