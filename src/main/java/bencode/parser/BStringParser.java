package bencode.parser;

import java.io.IOException;

import bencode.BString;
import lombok.NonNull;
import lombok.Value;

@Value
public final class BStringParser implements IBValueParser<BString> {

	private final BValueParsers parsers;

	@Override
	public BString deserialize(@NonNull BEncodeInputStream stream) throws IOException {
		StringBuffer strBuf = new StringBuffer();

		int c = -1;

		while (stream.isCoronCode()) {
			c = stream.read();
			if (Character.isDigit(c)) {
				strBuf.append((char) stream.read());
				continue;
			}
			break;
		}

		int length = Integer.parseInt(strBuf.toString());
		return BString.valueOf(stream.readBytes(length));
	}

	@Override
	public void serialize(@NonNull BEncodeOutputStream stream, @NonNull BString value) throws IOException {
		stream.writeLong(value.getValue().length);
		stream.writeCoronCode();
		stream.writeBytes(value.get());
	}

}
