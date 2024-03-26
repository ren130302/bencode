package bencode.io;

import java.io.IOException;

public interface Serializer<V> {

    public void serialize(BEncodeParser parser, BEncodeOutputStream stream, V value)
            throws IOException;

}
