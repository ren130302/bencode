package bencode.io;

import static bencode.io.ConstCharacter.CORON;
import static bencode.io.ConstCharacter.DICTIONARY;
import static bencode.io.ConstCharacter.END;
import static bencode.io.ConstCharacter.INTEGER;
import static bencode.io.ConstCharacter.LIST;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;
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

public final class BEncodeOutputStream implements Closeable, Flushable {

	private final ByteArrayOutputStream _stream;
	private final Charset _charset;

	private final Map<Class<? extends BValueDeserializer<?>>, BValueDeserializer<?>> knownSerializer = new HashMap<>();

	public BEncodeOutputStream() {
		this(Charset.defaultCharset());
	}

	public BEncodeOutputStream(Charset charset) {
		this._stream = new ByteArrayOutputStream();
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
	private <T extends BValueDeserializer<?>> T get(@NonNull Class<T> cls) {
		return (T) this.knownSerializer.get(cls);
	}

	public void serializeBValue(BValue<?> value) throws IOException {
		this.get(BValueParser.class).serialize(this, value);
	}

	public void serializeBDictionary(BDictionary value) throws IOException {
		this.get(BDictionaryParser.class).serialize(this, value);
	}

	public void serializeBList(BList<?> value) throws IOException {
		this.get(BListParser.class).serialize(this, value);
	}

	public void serializeBInteger(BInteger value) throws IOException {
		this.get(BIntegerParser.class).serialize(this, value);
	}

	public void serializeBString(BString value) throws IOException {
		this.get(BStringParser.class).serialize(this, value);
	}

	@Override
	public void flush() throws IOException {
		this._stream.flush();
	}

	@Override
	public void close() throws IOException {
		this._stream.close();
	}

	public byte[] array() {
		return this._stream.toByteArray();
	}

	public void writeInt(int b) throws IOException {
		this._stream.write(Integer.toString(b).getBytes(this._charset));
	}

	public void writeChar(char b) throws IOException {
		this._stream.write(b);
	}

	public void writeBytes(byte[] b) throws IOException {
		this._stream.write(b);
	}

	public void writeLong(long b) throws IOException {
		this._stream.write(Long.toString(b).getBytes(this._charset));
	}

	public void writeIntCode() throws IOException {
		this.writeChar(INTEGER);
	}

	public void writeDictCode() throws IOException {
		this.writeChar(DICTIONARY);
	}

	public void writeListCode() throws IOException {
		this.writeChar(LIST);
	}

	public void writeEndCode() throws IOException {
		this.writeChar(END);
	}

	public void writeCoronCode() throws IOException {
		this.writeChar(CORON);
	}

	public IllegalArgumentException unknownBEncodeType(Object obj) {
		return new IllegalArgumentException("Unknown bencode type. " + obj);
	}

}
