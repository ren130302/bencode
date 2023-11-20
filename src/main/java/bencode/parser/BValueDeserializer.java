package bencode.parser;

import java.io.IOException;

import bencode.BValue;
import lombok.NonNull;

public interface BValueDeserializer<T extends BValue<?>> {

	T deserialize(@NonNull BEncodeInputStream stream) throws IOException;
}
