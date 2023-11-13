package bencode.parser;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.regex.Pattern;

import bencode.BString;
import lombok.RequiredArgsConstructor;
import lombok.Value;

@RequiredArgsConstructor
@Value
public final class BStringParser implements IBValueParser<BString> {

	public static final Pattern PATTERN = Pattern.compile("(?sm)^(?<length>\\d+):(?<text>.*)$");

	private final Charset charset;

	public BStringParser() {
		this(Charset.defaultCharset());
	}

	@Override
	public ByteBuffer writeToByteBuffer(BString value) throws IOException {
		final StringBuffer buffer = new StringBuffer();

		buffer.append(value.getValue().length);
		buffer.append(CORON);
		buffer.append(value.getString());

		return ByteBuffer.wrap(buffer.toString().getBytes(this.getCharset()));
	}

	@Override
	public BString readFromByteBuffer(ByteBuffer byteBuffer) throws IOException {
		int from = byteBuffer.position();

		int c = IBValueParser.get(byteBuffer);

		while (c != CORON) {
			if (!byteBuffer.hasRemaining()) {
				throw new IllegalArgumentException("Expected ':', not '" + (char) c + "'");
			}
			c = IBValueParser.get(byteBuffer);
		}
		int to = byteBuffer.position() - 1;
		int length = Integer.parseInt(new String(this.slice(byteBuffer, from, to), this.getCharset()));

		if (length < 0) {
			throw new IllegalArgumentException("Expected negetive value, not " + length + "");
		}

		from = byteBuffer.position();
		to = from + length;

		if (byteBuffer.capacity() <= to) {
			to = byteBuffer.capacity();
		}

		byteBuffer.position(to);

		return BString.valueOf(this.slice(byteBuffer, from, to));
	}

	private byte[] slice(ByteBuffer byteBuffer, int from, int to) {
		return Arrays.copyOfRange(byteBuffer.array(), from, to);
	}

}
