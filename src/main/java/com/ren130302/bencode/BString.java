package com.ren130302.bencode;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;

import javax.annotation.Nonnull;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import com.google.common.primitives.Bytes;

import lombok.Value;

@Value(staticConstructor = "valueOf")
public final class BString implements BValue {

	private static final long serialVersionUID = 2812347755822710497L;
	private final @NonNull byte[] bytes;

	@Override
	public BString clone() {
		try {
			return (BString) super.clone();
		} catch (CloneNotSupportedException cnse) {
			final BString result = valueOf(this.getBytes());

			return result;
		}
	}

	public static final Pattern PATTERN = Pattern.compile("(?s)(?m)^(?<length>\\d+):(?<text>.*)$");

	public static BString valueOf(@Nullable String value) {
		return valueOf(Strings.nullToEmpty(value).getBytes());
	}

	public String getString() {
		return new String(this.getBytes());
	}

	public String getString(Charset charset) {
		return new String(this.getBytes(), charset);
	}

	public static String print(final @NonNull BString value) {
		final StringBuffer buffer = new StringBuffer();
		final StringBuffer bytes = new StringBuffer();
		final List<Byte> list = Bytes.asList(value.getBytes());
		final boolean hasNegativeValue = list.stream().anyMatch(v -> v < 0);

		if (hasNegativeValue) {
			bytes.append('<');
			bytes.append(Joiner.on(", ").join(list));
			bytes.append('>');
		} else {
			bytes.append('\'');
			bytes.append(value.getString());
			bytes.append('\'');
		}

		return buffer.append("'string':").append(bytes).toString();
	}

	public static String writeToString(final @NonNull BString value) {
		final StringBuffer buffer = new StringBuffer();

		buffer.append(value.getBytes().length);
		buffer.append(BValue.CORON);
		buffer.append(value.getString());

		return buffer.toString();
	}

	public static byte[] writeToBytes(final @NonNull BString value) {
		return writeToString(value).getBytes();
	}

	public static ByteBuffer writeToByteBuffer(final @NonNull BString value) {
		return ByteBuffer.wrap(writeToBytes(value));
	}

	public static BString readFromByteBuffer(final @Nonnull ByteBuffer data) throws IOException {
		return readFromByteBuffer(data, new AtomicInteger());
	}

	public static BString readFromByteBuffer(final @Nonnull ByteBuffer byteBuffer,
			final @NonNull AtomicInteger indicator) throws IOException {
		int c = BValueUtils.getNextIndicator(byteBuffer, indicator);
		int length = c - ZERO;

		if (length < 0 || 9 < length) {
			throw new IllegalArgumentException("Number expected, not '" + (char) c + "'");
		}

		indicator.set(0);

		c = BValueUtils.read(byteBuffer);
		int i = c - ZERO;

		while (length <= Integer.MAX_VALUE && 0 <= i && i <= 9) {
			length = length * 10 + i;
			c = BValueUtils.read(byteBuffer);
			i = c - ZERO;
		}

		if (c != CORON) {
			throw new IllegalArgumentException("Colon expected, not '" + (char) c + "'");
		}

		int from = byteBuffer.position();
		int to = from + length;

		if (byteBuffer.capacity() <= to) {
			to = byteBuffer.capacity();
		}

		byteBuffer.position(to);

		final byte[] arrays = Arrays.copyOfRange(byteBuffer.array(), from, to);
		final BString result = valueOf(arrays);

		return result;
	}

	public static BString readFromBytes(final @Nonnull byte[] data) throws IOException {
		return readFromByteBuffer(ByteBuffer.wrap(data));
	}

	public static BString readFromString(final @Nonnull String data) throws IOException {
		return readFromBytes(data.getBytes());
	}
}