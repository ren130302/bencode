package bencode.parser;

import java.io.IOException;

import bencode.BValueParsers;
import bencode.io.BEncodeInputStream;
import bencode.io.BEncodeOutputStream;
import bencode.values.BDictionary;
import bencode.values.BInteger;
import bencode.values.BList;
import bencode.values.BString;
import bencode.values.BValue;
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
		if (stream.isIntCode(stream.unread())) {
			return this.parsers.getBIntegerParser().deserialize(stream);
		}
		if (stream.isListCode(stream.unread())) {
			return this.parsers.getBListParser().deserialize(stream);
		}
		if (stream.isDictCode(stream.unread())) {
			return this.parsers.getBDictionaryParser().deserialize(stream);
		}

		throw stream.unknownStartCode();
	}

	@Override
	public void serialize(@NonNull BEncodeOutputStream stream, @NonNull BValue<?> value) throws IOException {
		System.out.println(value.getClass());
		try {
			value = BString.class.cast(value);
			if (value instanceof BString v || value.getClass() == BString.class) {
				this.parsers.getBStringParser().serialize(stream, (BString) value);
				return;
			}
		} catch (Exception e) {
			// pass
		}
		try {
			value = BInteger.class.cast(value);
			if (value instanceof BInteger v || value.getClass() == BInteger.class) {
				this.parsers.getBIntegerParser().serialize(stream, (BInteger) value);
				return;
			}
		} catch (Exception e) {
			// pass
		}
		try {
			value = BList.class.cast(value);
			if (value instanceof BList v || value.getClass() == BList.class) {
				this.parsers.getBListParser().serialize(stream, (BList<?>) value);
				return;
			}
		} catch (Exception e) {
			// pass
		}
		try {
			value = BDictionary.class.cast(value);
			if (value instanceof BDictionary v || value.getClass() == BDictionary.class) {
				this.parsers.getBDictionaryParser().serialize(stream, (BDictionary) value);
				return;
			}
		} catch (Exception e) {
			// pass
		}

		throw stream.unknownBEncodeType(value.getClass());
	}

}
