package bencode.parser;

import java.io.IOException;
import java.util.Map.Entry;

import bencode.BValueParsers;
import bencode.io.BEncodeInputStream;
import bencode.io.BEncodeOutputStream;
import bencode.values.BDictionary;
import bencode.values.BString;
import bencode.values.BValue;
import lombok.NonNull;
import lombok.Value;

@Value
public final class BDictionaryParser implements IBValueParser<BDictionary> {

	private final BValueParsers parsers;

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

			key = this.parsers.getBStringParser().deserialize(stream);
			value = this.parsers.getBValueParser().deserialize(stream);
			result.put(key, value);
		}

		return result;
	}

	@Override
	public void serialize(@NonNull BEncodeOutputStream stream, @NonNull BDictionary value) throws IOException {
		stream.writeDictCode();

		for (Entry<BString, BValue<?>> entry : value.entrySet()) {
			this.parsers.getBStringParser().serialize(stream, entry.getKey());
			this.parsers.getBValueParser().serialize(stream, entry.getValue());
		}

		stream.writeEndCode();
	}

}
