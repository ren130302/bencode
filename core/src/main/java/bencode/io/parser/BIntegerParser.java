package bencode.io.parser;

import java.io.IOException;

import bencode.io.BEncodeInputStream;
import bencode.io.BEncodeOutputStream;
import bencode.io.BValueDeserializer;
import bencode.io.BValueSerializer;
import bencode.values.BInteger;
import lombok.NonNull;

public final class BIntegerParser implements BValueSerializer<BInteger>, BValueDeserializer<BInteger> {

	@Override
	public BInteger deserialize(@NonNull BEncodeInputStream stream) throws IOException {
		stream.checkIntCode();

		StringBuffer strBuf = new StringBuffer();

		while (stream.hasRemaining()) {
			if (stream.isEndCode()) {
				stream.read();
				break;
			}

			int c = stream.read();
			if (Character.isDigit(c) || stream.isNegaCode(c)) {
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
