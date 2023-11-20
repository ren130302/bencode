package bencode.parser;

import java.io.IOException;

import bencode.BValue;
import lombok.NonNull;

public interface BValueSerializer<T extends BValue<?>> {
	void serialize(@NonNull BEncodeOutputStream stream, @NonNull T value) throws IOException;
}
