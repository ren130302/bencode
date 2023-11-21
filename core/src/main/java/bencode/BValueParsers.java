package bencode;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import bencode.io.BEncodeInputStream;
import bencode.io.BEncodeOutputStream;
import bencode.parser.BDictionaryParser;
import bencode.parser.BIntegerParser;
import bencode.parser.BListParser;
import bencode.parser.BStringParser;
import bencode.parser.BValueParser;
import bencode.parser.IBValueParser;
import bencode.values.BDictionary;
import bencode.values.BInteger;
import bencode.values.BList;
import bencode.values.BString;
import bencode.values.BValue;
import lombok.NonNull;
import lombok.Value;

@Value
public final class BValueParsers {

	private final Charset charset;

	private final Map<Class<?>, IBValueParser<?>> knownParsers = new HashMap<>();

	public BValueParsers() {
		this.charset = Charset.defaultCharset();
		this.init();
	}

	public BValueParsers(Charset charset) {
		this.charset = charset;
		this.init();
	}

	private void init() {
		this.knownParsers.put(BValueParser.class, new BValueParser(this));
		this.knownParsers.put(BStringParser.class, new BStringParser(this));
		this.knownParsers.put(BIntegerParser.class, new BIntegerParser(this));
		this.knownParsers.put(BListParser.class, new BListParser(this));
		this.knownParsers.put(BDictionaryParser.class, new BDictionaryParser(this));
	}

	@SuppressWarnings("unchecked")
	private <T extends IBValueParser<?>> T get(@NonNull Class<T> cls) {
		return (T) this.knownParsers.get(cls);
	}

	public BValueParser getBValueParser() {
		return this.get(BValueParser.class);
	}

	public BDictionaryParser getBDictionaryParser() {
		return this.get(BDictionaryParser.class);
	}

	public BListParser getBListParser() {
		return this.get(BListParser.class);
	}

	public BIntegerParser getBIntegerParser() {
		return this.get(BIntegerParser.class);
	}

	public BStringParser getBStringParser() {
		return this.get(BStringParser.class);
	}

	public <T extends IBValueParser<V>, V extends BValue<?>> byte[] _write(Class<T> cls, V value) throws IOException {
		try (BEncodeOutputStream stream = new BEncodeOutputStream(this.charset)) {
			this.get(cls).serialize(stream, value);
			return stream.array();
		}
	}

	public byte[] writeBDictionaryToBytes(BDictionary value) throws IOException {
		return this._write(BDictionaryParser.class, value);
	}

	public String writeBDictionaryToString(BDictionary value) throws IOException {
		return new String(this.writeBDictionaryToBytes(value), this.charset);
	}

	public byte[] writeBListToBytes(BList<?> value) throws IOException {
		return this._write(BListParser.class, value);
	}

	public String writeBListToString(BList<?> value) throws IOException {
		return new String(this.writeBListToBytes(value), this.charset);
	}

	public byte[] writeBIntegerToBytes(BInteger value) throws IOException {
		return this._write(BIntegerParser.class, value);
	}

	public String writeBIntegerToString(BInteger value) throws IOException {
		return new String(this.writeBIntegerToBytes(value), this.charset);
	}

	public byte[] writeBStringToBytes(BString value) throws IOException {
		return this._write(BStringParser.class, value);
	}

	public String writeBStringToString(BString value) throws IOException {
		return new String(this.writeBStringToBytes(value), this.charset);
	}

	public byte[] writeBValueToBytes(BValue<?> value) throws IOException {
		return this._write(BValueParser.class, value);
	}

	public String writeBValueToString(BValue<?> value) throws IOException {
		return new String(this.writeBValueToBytes(value), this.charset);
	}

	private <T extends IBValueParser<V>, V extends BValue<?>> V _read(Class<T> cls, byte[] bytes) throws IOException {
		try (BEncodeInputStream stream = new BEncodeInputStream(bytes)) {
			return this.get(cls).deserialize(stream);
		}
	}

	public BDictionary readBDictionaryFromBytes(byte[] bytes) throws IOException {
		return this._read(BDictionaryParser.class, bytes);
	}

	public BDictionary readBDictionaryFromString(String data) throws IOException {
		return this.readBDictionaryFromBytes(data.getBytes(this.charset));
	}

	public BList<?> readBListFromBytes(byte[] bytes) throws IOException {
		return this._read(BListParser.class, bytes);
	}

	public BList<?> readBListFromString(String data) throws IOException {
		return this.readBListFromBytes(data.getBytes(this.charset));
	}

	public BInteger readBIntegerFromBytes(byte[] bytes) throws IOException {
		return this._read(BIntegerParser.class, bytes);
	}

	public BInteger readBIntegerFromString(String data) throws IOException {
		return this.readBIntegerFromBytes(data.getBytes(this.charset));
	}

	public BString readBStringFromBytes(byte[] bytes) throws IOException {
		return this._read(BStringParser.class, bytes);
	}

	public BString readBStringFromString(String data) throws IOException {
		return this.readBStringFromBytes(data.getBytes(this.charset));
	}

	public BValue<?> readBValueFromBytes(byte[] bytes) throws IOException {
		return this._read(BValueParser.class, bytes);
	}

	public BValue<?> readBValueFromString(String data) throws IOException {
		return this.readBValueFromBytes(data.getBytes(this.charset));
	}
}
