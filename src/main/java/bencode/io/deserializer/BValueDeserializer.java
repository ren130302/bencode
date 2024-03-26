package bencode.io.deserializer;

import java.io.IOException;
import bencode.io.BEncodeInputStream;
import bencode.io.BEncodeParser;
import bencode.io.Deserializer;
import bencode.value.BValue;

public final class BValueDeserializer implements Deserializer<BValue<?>> {

    @Override
    public BValue<?> deserialize(BEncodeParser parser, BEncodeInputStream stream)
            throws IOException {
        if (Character.isDigit(stream.unread())) {
            return parser.getBStringDeserializer().deserialize(parser, stream);
        }
        if (stream.isIntCode(stream.unread())) {
            return parser.getBIntegerDeserializer().deserialize(parser, stream);
        }
        if (stream.isListCode(stream.unread())) {
            return parser.getBListDeserializer().deserialize(parser, stream);
        }
        if (stream.isDictCode(stream.unread())) {
            return parser.getBDictionaryDeserializer().deserialize(parser, stream);
        }

        throw stream.unknownStartCode();
    }

}
