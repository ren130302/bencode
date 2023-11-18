package bencode.parser;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import lombok.NonNull;
import lombok.Value;

@Value
public final class BValueParsers {

	private final Charset charset;
	private final Map<Class<? extends IBValueParser<?>>, IBValueParser<?>> knownParsers = new HashMap<>();

	public BValueParsers() {
		this.charset = Charset.defaultCharset();
		this.init();
	}

	public BValueParsers(Charset charset) {
		this.charset = charset;
		this.init();
	}

	private void init() {
		this.register(BValueParser.class);
		this.register(BDictionaryParser.class);
		this.register(BListParser.class);
		this.register(BIntegerParser.class);
		this.register(BStringParser.class);
	}

	private void register(@NonNull Class<? extends IBValueParser<?>> cls) {
		try {
			this.knownParsers.put(cls, cls.getDeclaredConstructor(this.getClass()).newInstance(this));
		} catch (Exception e) {
			e.printStackTrace();
		}
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
}
