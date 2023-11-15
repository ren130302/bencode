package bencode.parser;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import bencode.BDictionary;
import bencode.BInteger;
import bencode.BList;
import bencode.BString;
import bencode.BValue;
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
		this.knownParsers.put(BValue.class, new BValueParser(this));
		this.knownParsers.put(BDictionary.class, new BDictionaryParser(this));
		this.knownParsers.put(BList.class, new BListParser(this));
		this.knownParsers.put(BInteger.class, new BIntegerParser(this));
		this.knownParsers.put(BString.class, new BStringParser(this));
	}

	public BValueParser getBValueParser() {
		return (BValueParser) this.knownParsers.get(BValue.class);
	}

	public BDictionaryParser getBDictionaryParser() {
		return (BDictionaryParser) this.knownParsers.get(BDictionary.class);
	}

	public BListParser getBListParser() {
		return (BListParser) this.knownParsers.get(BList.class);
	}

	public BIntegerParser getBIntegerParser() {
		return (BIntegerParser) this.knownParsers.get(BInteger.class);
	}

	public BStringParser getBStringParser() {
		return (BStringParser) this.knownParsers.get(BString.class);
	}
}
