package bencode.io;

import java.io.IOException;

import bencode.values.BValue;
import lombok.NonNull;

public interface BValueDeserializer<T extends BValue<?>> {

	T deserialize(@NonNull BEncodeInputStream stream) throws IOException;
}
