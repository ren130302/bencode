package com.ren130302.bencode;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;

import javax.annotation.Nonnull;

import org.checkerframework.checker.nullness.qual.NonNull;

import com.google.common.collect.Lists;
import com.google.common.primitives.Chars;

import lombok.Value;

@Value(staticConstructor = "valueOf")
public final class BInteger implements BValue {

	private static final long serialVersionUID = 4188918941802769935L;
	private final @NonNull long number;

	@Override
	public BInteger clone() {
		try {
			return (BInteger) super.clone();
		} catch (CloneNotSupportedException cnse) {
			final BInteger result = valueOf(this.getLong());

			return result;
		}
	}

	public static final Pattern PATTERN = Pattern.compile("(?s)(?m)^i(?<number>\\d+)e$");

	public static BInteger valueOf(@Nonnull Number value) {
		return BInteger.valueOf(value.longValue());
	}

	public short getShort() {
		return (short) this.getNumber();
	}

	public int getInt() {
		return (int) this.getNumber();
	}

	public long getLong() {
		return this.getNumber();
	}

	public static String print(final @Nonnull BInteger value) {
		final StringBuffer buffer = new StringBuffer();
		return buffer.append("'integer':").append(value.getLong()).toString();
	}

	public static String writeToString(final @Nonnull BInteger value) {
		final StringBuffer buffer = new StringBuffer();

		buffer.append(BValue.INTEGER);
		buffer.append(value.getLong());
		buffer.append(BValue.END);

		return buffer.toString();
	}

	public static byte[] writeToBytes(final @Nonnull BInteger value) {
		return writeToString(value).getBytes();
	}

	public static ByteBuffer writeToByteBuffer(final @Nonnull BInteger value) {
		return ByteBuffer.wrap(writeToBytes(value));
	}

	public static BInteger readFromByteBuffer(final @Nonnull ByteBuffer data) throws IOException {
		return readFromByteBuffer(data, new AtomicInteger());
	}

	public static BInteger readFromByteBuffer(final @Nonnull ByteBuffer byteBuffer,
			final @Nonnull AtomicInteger indicator) throws IOException {
		int c = BValueUtils.getNextIndicator(byteBuffer, indicator);

		if (c != INTEGER) {
			throw new IllegalArgumentException("Expected 'i', not '" + (char) c + "'");
		}

		indicator.set(0);

		c = BValueUtils.read(byteBuffer);
		if (c == ZERO) {
			c = BValueUtils.read(byteBuffer);

			if (c == END) {
				return BInteger.valueOf(BigInteger.ZERO);
			}
			throw new IllegalArgumentException("'e' expected after zero," + " not '" + (char) c + "'");
		}

		// We don't support more the 255 char big integers
		final List<Character> chars = Lists.newArrayList();

		if (c == NEGA) {
			c = BValueUtils.read(byteBuffer);
			if (c == ZERO) {
				throw new IllegalArgumentException("Negative zero not allowed");
			}

			chars.add(NEGA);
		}

		if (c < ONE || c > NINE) {
			throw new IllegalArgumentException("Invalid Integer start '" + (char) c + "'");
		}

		chars.add((char) c);

		c = BValueUtils.read(byteBuffer);
		int i = c - ZERO;

		while (i >= 0 && i <= 9) {
			chars.add((char) c);
			c = BValueUtils.read(byteBuffer);
			i = c - ZERO;
		}

		if (c != END) {
			throw new IllegalArgumentException("Integer should end with 'e'");
		}

		final BigInteger number = new BigInteger(new String(Chars.toArray(chars)));
		final BInteger result = BInteger.valueOf(number);

		return result;

	}

	public static BInteger readFromBytes(final @Nonnull byte[] data) throws IOException {
		return readFromByteBuffer(ByteBuffer.wrap(data));
	}

	public static BInteger readFromString(final @Nonnull String data) throws IOException {
		return readFromBytes(data.getBytes());
	}
}