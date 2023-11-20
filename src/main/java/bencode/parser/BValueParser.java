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
		if (value instanceof BString v) {
			this.parsers.getBStringParser().serialize(stream, v);
		}
		if (value instanceof BInteger v) {
			this.parsers.getBIntegerParser().serialize(stream, v);
		}
		if (value instanceof BList v) {
			this.parsers.getBListParser().serialize(stream, v);
		}
		if (value instanceof BDictionary v) {
			this.parsers.getBDictionaryParser().serialize(stream, v);
		}

		throw stream.unknownBEncodeType(value.getClass());
	}

}
