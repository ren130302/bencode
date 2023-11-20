package bencode.parser;

import static bencode.parser.BValueCharacter.CORON;
import static bencode.parser.BValueCharacter.DICTIONARY;
import static bencode.parser.BValueCharacter.END;
import static bencode.parser.BValueCharacter.INTEGER;
import static bencode.parser.BValueCharacter.LIST;

import java.io.ByteArrayInputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.PushbackInputStream;

public final class BEncodeInputStream implements Closeable {

	private final PushbackInputStream _stream;

	public BEncodeInputStream(byte[] bytes) {
		this._stream = new PushbackInputStream(new ByteArrayInputStream(bytes));
	}

	@Override
	public void close() throws IOException {
		this._stream.close();
	}

	public int unread() throws IOException {
		int b = this._stream.read();
		this._stream.unread(b);
		return b;
	}

	public int read() throws IOException {
		int b = this._stream.read();
		return b;
	}

	public byte[] readBytes(int capacity) throws IOException {
		final byte[] buf = new byte[capacity];
		this._stream.read(buf);
		return buf;
	}

	public boolean isCode(int c, int ch) throws IOException {
		return c == ch;
	}

	public boolean isIntCode(int c) throws IOException {
		return this.isCode(c, INTEGER);
	}

	public boolean isDictCode(int c) throws IOException {
		return this.isCode(c, DICTIONARY);
	}

	public boolean isListCode(int c) throws IOException {
		return this.isCode(c, LIST);
	}

	public boolean isCoronCode() throws IOException {
		return this.isCode(this.unread(), CORON);
	}

	public boolean isEndCode() throws IOException {
		return this.isCode(this.unread(), END);
	}

	public IllegalArgumentException makeCodeExcept(char ch) throws IOException {
		return new IllegalArgumentException("Except '" + ch + "', not'" + (char) this.unread() + "'");
	}

	public void checkDictCode() throws IOException {
		if (this.isDictCode(this.read())) {
			this.makeCodeExcept(DICTIONARY);
		}
	}

	public void checkListCode() throws IOException {
		if (this.isListCode(this.read())) {
			this.makeCodeExcept(LIST);
		}
	}

	public void checkIntCode() throws IOException {
		if (this.isIntCode(this.read())) {
			this.makeCodeExcept(INTEGER);
		}
	}

	public IllegalArgumentException unknownStartCode() throws IOException {
		return new IllegalArgumentException("Unknown start code. '" + (char) this.unread() + "'");
	}

	public IOException createExcept(String msg, Throwable cause) throws IOException {
		return new IOException(msg + ":" + new String(this._stream.readAllBytes()), cause);
	}
}
