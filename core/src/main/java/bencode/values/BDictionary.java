package bencode.values;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.LinkedHashMap;
import java.util.Map;

public final class BDictionary extends LinkedHashMap<BString, BValue<?>> implements BValue<Map<BString, BValue<?>>> {

	private static final long serialVersionUID = -7574359365654348201L;

	public static BDictionary create() {
		return new BDictionary();
	}

	public static BDictionary create(Map<BString, BValue<?>> m) {
		return new BDictionary(m);
	}

	private BDictionary() {
		super();
	}

	private BDictionary(Map<BString, BValue<?>> m) {
		super(m);
	}

	@Override
	public Map<BString, BValue<?>> getValue() {
		return this;
	}

	@Override
	public BDictionary clone() {
		return (BDictionary) super.clone();
	}

	public BValue<?> get(String key) {
		return this.get(BString.valueOf(key));
	}

	public BDictionary getBDictionary(String key) {
		return (BDictionary) this.get(key);
	}

	public BInteger getBInteger(String key) {
		return (BInteger) this.get(key);
	}

	public BList<?> getBList(String key) {
		return (BList<?>) this.get(key);
	}

	public BString getBString(String key) {
		return (BString) this.get(key);
	}

	public BValue<?> put(String key, BValue<?> value) {
		return this.put(BString.valueOf(key), value);
	}

	public BValue<?> put(String key, Number value) {
		return this.put(BString.valueOf(key), BInteger.valueOf(value));
	}

	public BValue<?> put(String key, byte[] value) {
		return this.put(BString.valueOf(key), BString.valueOf(value));
	}

	public BValue<?> put(String key, Byte[] value) {
		return this.put(BString.valueOf(key), BString.valueOf(value));
	}

	public BValue<?> put(String key, String value) {
		return this.put(BString.valueOf(key), BString.valueOf(value));
	}

	public BValue<?> put(String key, String value, Charset charset) {
		return this.put(BString.valueOf(key), BString.valueOf(value, charset));
	}

	public BValue<?> put(String key, String value, String charset) throws UnsupportedEncodingException {
		return this.put(BString.valueOf(key), BString.valueOf(value, charset));
	}

}