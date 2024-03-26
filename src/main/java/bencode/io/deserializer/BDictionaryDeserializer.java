package bencode.io.deserializer;

import java.io.IOException;
import bencode.io.BEncodeInputStream;
import bencode.io.BEncodeParser;
import bencode.io.Deserializer;
import bencode.value.BDictionary;
import bencode.value.BString;
import bencode.value.BValue;

public final class BDictionaryDeserializer implements Deserializer<BDictionary> {

    @Override
    public BDictionary deserialize(BEncodeParser parser, BEncodeInputStream stream)
            throws IOException {
        stream.checkDictCode();

        final BDictionary result = BDictionary.create();

        BString key = null;
        BValue<?> value = null;

        while (stream.hasRemaining()) {
            if (stream.isEndCode()) {
                stream.read();
                break;
            }

            key = parser.getBStringDeserializer().deserialize(parser, stream);
            value = parser.getBValueDeserializer().deserialize(parser, stream);
            result.put(key, value);
        }

        return result;
    }

}
