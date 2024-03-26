package bencode.io;

import java.io.IOException;

public interface Deserializer<V> {

    public V deserialize(BEncodeParser parsers, BEncodeInputStream stream) throws IOException;

}
