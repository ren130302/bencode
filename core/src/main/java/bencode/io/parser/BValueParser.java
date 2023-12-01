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
		if (value instanceof BString v) {
			stream.serializeBString(v);
			return;
		}
		if (value instanceof BInteger v) {
			stream.serializeBInteger(v);
			return;
		}
		if (value instanceof BList v) {
			stream.serializeBList(v);
			return;
		}
		if (value instanceof BDictionary v) {
			stream.serializeBDictionary(v);
			return;
		}

		throw stream.unknownBEncodeType(value.getClass());
	}

}
