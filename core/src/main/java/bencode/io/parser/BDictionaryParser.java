package bencode.io.parser;

import java.io.IOException;
import java.util.Map.Entry;

import bencode.io.BEncodeInputStream;
import bencode.io.BEncodeOutputStream;
import bencode.io.BValueDeserializer;
import bencode.io.BValueSerializer;
import bencode.values.BDictionary;
import bencode.values.BString;
import bencode.values.BValue;
import lombok.NonNull;

public final class BDictionaryParser implements BValueSerializer<BDictionary>, BValueDeserializer<BDictionary> {

	@Override
	public BDictionary deserialize(@NonNull BEncodeInputStream stream) throws IOException {
		stream.checkDictCode();

		final BDictionary result = BDictionary.create();

		BString key = null;
		BValue<?> value = null;

		while (stream.hasRemaining()) {
			if (stream.isEndCode()) {
				stream.read();
				break;
			}

			key = stream.deserializeBString();
			value = stream.deserializeBValue();
			result.put(key, value);
		}

		return result;
	}

	@Override
	public void serialize(@NonNull BEncodeOutputStream stream, @NonNull BDictionary value) throws IOException {
		stream.writeDictCode();

		for (Entry<BString, BValue<?>> entry : value.entrySet()) {
			stream.serializeBString(entry.getKey());
			stream.serializeBValue(entry.getValue());
		}

		stream.writeEndCode();
	}

}
