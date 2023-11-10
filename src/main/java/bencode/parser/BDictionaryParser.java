package bencode.parser;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.regex.Pattern;

import bencode.BDictionary;
import bencode.BString;
import bencode.BValue;
import bencode.BValueCharacter;
import lombok.RequiredArgsConstructor;
import lombok.Value;

@RequiredArgsConstructor
@Value
public final class BDictionaryParser implements BValueParser<BDictionary> {

	public static final Pattern PATTERN = Pattern.compile("(?sm)^d(?<entries>.*)e$");

	private final Charset charset;

	public BDictionaryParser() {
		this(Charset.defaultCharset());
	}

	@Override
	public ByteBuffer writeToByteBuffer(BDictionary value) throws IOException {
		final StringBuffer buffer = new StringBuffer();

		buffer.append(BValueCharacter.DICTIONARY);

		for (Entry<String, BValue<?>> entry : value.entrySet()) {
			buffer.append(new BStringParser(this.getCharset()).writeToString(BString.valueOf(entry.getKey())));
			buffer.append(new BValueParsers(this.getCharset()).writeToString(entry.getValue()));
		}

		buffer.append(BValueCharacter.END);

		return ByteBuffer.wrap(buffer.toString().getBytes(this.getCharset()));
	}

	@Override
	public BDictionary readFromByteBuffer(ByteBuffer byteBuffer) throws IOException {
		int c = BValueParser.get(byteBuffer);

		if (c != BValueCharacter.DICTIONARY) {
			throw new IllegalArgumentException("Expected 'd', not '" + (char) c + "'");
		}

		final Map<String, BValue<?>> map = new TreeMap<>();

		BString key = null;
		BValue<?> value = null;

		c = BValueParser.get(byteBuffer, byteBuffer.position());

		while (c != BValueCharacter.END) {
			if (!byteBuffer.hasRemaining()) {
				throw new IllegalArgumentException("Expected 'e', not '" + (char) c + "'");
			}

			key = new BStringParser(this.getCharset()).readFromByteBuffer(byteBuffer);
			value = new BValueParsers(this.getCharset()).readFromByteBuffer(byteBuffer);

			map.put(key.getString(), value);

			c = BValueParser.get(byteBuffer, byteBuffer.position());
		}

		return BDictionary.create(map);
	}

}
