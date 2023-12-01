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
import java.util.Map.Entry;

import bencode.values.BDictionary;
import bencode.values.BInteger;
import bencode.values.BList;
import bencode.values.BString;
import bencode.values.BValue;

public final class BEncodeOutputStream implements Closeable, Flushable {

	private final ByteArrayOutputStream _stream;
	private final Charset _charset;

	public BEncodeOutputStream() {
		this(Charset.defaultCharset());
	}

	public BEncodeOutputStream(Charset charset) {
		this._stream = new ByteArrayOutputStream();
		this._charset = charset;

	}

	public void serializeBValue(BValue<?> value) throws IOException {
		if (value instanceof BString v) {
			this.serializeBString(v);
			return;
		}
		if (value instanceof BInteger v) {
			this.serializeBInteger(v);
			return;
		}
		if (value instanceof BList v) {
			this.serializeBList(v);
			return;
		}
		if (value instanceof BDictionary v) {
			this.serializeBDictionary(v);
			return;
		}

		throw this.unknownBEncodeType(value.getClass());
	}

	public void serializeBDictionary(BDictionary value) throws IOException {
		this.writeDictCode();

		for (Entry<BString, BValue<?>> entry : value.entrySet()) {
			this.serializeBString(entry.getKey());
			this.serializeBValue(entry.getValue());
		}

		this.writeEndCode();
	}

	public void serializeBList(BList<?> value) throws IOException {
		this.writeListCode();

		for (BValue<?> v : value.getValue()) {
			this.serializeBValue(v);
		}

		this.writeEndCode();
	}

	public void serializeBInteger(BInteger value) throws IOException {
		this.writeIntCode();
		this.writeLong(value.longValue());
		this.writeEndCode();
	}

	public void serializeBString(BString value) throws IOException {
		this.writeLong(value.getValue().length);
		this.writeCoronCode();
		this.writeBytes(value.getBytes());
	}

	@Override
	public void flush() throws IOException {
		this._stream.flush();
	}

	@Override
	public void close() throws IOException {
		this._stream.close();
	}

	public byte[] toBytes() {
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
