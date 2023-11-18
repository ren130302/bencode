package bencode.parser;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.regex.Pattern;

import bencode.BList;
import bencode.BValue;
import lombok.Value;

@Value
public final class BListParser implements IBValueParser<BList<?>> {

	public static final Pattern PATTERN = Pattern.compile("(?sm)^l(?<items>.*)e$");

	private final BValueParsers parsers;

	@Override
	public BList<?> readFromByteBuffer(ByteBuffer byteBuffer) throws IOException {
		int c = ByteBufferUtils.get(byteBuffer);

		if (c != LIST) {
			throw new IllegalArgumentException("Expected 'l', not '" + (char) c + "'");
		}

		final BList<BValue<?>> result = BList.create();

		BValue<?> value = null;

		c = ByteBufferUtils.get(byteBuffer, byteBuffer.position());

		while (c != END) {
			value = this.parsers.getBValueParser().readFromByteBuffer(byteBuffer);
			result.add(value);

			c = ByteBufferUtils.get(byteBuffer, byteBuffer.position());
		}
		// drop 'e'
		c = byteBuffer.get();

		if (c != END) {
			throw new IllegalArgumentException("Expected 'e', not '" + (char) c + "'");
		}

		return result;
	}

	@Override
	public String writeToString(BList<?> value) throws IOException {
		final StringBuffer buffer = new StringBuffer();
		buffer.append(LIST);

		for (BValue<?> v : value.getValue()) {
			buffer.append(this.parsers.getBValueParser().writeToString(v));
		}

		buffer.append(END);

		return buffer.toString();
	}

}
