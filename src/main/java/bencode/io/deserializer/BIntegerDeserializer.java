package bencode.io.deserializer;

import java.io.IOException;
import bencode.io.BEncodeInputStream;
import bencode.io.BEncodeParser;
import bencode.io.Deserializer;
import bencode.value.BInteger;

public final class BIntegerDeserializer implements Deserializer<BInteger> {

    @Override
    public BInteger deserialize(BEncodeParser parser, BEncodeInputStream stream)
            throws IOException {
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

}
