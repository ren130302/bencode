package bencode.io.serializer;

import java.io.IOException;
import java.util.Map.Entry;
import bencode.io.BEncodeOutputStream;
import bencode.io.BEncodeParser;
import bencode.io.Serializer;
import bencode.value.BDictionary;
import bencode.value.BString;
import bencode.value.BValue;

public final class BDictionarySerializer implements Serializer<BDictionary> {

    @Override
    public void serialize(BEncodeParser parser, BEncodeOutputStream stream, BDictionary value)
            throws IOException {
        stream.writeDictCode();

        for (Entry<BString, BValue<?>> entry : value.entrySet()) {
            parser.getBStringSerializer().serialize(parser, stream, entry.getKey());
            parser.getBValueSerializer().serialize(parser, stream, entry.getValue());
        }

        stream.writeEndCode();
    }

}
