package bencode.io;

import java.io.IOException;
import java.nio.charset.Charset;

import bencode.values.BDictionary;
import bencode.values.BInteger;
import bencode.values.BList;
import bencode.values.BString;
import bencode.values.BValue;

public final class BValueParsers {

	private final Charset _charset;

	public BValueParsers() {
		this(Charset.defaultCharset());
	}

	public BValueParsers(Charset charset) {
		this._charset = charset;
	}

	/* write methods */

	public byte[] writeBDictionaryToBytes(BDictionary value) throws IOException {
		try (BEncodeOutputStream stream = new BEncodeOutputStream(this._charset)) {
			stream.serializeBDictionary(value);
			return stream.toBytes();
		}
	}

	public String writeBDictionaryToString(BDictionary value) throws IOException {
		return new String(this.writeBDictionaryToBytes(value), this._charset);
	}

	public byte[] writeBListToBytes(BList<?> value) throws IOException {
		try (BEncodeOutputStream stream = new BEncodeOutputStream(this._charset)) {
			stream.serializeBList(value);
			return stream.toBytes();
		}
	}

	public String writeBListToString(BList<?> value) throws IOException {
		return new String(this.writeBListToBytes(value), this._charset);
	}

	public byte[] writeBIntegerToBytes(BInteger value) throws IOException {
		try (BEncodeOutputStream stream = new BEncodeOutputStream(this._charset)) {
			stream.serializeBInteger(value);
			return stream.toBytes();
		}
	}

	public String writeBIntegerToString(BInteger value) throws IOException {
		return new String(this.writeBIntegerToBytes(value), this._charset);
	}

	public byte[] writeBStringToBytes(BString value) throws IOException {
		try (BEncodeOutputStream stream = new BEncodeOutputStream(this._charset)) {
			stream.serializeBString(value);
			return stream.toBytes();
		}
	}

	public String writeBStringToString(BString value) throws IOException {
		return new String(this.writeBStringToBytes(value), this._charset);
	}

	public byte[] writeBValueToBytes(BValue<?> value) throws IOException {
		try (BEncodeOutputStream stream = new BEncodeOutputStream(this._charset)) {
			stream.serializeBValue(value);
			return stream.toBytes();
		}
	}

	public String writeBValueToString(BValue<?> value) throws IOException {
		return new String(this.writeBValueToBytes(value), this._charset);
	}

	/* read methods */

	public BDictionary readBDictionaryFromBytes(byte[] bytes) throws IOException {
		try (BEncodeInputStream stream = new BEncodeInputStream(bytes)) {
			return stream.deserializeBDictionary();
		}
	}

	public BDictionary readBDictionaryFromString(String data) throws IOException {
		return this.readBDictionaryFromBytes(data.getBytes(this._charset));
	}

	public BList<?> readBListFromBytes(byte[] bytes) throws IOException {
		try (BEncodeInputStream stream = new BEncodeInputStream(bytes)) {
			return stream.deserializeBList();
		}
	}

	public BList<?> readBListFromString(String data) throws IOException {
		return this.readBListFromBytes(data.getBytes(this._charset));
	}

	public BInteger readBIntegerFromBytes(byte[] bytes) throws IOException {
		try (BEncodeInputStream stream = new BEncodeInputStream(bytes)) {
			return stream.deserializeBInteger();
		}
	}

	public BInteger readBIntegerFromString(String data) throws IOException {
		return this.readBIntegerFromBytes(data.getBytes(this._charset));
	}

	public BString readBStringFromBytes(byte[] bytes) throws IOException {
		try (BEncodeInputStream stream = new BEncodeInputStream(bytes)) {
			return stream.deserializeBString();
		}
	}

	public BString readBStringFromString(String data) throws IOException {
		return this.readBStringFromBytes(data.getBytes(this._charset));
	}

	public BValue<?> readBValueFromBytes(byte[] bytes) throws IOException {
		try (BEncodeInputStream stream = new BEncodeInputStream(bytes)) {
			return stream.deserializeBValue();
		}
	}

	public BValue<?> readBValueFromString(String data) throws IOException {
		return this.readBValueFromBytes(data.getBytes(this._charset));
	}

}
