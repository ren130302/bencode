package bencode.io.serializer;

import java.io.IOException;
import bencode.io.BEncodeOutputStream;
import bencode.io.BEncodeParser;
import bencode.io.Serializer;
import bencode.value.BList;
import bencode.value.BValue;

public final class BListSerializer implements Serializer<BList<?>> {

    @Override
    public void serialize(BEncodeParser parser, BEncodeOutputStream stream, BList<?> value)
            throws IOException {
        stream.writeListCode();

        for (BValue<?> v : value.getValue()) {
            parser.getBValueSerializer().serialize(parser, stream, v);
        }

        stream.writeEndCode();
    }

}
