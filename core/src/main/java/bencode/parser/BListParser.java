package bencode.parser;

import java.io.IOException;

import bencode.BValueParsers;
import bencode.io.BEncodeInputStream;
import bencode.io.BEncodeOutputStream;
import bencode.values.BList;
import bencode.values.BValue;
import lombok.NonNull;
import lombok.Value;

@Value
public final class BListParser implements IBValueParser<BList<?>> {

	private final BValueParsers parsers;

	@Override
	public BList<?> deserialize(@NonNull BEncodeInputStream stream) throws IOException {
		stream.checkListCode();

		final BList<BValue<?>> result = BList.create();

		BValue<?> value = null;

		while (stream.hasRemaining()) {
			if (stream.isEndCode()) {
				stream.read();
				break;
			}
			value = this.parsers.getBValueParser().deserialize(stream);
			result.add(value);
		}

		return result;
	}

	@Override
	public void serialize(@NonNull BEncodeOutputStream stream, @NonNull BList<?> value) throws IOException {
		stream.writeListCode();

		for (BValue<?> v : value.getValue()) {
			this.parsers.getBValueParser().serialize(stream, v);
		}

		stream.writeEndCode();
	}

}
