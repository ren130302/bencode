package bencode.parser;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.regex.Pattern;

import bencode.BInteger;
import lombok.Value;

@Value
public final class BIntegerParser implements IBValueParser<BInteger> {

	public static final Pattern PATTERN = Pattern.compile("(?sm)^i(?<number>\\d+)e$");

	private final BValueParsers parsers;

	@Override
	public BInteger readFromByteBuffer(ByteBuffer byteBuffer) throws IOException {
		int c = ByteBufferUtils.get(byteBuffer);

		if (c != INTEGER) {
			throw new IllegalArgumentException("Expected 'i', not '" + (char) c + "'");
		}

		int from = byteBuffer.position();

		c = ByteBufferUtils.get(byteBuffer);

		while (c != END) {
			if (!byteBuffer.hasRemaining()) {
				throw new IllegalArgumentException("Expected 'e', not '" + (char) c + "'");
			}
			c = ByteBufferUtils.get(byteBuffer);
		}
		int to = byteBuffer.position() - 1;
		int number = Integer.parseInt(new String(ByteBufferUtils.slice(byteBuffer, from, to), this.getCharset()));

		return BInteger.valueOf(number);
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
