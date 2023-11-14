package bencode.parser;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

import bencode.BDictionary;
import bencode.BInteger;
import bencode.BList;
import bencode.BString;
import bencode.BValue;
import lombok.RequiredArgsConstructor;
import lombok.Value;

@RequiredArgsConstructor
@Value
public final class BValueParser implements IBValueParser<BValue<?>> {

	private final Charset charset;

	public BValueParser() {
		this(Charset.defaultCharset());
	}

	@Override
	public BValue<?> readFromByteBuffer(ByteBuffer byteBuffer) throws IOException {
		int indicator = ParseUtils.get(byteBuffer, byteBuffer.position() + 1);
		if (Character.isDigit(indicator)) {
			return new BStringParser(this.getCharset()).readFromByteBuffer(byteBuffer);
		}
		switch (indicator) {
		case INTEGER:
			return new BIntegerParser(this.getCharset()).readFromByteBuffer(byteBuffer);
		case LIST:
			return new BListParser(this.getCharset()).readFromByteBuffer(byteBuffer);
		case DICTIONARY:
			return new BDictionaryParser(this.getCharset()).readFromByteBuffer(byteBuffer);
		}

		throw new IOException("" + (char) indicator);
	}

	@Override
	public ByteBuffer writeToByteBuffer(BValue<?> value) throws IOException {
		if (value instanceof BString v) {
			return new BStringParser(this.getCharset()).writeToByteBuffer(v);
		}
		if (value instanceof BInteger v) {
			return new BIntegerParser(this.getCharset()).writeToByteBuffer(v);
		}
		if (value instanceof BList v) {
			return new BListParser(this.getCharset()).writeToByteBuffer(v);
		}
		if (value instanceof BDictionary v) {
			return new BDictionaryParser(this.getCharset()).writeToByteBuffer(v);
		}

		throw new IOException("" + value.getClass().getSimpleName());
	}

}
