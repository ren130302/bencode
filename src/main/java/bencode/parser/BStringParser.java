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
		StringBuffer strBuf = new StringBuffer();

		int c = ByteBufferUtils.get(byteBuffer);
		while (c != CORON) {
			if (Character.isDigit(c)) {
				strBuf.append((char) c);

			}
			c = ByteBufferUtils.get(byteBuffer);
		}

		int length = Integer.parseInt(strBuf.toString());

		if (length < 0) {
			length = 0;
		}

		final byte[] byteData = new byte[length];
		byteBuffer.get(byteData);

		return BString.valueOf(byteData);
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
