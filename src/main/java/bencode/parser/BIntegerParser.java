package bencode.parser;

import static bencode.parser.BValueCharacter.NEGA;

import java.io.IOException;

import bencode.BInteger;
import lombok.NonNull;
import lombok.Value;

@Value
public final class BIntegerParser implements IBValueParser<BInteger> {

	private final BValueParsers parsers;

	@Override
	public BInteger deserialize(@NonNull BEncodeInputStream stream) throws IOException {
		try {
			stream.checkIntCode();

			StringBuffer strBuf = new StringBuffer();
			int c = stream.read();

			while (stream.isEndCode()) {

				if ((c == NEGA || Character.isDigit((char) c))) {
					strBuf.append((char) c);
				}
				c = stream.read();
			}
			System.out.println(strBuf);
			long number = Long.parseLong(strBuf.toString());

			return BInteger.valueOf(number);
		} catch (Exception e) {
			throw stream.createExcept("", e);
		}
	}

	@Override
	public void serialize(@NonNull BEncodeOutputStream stream, @NonNull BInteger value) throws IOException {
		stream.writeIntCode();
		stream.writeLong(value.longValue());
		stream.writeEndCode();
	}

}
