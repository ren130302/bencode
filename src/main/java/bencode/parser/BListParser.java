package bencode.parser;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.regex.Pattern;

import bencode.BList;
import bencode.BValue;
import lombok.Value;

@Value
public final class BListParser implements IBValueParser<BList> {

	public static final Pattern PATTERN = Pattern.compile("(?sm)^l(?<items>.*)e$");

	private final BValueParsers parsers;

	@Override
	public BList readFromByteBuffer(ByteBuffer byteBuffer) throws IOException {
		int c = ByteBufferUtils.get(byteBuffer);

		if (c != LIST) {
			throw new IllegalArgumentException("Expected 'l', not '" + (char) c + "'");
		}

		final BList result = BList.create();

		BValue<?> value = null;

		c = ByteBufferUtils.get(byteBuffer, byteBuffer.position());

		while (c != END) {
			value = this.parsers.getBValueParser().readFromByteBuffer(byteBuffer);
			result.add(value);

			c = ByteBufferUtils.get(byteBuffer, byteBuffer.position());
		}

		return result;
	}

	@Override
	public ByteBuffer writeToByteBuffer(BList value) throws IOException {
		final StringBuffer buffer = new StringBuffer();
		buffer.append(LIST);

		for (BValue<?> v : value.getValue()) {
			buffer.append(this.parsers.getBValueParser().writeToString(v));
		}

		buffer.append(END);

		return ByteBuffer.wrap(buffer.toString().getBytes(this.getCharset()));
	}

}
