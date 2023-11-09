package bencode.parser;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import bencode.BList;
import bencode.BValue;
import bencode.BValueCharacter;
import lombok.RequiredArgsConstructor;
import lombok.Value;

@RequiredArgsConstructor
@Value
public class BListParser implements BValueParser<BList> {

	public static final Pattern PATTERN = Pattern.compile("(?sm)^l(?<items>.*)e$");

	private final Charset charset;

	public BListParser() {
		this(Charset.defaultCharset());
	}

	@Override
	public ByteBuffer writeToByteBuffer(BList value) throws IOException {
		final StringBuffer buffer = new StringBuffer();
		buffer.append(BValueCharacter.LIST);

		for (BValue<?> v : value.getValue()) {
			buffer.append(new BValueParsers(this.getCharset()).writeToString(v));
		}

		buffer.append(BValueCharacter.END);

		return ByteBuffer.wrap(buffer.toString().getBytes(this.getCharset()));
	}

	@Override
	public BList readFromByteBuffer(ByteBuffer byteBuffer) throws IOException {
		int c = BValueParser.get(byteBuffer);

		if (c != BValueCharacter.LIST) {
			throw new IllegalArgumentException("Expected 'l', not '" + (char) c + "'");
		}

		final List<BValue<?>> list = new ArrayList<>();

		BValue<?> value = null;
		c = BValueParser.get(byteBuffer, byteBuffer.position());

		while (c != BValueCharacter.END) {
			if (!byteBuffer.hasRemaining()) {
				throw new IllegalArgumentException("Expected 'e', not '" + (char) c + "'");
			}

			value = new BValueParsers(this.getCharset()).readFromByteBuffer(byteBuffer);
			list.add(value);
			c = BValueParser.get(byteBuffer, byteBuffer.position());
		}

		final BList result = BList.create(list);

		return result;
	}

}
