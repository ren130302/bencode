package bencode.io;

import static bencode.io.ConstCharacter.CORON;
import static bencode.io.ConstCharacter.DICTIONARY;
import static bencode.io.ConstCharacter.END;
import static bencode.io.ConstCharacter.INTEGER;
import static bencode.io.ConstCharacter.LIST;
import static bencode.io.ConstCharacter.NEGA;

import java.io.ByteArrayInputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.PushbackInputStream;

import bencode.values.BDictionary;
import bencode.values.BInteger;
import bencode.values.BList;
import bencode.values.BString;
import bencode.values.BValue;

public final class BEncodeInputStream implements Closeable {

	private final PushbackInputStream _stream;

	public BEncodeInputStream(byte[] bytes) {
		this._stream = new PushbackInputStream(new ByteArrayInputStream(bytes));
	}

	public BValue<?> deserializeBValue() throws IOException {
		if (Character.isDigit(this.unread())) {
			return this.deserializeBString();
		}
		if (this.isIntCode(this.unread())) {
			return this.deserializeBInteger();
		}
		if (this.isListCode(this.unread())) {
			return this.deserializeBList();
		}
		if (this.isDictCode(this.unread())) {
			return this.deserializeBDictionary();
		}

		throw this.unknownStartCode();
	}

	public BDictionary deserializeBDictionary() throws IOException {
		this.checkDictCode();

		final BDictionary result = BDictionary.create();

		BString key = null;
		BValue<?> value = null;

		while (this.hasRemaining()) {
			if (this.isEndCode()) {
				this.read();
				break;
			}

			key = this.deserializeBString();
			value = this.deserializeBValue();
			result.put(key, value);
		}

		return result;
	}

	public BList<?> deserializeBList() throws IOException {
		this.checkListCode();

		final BList<BValue<?>> result = BList.create();

		BValue<?> value = null;

		while (this.hasRemaining()) {
			if (this.isEndCode()) {
				this.read();
				break;
			}
			value = this.deserializeBValue();
			result.add(value);
		}

		return result;
	}

	public BInteger deserializeBInteger() throws IOException {
		this.checkIntCode();

		StringBuffer strBuf = new StringBuffer();

		while (this.hasRemaining()) {
			if (this.isEndCode()) {
				this.read();
				break;
			}

			int c = this.read();
			if (Character.isDigit(c) || this.isNegaCode(c)) {
				strBuf.append((char) c);
			}
		}

		return BInteger.valueOf(Long.parseLong(strBuf.toString()));
	}

	public BString deserializeBString() throws IOException {
		StringBuffer strBuf = new StringBuffer();

		int c = this.read();

		while (this.hasRemaining()) {
			if (this.isCoronCode(c)) {
				break;
			}
			if (Character.isDigit(c)) {
				strBuf.append((char) c);
			}
			c = this.read();
		}

		int length = Integer.parseInt(strBuf.toString());
		return BString.valueOf(this.readBytes(length));
	}

	@Override
	public void close() throws IOException {
		this._stream.close();
	}

	public boolean hasRemaining() throws IOException {
		return 0 <= this._stream.available();
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

	public boolean isNegaCode(int c) throws IOException {
		return this.isCode(c, NEGA);
	}

	public boolean isCoronCode(int c) throws IOException {
		return this.isCode(c, CORON);
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
