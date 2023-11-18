package bencode.parser;

import java.io.IOException;
import java.nio.ByteBuffer;

import bencode.BDictionary;
import bencode.BInteger;
import bencode.BList;
import bencode.BString;
import bencode.BValue;
import lombok.Value;

@Value
public final class BValueParser implements IBValueParser<BValue<?>> {

	private final BValueParsers parsers;

	@Override
	public BValue<?> readFromByteBuffer(ByteBuffer byteBuffer) throws IOException {
		int indicator = ByteBufferUtils.get(byteBuffer, byteBuffer.position());
		if (Character.isDigit(indicator)) {
			return this.getParsers().getBStringParser().readFromByteBuffer(byteBuffer);
		}
		switch (indicator) {
		case INTEGER:
			return this.getParsers().getBIntegerParser().readFromByteBuffer(byteBuffer);
		case LIST:
			return this.getParsers().getBListParser().readFromByteBuffer(byteBuffer);
		case DICTIONARY:
			return this.getParsers().getBDictionaryParser().readFromByteBuffer(byteBuffer);
		}

		throw ByteBufferUtils.createUnknownValueType((char) indicator);
	}

	@Override
	public String writeToString(BValue<?> value) throws IOException {
		if (value instanceof BString v) {
			return this.getParsers().getBStringParser().writeToString(v);
		}
		if (value instanceof BInteger v) {
			return this.getParsers().getBIntegerParser().writeToString(v);
		}
		if (value instanceof BList v) {
			return this.getParsers().getBListParser().writeToString(v);
		}
		if (value instanceof BDictionary v) {
			return this.getParsers().getBDictionaryParser().writeToString(v);
		}

		throw ByteBufferUtils.createUnknownValueType(value.getClass());
	}

}
