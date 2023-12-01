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
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import bencode.io.parser.BDictionaryParser;
import bencode.io.parser.BIntegerParser;
import bencode.io.parser.BListParser;
import bencode.io.parser.BStringParser;
import bencode.io.parser.BValueParser;
import bencode.values.BDictionary;
import bencode.values.BInteger;
import bencode.values.BList;
import bencode.values.BString;
import bencode.values.BValue;
import lombok.NonNull;

public final class BEncodeInputStream implements Closeable {

	private final PushbackInputStream _stream;
	private final Charset _charset;

	private final Map<Class<? extends BValueDeserializer<?>>, BValueSerializer<?>> knownDeserializer = new HashMap<>();

	public BEncodeInputStream(byte[] bytes) {
		this(bytes, Charset.defaultCharset());
	}

	public BEncodeInputStream(byte[] bytes, Charset charset) {
		this._stream = new PushbackInputStream(new ByteArrayInputStream(bytes));
		this._charset = charset;
		this.init();
	}

	private void init() {
		this.register(BValueParser.class);
		this.register(BStringParser.class);
		this.register(BIntegerParser.class);
		this.register(BListParser.class);
		this.register(BDictionaryParser.class);
	}

	private void register(Class<?> cls) {
		try {
			cls.getDeclaredConstructor().newInstance();
		} catch (Exception e) {
			throw new IllegalArgumentException(cls.getSimpleName(), e);
		}
	}

	@SuppressWarnings("unchecked")
	private <T extends BValueSerializer<?>> T get(@NonNull Class<T> cls) {
		return (T) this.knownDeserializer.get(cls);
	}

	public BValue<?> deserializeBValue() throws IOException {
		return this.get(BValueParser.class).deserialize(this);
	}

	public BDictionary deserializeBDictionary() throws IOException {
		return this.get(BDictionaryParser.class).deserialize(this);
	}

	public BList<?> deserializeBList() throws IOException {
		return this.get(BListParser.class).deserialize(this);
	}

	public BInteger deserializeBInteger() throws IOException {
		return this.get(BIntegerParser.class).deserialize(this);
	}

	public BString deserializeBString() throws IOException {
		return this.get(BStringParser.class).deserialize(this);
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
