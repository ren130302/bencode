package bencode.io.serializer;

import java.io.IOException;
import bencode.io.BEncodeOutputStream;
import bencode.io.BEncodeParser;
import bencode.io.Serializer;
import bencode.value.BInteger;

public final class BIntegerSerializer implements Serializer<BInteger> {

    @Override
    public void serialize(BEncodeParser parser, BEncodeOutputStream stream, BInteger value)
            throws IOException {
        stream.writeIntCode();
        stream.writeLong(value.longValue());
        stream.writeEndCode();
    }

}
