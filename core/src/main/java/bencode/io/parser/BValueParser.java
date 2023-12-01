package bencode.io.parser;

import java.io.IOException;

import bencode.io.BEncodeInputStream;
import bencode.io.BEncodeOutputStream;
import bencode.io.BValueDeserializer;
import bencode.io.BValueSerializer;
import bencode.values.BDictionary;
import bencode.values.BInteger;
import bencode.values.BList;
import bencode.values.BString;
import bencode.values.BValue;
import lombok.NonNull;

public final class BValueParser implements BValueSerializer<BValue<?>>, BValueDeserializer<BValue<?>> {

	@Override
	public BValue<?> deserialize(@NonNull BEncodeInputStream stream) throws IOException {
		if (Character.isDigit(stream.unread())) {
			return stream.deserializeBString();
		}
		if (stream.isIntCode(stream.unread())) {
			return stream.deserializeBInteger();
		}
		if (stream.isListCode(stream.unread())) {
			return stream.deserializeBList();
		}
		if (stream.isDictCode(stream.unread())) {
			return stream.deserializeBDictionary();
		}

		throw stream.unknownStartCode();
	}

	@Override
	public void serialize(@NonNull BEncodeOutputStream stream, @NonNull BValue<?> value) throws IOException {
		try {
			value = BString.class.cast(value);
			if (value instanceof BString v || value.getClass() == BString.class) {
				stream.serializeBString((BString) value);
				return;
			}
		} catch (Exception e) {
			// pass
		}
		try {
			value = BInteger.class.cast(value);
			if (value instanceof BInteger v || value.getClass() == BInteger.class) {
				stream.serializeBInteger((BInteger) value);
				return;
			}
		} catch (Exception e) {
			// pass
		}
		try {
			value = BList.class.cast(value);
			if (value instanceof BList v || value.getClass() == BList.class) {
				stream.serializeBList((BList<?>) value);
				return;
			}
		} catch (Exception e) {
			// pass
		}
		try {
			value = BDictionary.class.cast(value);
			if (value instanceof BDictionary v || value.getClass() == BDictionary.class) {
				stream.serializeBDictionary((BDictionary) value);
				return;
			}
		} catch (Exception e) {
			// pass
		}

		throw stream.unknownBEncodeType(value.getClass());
	}

}
