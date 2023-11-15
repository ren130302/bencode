package bencode.parser;

import java.io.EOFException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;

class ByteBufferUtils {
	static int get(ByteBuffer byteBuffer) throws EOFException {
		hasRemaining(byteBuffer);

		return Byte.toUnsignedInt(byteBuffer.get());
	}

	static int get(ByteBuffer byteBuffer, int index) throws IOException {
		hasRemaining(byteBuffer);

		return Byte.toUnsignedInt(byteBuffer.get(index));
	}

	static void hasRemaining(ByteBuffer byteBuffer) throws EOFException {
		if (!byteBuffer.hasRemaining()) {
			throw new EOFException();
		}
	}

	static void wasEnded() throws IOException {

	}

	static byte[] slice(ByteBuffer byteBuffer, int from, int to) {
		return Arrays.copyOfRange(byteBuffer.array(), from, to);
	}
}
