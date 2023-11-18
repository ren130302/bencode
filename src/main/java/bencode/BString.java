package bencode;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Objects;

import lombok.NonNull;
import lombok.Value;

@Value(staticConstructor = "valueOf")
public final class BString implements BValue<Byte[]>, Comparable<BString> {

	private static final long serialVersionUID = 2812347755822710497L;

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

	public static BString valueOf(String value) {
		return valueOf(Objects.requireNonNullElse(value, "").getBytes());
	}

	public static BString valueOf(String value, Charset encording) {
		return valueOf(Objects.requireNonNullElse(value, "").getBytes(encording));
	}

	public static BString valueOf(String value, String encording) throws UnsupportedEncodingException {
		return valueOf(Objects.requireNonNullElse(value, "").getBytes(encording));
	}

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

	@Override
	public String toString() {
		final StringBuffer buffer = new StringBuffer();

		if (Arrays.asList(this.getValue()).stream().anyMatch(v -> v < 0)) {
			buffer.append('[');
			buffer.append(
					String.join(",", Arrays.asList(this.getValue()).stream().map(v -> Byte.toString(v)).toList()));
			buffer.append(']');
		} else {
			buffer.append('\"');
			buffer.append(this.getString());
			buffer.append('\"');
		}
		return buffer.toString();
	}

	@Override
	public int compareTo(BString that) {
		final int thisLength = this.value.length;
		final int thatLength = that.value.length;

		if (thisLength == thatLength) {
			for (int index = 0; index < thisLength && index < thatLength; index++) {
				int sum = Byte.toUnsignedInt(this.value[index]) - Byte.toUnsignedInt(that.value[index]);

				if (sum != 0) {
					return sum;
				}
			}
		}

		return thisLength - thatLength;
	}

	public byte[] get() {
		return autoboxing(this.value);
	}

	public String getString() {
		return new String(this.get());
	}

	public String getString(Charset charset) {
		return new String(this.get(), charset);
	}

	public byte[] getLength(Charset charset) {
		return Integer.toString(this.value.length).getBytes(charset);
	}

}