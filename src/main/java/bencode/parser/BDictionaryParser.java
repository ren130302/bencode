package bencode.parser;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.regex.Pattern;

import bencode.BDictionary;
import bencode.BString;
import bencode.BValue;
import lombok.Value;

@Value
public final class BDictionaryParser implements IBValueParser<BDictionary> {

	public static final Pattern PATTERN = Pattern.compile("(?sm)^d(?<entries>.*)e$");

	private final BValueParsers parsers;

	@Override
	public BDictionary readFromByteBuffer(ByteBuffer byteBuffer) throws IOException {
		int c = ByteBufferUtils.get(byteBuffer);

		if (c != DICTIONARY) {
			throw new IllegalArgumentException("Expected 'd', not '" + (char) c + "'");
		}

		final Map<String, BValue<?>> map = new TreeMap<>();

		BString key = null;
		BValue<?> value = null;

		c = ByteBufferUtils.get(byteBuffer, byteBuffer.position());

		while (c != END) {
			if (!byteBuffer.hasRemaining()) {
				throw new IllegalArgumentException("Expected 'e', not '" + (char) c + "'");
			}

			key = this.parsers.getBStringParser().readFromByteBuffer(byteBuffer);
			value = this.parsers.getBValueParser().readFromByteBuffer(byteBuffer);

			map.put(key.getString(), value);

			c = ByteBufferUtils.get(byteBuffer, byteBuffer.position());
		}

		return BDictionary.create(map);
	}

	@Override
	public ByteBuffer writeToByteBuffer(BDictionary value) throws IOException {
		final StringBuffer buffer = new StringBuffer();

		buffer.append(DICTIONARY);

		for (Entry<String, BValue<?>> entry : value.entrySet()) {
			buffer.append(this.parsers.getBStringParser().writeToString(BString.valueOf(entry.getKey())));
			buffer.append(this.parsers.getBValueParser().writeToString(entry.getValue()));
		}

		buffer.append(END);

		return ByteBuffer.wrap(buffer.toString().getBytes(this.getCharset()));
	}

}
