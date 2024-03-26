package bencode.io.serializer;

import java.io.IOException;
import bencode.io.BEncodeOutputStream;
import bencode.io.BEncodeParser;
import bencode.io.Serializer;
import bencode.value.BString;

public final class BStringSerializer implements Serializer<BString> {

    @Override
    public void serialize(BEncodeParser parser, BEncodeOutputStream stream, BString value)
            throws IOException {
        stream.writeLong(value.getValue().length);
        stream.writeCoronCode();
        stream.writeBytes(value.getBytes());
    }

}
