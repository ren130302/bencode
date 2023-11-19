package bencode.parser;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Map.Entry;
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

		final BDictionary result = BDictionary.create();

		BString key = null;
		BValue<?> value = null;

		c = ByteBufferUtils.get(byteBuffer, byteBuffer.position());

		while (c != END) {
			key = this.parsers.getBStringParser().readFromByteBuffer(byteBuffer);
			value = this.parsers.getBValueParser().readFromByteBuffer(byteBuffer);
			result.put(key, value);

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
	public ByteBuffer writeToByteBuffer(BDictionary value) throws IOException {
		try (ByteArrayOutputStream stream = new ByteArrayOutputStream()) {
			stream.write(DICTIONARY);

			for (Entry<BString, BValue<?>> entry : value.entrySet()) {
				stream.write(this.parsers.getBStringParser().writeToBytes(entry.getKey()));
				stream.write(this.parsers.getBValueParser().writeToBytes(entry.getValue()));
			}

			stream.write(END);

			return ByteBuffer.wrap(stream.toByteArray());
		}
	}

}
