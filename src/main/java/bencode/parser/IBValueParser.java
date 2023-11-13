package bencode.parser;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

import bencode.IBValue;
import lombok.NonNull;

public interface IBValueParser<T extends IBValue<?>> {

	Charset getCharset();

	ByteBuffer writeToByteBuffer(@NonNull T value) throws IOException;

	default String writeToString(@NonNull T value) throws IOException {
		return new String(this.writeToBytes(value), this.getCharset());
	}

	default byte[] writeToBytes(@NonNull T value) throws IOException {
		return this.writeToByteBuffer(value).array();
	}

	T readFromByteBuffer(@NonNull ByteBuffer data) throws IOException;

	default T readFromBytes(@NonNull byte[] data) throws IOException {
		return this.readFromByteBuffer(ByteBuffer.wrap(data));
	}

	default T readFromString(@NonNull String data) throws IOException {
		return this.readFromBytes(data.getBytes(this.getCharset()));
	}

	static int get(ByteBuffer byteBuffer, int index) {
		return Byte.toUnsignedInt(byteBuffer.get(index));
	}

	static int get(ByteBuffer byteBuffer) {
		return Byte.toUnsignedInt(byteBuffer.get());
	}
}
