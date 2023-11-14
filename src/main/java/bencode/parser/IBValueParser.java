package bencode.parser;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

import bencode.BValue;
import lombok.NonNull;

public interface IBValueParser<T extends BValue<?>> {

	final char CORON = ':';
	final char INTEGER = 'i';
	final char LIST = 'l';
	final char DICTIONARY = 'd';
	final char END = 'e';

	static int get(ByteBuffer byteBuffer) {
		return Byte.toUnsignedInt(byteBuffer.get());
	}

	static int get(ByteBuffer byteBuffer, int index) {
		return Byte.toUnsignedInt(byteBuffer.get(index));
	}

	Charset getCharset();

	T readFromByteBuffer(@NonNull ByteBuffer data) throws IOException;

	default T readFromBytes(@NonNull byte[] data) throws IOException {
		return this.readFromByteBuffer(ByteBuffer.wrap(data));
	}

	default T readFromString(@NonNull String data) throws IOException {
		return this.readFromBytes(data.getBytes(this.getCharset()));
	}

	ByteBuffer writeToByteBuffer(@NonNull T value) throws IOException;

	default byte[] writeToBytes(@NonNull T value) throws IOException {
		return this.writeToByteBuffer(value).array();
	}

	default String writeToString(@NonNull T value) throws IOException {
		return new String(this.writeToBytes(value), this.getCharset());
	}
}
