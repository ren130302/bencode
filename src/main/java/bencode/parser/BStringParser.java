package bencode.parser;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.regex.Pattern;

import bencode.BString;
import lombok.Value;

@Value
public final class BStringParser implements IBValueParser<BString> {

	public static final Pattern PATTERN = Pattern.compile("(?sm)^(?<length>\\d+):(?<text>.*)$");

	private final BValueParsers parsers;

	@Override
	public BString readFromByteBuffer(ByteBuffer byteBuffer) throws IOException {
		int from = byteBuffer.position();

		int c = ByteBufferUtils.get(byteBuffer);

		while (c != CORON) {
			if (!byteBuffer.hasRemaining()) {
				throw new IllegalArgumentException("Expected ':', not '" + (char) c + "'");
			}
			c = ByteBufferUtils.get(byteBuffer);
		}
		int to = byteBuffer.position() - 1;
		int length = Integer.parseInt(new String(ByteBufferUtils.slice(byteBuffer, from, to), this.getCharset()));

		if (length < 0) {
			throw new IllegalArgumentException("Expected negetive value, not " + length + "");
		}

		from = byteBuffer.position();
		to = from + length;

		if (byteBuffer.capacity() <= to) {
			to = byteBuffer.capacity();
		}

		byteBuffer.position(to);

		return BString.valueOf(ByteBufferUtils.slice(byteBuffer, from, to));
	}

	@Override
	public ByteBuffer writeToByteBuffer(BString value) throws IOException {
		final StringBuffer buffer = new StringBuffer();

		buffer.append(value.getValue().length);
		buffer.append(CORON);
		buffer.append(value.getString(this.getCharset()));

		return ByteBuffer.wrap(buffer.toString().getBytes(this.getCharset()));
	}

}
