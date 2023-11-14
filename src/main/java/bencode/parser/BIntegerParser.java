package bencode.parser;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.regex.Pattern;

import bencode.BInteger;
import lombok.RequiredArgsConstructor;
import lombok.Value;

@RequiredArgsConstructor
@Value
public final class BIntegerParser implements IBValueParser<BInteger> {

	public static final Pattern PATTERN = Pattern.compile("(?sm)^i(?<number>\\d+)e$");

	private final Charset charset;

	public BIntegerParser() {
		this(Charset.defaultCharset());
	}

	@Override
	public BInteger readFromByteBuffer(ByteBuffer byteBuffer) throws IOException {
		int c = ParseUtils.get(byteBuffer);

		if (c != INTEGER) {
			throw new IllegalArgumentException("Expected 'i', not '" + (char) c + "'");
		}

		int from = byteBuffer.position();

		c = ParseUtils.get(byteBuffer);

		while (c != END) {
			if (!byteBuffer.hasRemaining()) {
				throw new IllegalArgumentException("Expected 'e', not '" + (char) c + "'");
			}
			c = ParseUtils.get(byteBuffer);
		}
		int to = byteBuffer.position() - 1;
		int number = Integer.parseInt(new String(this.slice(byteBuffer, from, to), this.getCharset()));

		return BInteger.valueOf(number);
	}

	private byte[] slice(ByteBuffer byteBuffer, int from, int to) {
		return Arrays.copyOfRange(byteBuffer.array(), from, to);
	}

	@Override
	public ByteBuffer writeToByteBuffer(BInteger value) throws IOException {
		final StringBuffer buffer = new StringBuffer();

		buffer.append(INTEGER);
		buffer.append(value.longValue());
		buffer.append(END);

		return ByteBuffer.wrap(buffer.toString().getBytes(this.getCharset()));
	}
}
