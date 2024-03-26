package bencode.io.serializer;

import java.io.IOException;
import bencode.io.BEncodeOutputStream;
import bencode.io.BEncodeParser;
import bencode.io.Serializer;
import bencode.value.BDictionary;
import bencode.value.BInteger;
import bencode.value.BList;
import bencode.value.BString;
import bencode.value.BValue;

public final class BValueSerializer implements Serializer<BValue<?>> {

    @Override
    public void serialize(BEncodeParser parser, BEncodeOutputStream stream, BValue<?> value)
            throws IOException {
        if (value instanceof BString v) {
            parser.getBStringSerializer().serialize(parser, stream, v);
            return;
        }
        if (value instanceof BInteger v) {
            parser.getBIntegerSerializer().serialize(parser, stream, v);
            return;
        }
        if (value instanceof BList v) {
            parser.getBListSerializer().serialize(parser, stream, v);
            return;
        }
        if (value instanceof BDictionary v) {
            parser.getBDictionarySerializer().serialize(parser, stream, v);
            return;
        }

        throw stream.unknownBEncodeType(value.getClass());
    }

}
