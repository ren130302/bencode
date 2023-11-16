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

		StringBuffer strBuf = new StringBuffer();

		c = ByteBufferUtils.get(byteBuffer, byteBuffer.position());

		while (c != END && (c == NEGA || Character.isDigit(c))) {
			strBuf.append((char) c);
			c = ByteBufferUtils.get(byteBuffer);
		}

		long number = Long.parseLong(strBuf.toString());

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
