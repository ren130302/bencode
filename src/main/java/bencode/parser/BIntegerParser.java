package bencode.parser;

import static bencode.utils.ConstCharacter.NEGA;

import java.io.IOException;

import bencode.BValueParsers;
import bencode.io.BEncodeInputStream;
import bencode.io.BEncodeOutputStream;
import bencode.values.BInteger;
import lombok.NonNull;
import lombok.Value;

@Value
public final class BIntegerParser implements IBValueParser<BInteger> {

	private final BValueParsers parsers;

	@Override
	public BInteger deserialize(@NonNull BEncodeInputStream stream) throws IOException {
		stream.checkIntCode();

		StringBuffer strBuf = new StringBuffer();

		while (!stream.isEndCode()) {
			int c = stream.read();
			if (c == NEGA || Character.isDigit(c)) {
				strBuf.append((char) c);
			}
		}

		return BInteger.valueOf(Long.parseLong(strBuf.toString()));
	}

	@Override
	public void serialize(@NonNull BEncodeOutputStream stream, @NonNull BInteger value) throws IOException {
		stream.writeIntCode();
		stream.writeLong(value.longValue());
		stream.writeEndCode();
	}

}
