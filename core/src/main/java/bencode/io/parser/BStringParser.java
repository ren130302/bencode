package bencode.io.parser;

import java.io.IOException;

import bencode.io.BEncodeInputStream;
import bencode.io.BEncodeOutputStream;
import bencode.io.BValueDeserializer;
import bencode.io.BValueSerializer;
import bencode.values.BString;
import lombok.NonNull;

public final class BStringParser implements BValueSerializer<BString>, BValueDeserializer<BString> {

	@Override
	public BString deserialize(@NonNull BEncodeInputStream stream) throws IOException {
		StringBuffer strBuf = new StringBuffer();

		int c = stream.read();

		while (stream.hasRemaining()) {
			if (stream.isCoronCode(c)) {
				break;
			}
			if (Character.isDigit(c)) {
				strBuf.append((char) c);
			}
			c = stream.read();
		}

		int length = Integer.parseInt(strBuf.toString());
		return BString.valueOf(stream.readBytes(length));
	}

	@Override
	public void serialize(@NonNull BEncodeOutputStream stream, @NonNull BString value) throws IOException {
		stream.writeLong(value.getValue().length);
		stream.writeCoronCode();
		stream.writeBytes(value.getBytes());
	}

}
