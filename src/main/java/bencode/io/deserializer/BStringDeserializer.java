package bencode.io.deserializer;

import java.io.IOException;
import bencode.io.BEncodeInputStream;
import bencode.io.BEncodeParser;
import bencode.io.Deserializer;
import bencode.value.BString;

public final class BStringDeserializer implements Deserializer<BString> {

    @Override
    public BString deserialize(BEncodeParser parser, BEncodeInputStream stream) throws IOException {
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

}
