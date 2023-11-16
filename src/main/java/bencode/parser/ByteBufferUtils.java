package bencode.parser;

import java.io.EOFException;
import java.io.IOException;
import java.nio.ByteBuffer;

public class ByteBufferUtils {
	public static int get(ByteBuffer byteBuffer) throws EOFException {
		hasRemaining(byteBuffer);

		int indicator = Byte.toUnsignedInt(byteBuffer.get());
		return indicator;
	}

	public static int get(ByteBuffer byteBuffer, int index) throws IOException {
		hasRemaining(byteBuffer);

		int indicator = Byte.toUnsignedInt(byteBuffer.get(index));

		return indicator;
	}

	public static void hasRemaining(ByteBuffer byteBuffer) throws EOFException {
		if (!byteBuffer.hasRemaining()) {
			throw new EOFException("" + byteBuffer);
		}
	}

	public static IllegalArgumentException createUnknownValueType(Object obj) {
		return new IllegalArgumentException("Unknown value type. " + obj);
	}

	public static void validateCode(ByteBuffer byteBuffer, char code) throws IOException {
		int c = ByteBufferUtils.get(byteBuffer, byteBuffer.position());

		if (c != code) {
			throw new IllegalArgumentException("Expected '" + code + "', not '" + (char) c + "'");
		}
	}

}
