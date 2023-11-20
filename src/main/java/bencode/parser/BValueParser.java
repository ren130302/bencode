package bencode.parser;

import java.io.IOException;

import bencode.BDictionary;
import bencode.BInteger;
import bencode.BList;
import bencode.BString;
import bencode.BValue;
import lombok.NonNull;
import lombok.Value;

@Value
public final class BValueParser implements IBValueParser<BValue<?>> {

	private final BValueParsers parsers;

	@Override
	public BValue<?> deserialize(@NonNull BEncodeInputStream stream) throws IOException {
		if (Character.isDigit(stream.unread())) {
			return this.parsers.getBStringParser().deserialize(stream);
		}
		if (stream.isIntCode()) {
			return this.parsers.getBIntegerParser().deserialize(stream);
		}
		if (stream.isListCode()) {
			return this.parsers.getBListParser().deserialize(stream);
		}
		if (stream.isDictCode()) {
			return this.parsers.getBDictionaryParser().deserialize(stream);
		}

		throw stream.unknownStartCode();
	}

	@Override
	public void serialize(@NonNull BEncodeOutputStream stream, @NonNull BValue<?> value) throws IOException {
		if (value instanceof BString v || value.getClass() == BString.class) {
			this.parsers.getBStringParser().serialize(stream, (BString) value);
		}
		if (value instanceof BInteger v || value.getClass() == BInteger.class) {
			this.parsers.getBIntegerParser().serialize(stream, (BInteger) value);
		}
		if (value instanceof BList v || value.getClass() == BList.class) {
			this.parsers.getBListParser().serialize(stream, (BList<?>) value);
		}
		if (value instanceof BDictionary v || value.getClass() == BDictionary.class) {
			this.parsers.getBDictionaryParser().serialize(stream, (BDictionary) value);
		}
		if (value instanceof BValue v || value.getClass() == BValue.class) {
			this.parsers.getBValueParser().serialize(stream, value);
		}

		throw stream.unknownBEncodeType(value.getClass());
	}

}
