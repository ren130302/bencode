package bencode.io.deserializer;

import java.io.IOException;
import bencode.io.BEncodeInputStream;
import bencode.io.BEncodeParser;
import bencode.io.Deserializer;
import bencode.value.BList;
import bencode.value.BValue;

public final class BListDeserializer implements Deserializer<BList<?>> {

    @Override
    public BList<?> deserialize(BEncodeParser parser, BEncodeInputStream stream)
            throws IOException {
        stream.checkListCode();

        final BList<BValue<?>> result = BList.create();

        BValue<?> value = null;

        while (stream.hasRemaining()) {
            if (stream.isEndCode()) {
                stream.read();
                break;
            }
            value = parser.getBListDeserializer().deserialize(parser, stream);
            result.add(value);
        }

        return result;
    }

}
