package bencode.parser;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.regex.Pattern;

import bencode.BString;
import bencode.BValueCharacter;
import lombok.RequiredArgsConstructor;
import lombok.Value;

@RequiredArgsConstructor
@Value
public final class BStringParser implements BValueParser<BString> {

	public static final Pattern PATTERN = Pattern.compile("(?s)(?m)^(?<length>\\d+):(?<text>.*)$");

	private final Charset charset;

	public BStringParser() {
		this(Charset.defaultCharset());
	}

	@Override
	public ByteBuffer writeToByteBuffer(BString value) throws IOException {
		final StringBuffer buffer = new StringBuffer();

		buffer.append(value.getValue().length);
		buffer.append(BValueCharacter.CORON);
		buffer.append(value.getString());

		return ByteBuffer.wrap(buffer.toString().getBytes(this.getCharset()));
	}

	@Override
	public BString readFromByteBuffer(ByteBuffer byteBuffer) throws IOException {
		int from = byteBuffer.position();

		int c = BValueParser.get(byteBuffer);

		while (c != BValueCharacter.CORON) {
			if (!byteBuffer.hasRemaining()) {
				throw new IllegalArgumentException("Colon expected, not '" + (char) c + "'");
			}
			c = BValueParser.get(byteBuffer);
		}
		int to = byteBuffer.position() - 1;
		int length = Integer.parseInt(new String(this.slice(byteBuffer, from, to), this.getCharset()));

		if (length < 0) {
			throw new IllegalArgumentException("Length expected, " + length + "");
		}

		from = byteBuffer.position();
		to = from + length;

		if (byteBuffer.capacity() <= to) {
			to = byteBuffer.capacity();
		}

		byteBuffer.position(to);

		final BString result = BString.valueOf(this.slice(byteBuffer, from, to));

		return result;
	}

	private byte[] slice(ByteBuffer byteBuffer, int from, int to) {
		return Arrays.copyOfRange(byteBuffer.array(), from, to);
	}

}
