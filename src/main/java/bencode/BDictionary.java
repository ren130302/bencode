package bencode;

import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.function.Function;

import lombok.NonNull;
import lombok.Value;

@Value(staticConstructor = "create")
public final class BDictionary implements BValue<Map<String, BValue<?>>>, Map<String, BValue<?>> {

	private static final long serialVersionUID = -7574359365654348201L;
	private final @NonNull Map<String, BValue<?>> value;

	@Override
	public BDictionary clone() {
		try {
			return (BDictionary) super.clone();
		} catch (CloneNotSupportedException cnse) {
			final BDictionary result = create(new TreeMap<>(this.value));

			return result;
		}
	}

	@Override
	public BValueType getType() {
		return BValueType.DICTIONARY;
	}

	@Override
	public String toString() {
		final StringBuffer buffer = new StringBuffer();
		buffer.append('{');
		buffer.append(String.join(", ",
				this.getValue().entrySet().stream()
						.map(entry -> BString.valueOf(entry.getKey()).toString() + ":" + entry.getValue().toString())
						.toList()));
		buffer.append('}');
		return buffer.toString();
	}

	@Override
	public void clear() {
		this.getValue().clear();
	}

	@Override
	public boolean containsKey(Object key) {
		return this.getValue().containsKey(key);
	}

	@Override
	public boolean containsValue(Object value) {
		return this.getValue().containsValue(value);
	}

	@Override
	public Set<Entry<String, BValue<?>>> entrySet() {
		return this.getValue().entrySet();
	}

	public Set<Entry<BString, BValue<?>>> entrySetConverted() {
		final Map<BString, BValue<?>> result = new TreeMap<>();

		for (Entry<String, BValue<?>> entry : this.getValue().entrySet()) {
			result.put(BString.valueOf(entry.getKey()), entry.getValue());
		}

		return result.entrySet();
	}

	@Override
	public BValue<?> get(Object key) {
		return this.getValue().get(key);
	}

	public BDictionary getBDictionary(String key) {
		return this.getOptionalBDictionary(key).get();
	}

	public BInteger getBInteger(String key) {
		return this.getOptionalBInteger(key).get();
	}

	public BList getBList(String key) {
		return this.getOptionalBList(key).get();
	}

	public boolean getBool(String key) {
		return this.getLong(key, 0) == 1;
	}

	public BString getBString(String key) {
		return this.getOptionalBString(key).get();
	}

	public Byte[] getBytes(String key) {
		return this.getBString(key).getValue();
	}

	public <T extends BValue<?>, V> V getDef(Optional<T> optional, V defaultValue, Function<T, V> func) {
		return optional.isPresent() ? func.apply(optional.get()) : defaultValue;
	}

	public Map<String, BValue<?>> getDictionary(String key) {
		return this.getBDictionary(key).getValue();
	}

	public int getInt(String key) {
		return this.getBInteger(key).intValue();
	}

	public int getInt(String key, int defaultValue) {
		return this.getDef(this.getOptionalBInteger(key), defaultValue, BInteger::intValue);
	}

	public long getLong(String key) {
		return this.getBInteger(key).longValue();
	}

	public long getLong(String key, long defaultValue) {
		return this.getDef(this.getOptionalBInteger(key), defaultValue, BInteger::longValue);
	}

	public Optional<BDictionary> getOptionalBDictionary(String key) {
		return this.getOptionalBValue(key, BDictionary.class::cast);
	}

	public Optional<BInteger> getOptionalBInteger(String key) {
		return this.getOptionalBValue(key, BInteger.class::cast);
	}

	public Optional<BList> getOptionalBList(String key) {
		return this.getOptionalBValue(key, BList.class::cast);
	}

	public Optional<BString> getOptionalBString(String key) {
		return this.getOptionalBValue(key, BString.class::cast);
	}

	public <T extends BValue<?>> Optional<T> getOptionalBValue(String key, Function<BValue<?>, T> castFunc) {
		return Optional.ofNullable(castFunc.apply(this.get(key)));
	}

	public short getShort(String key) {
		return this.getBInteger(key).shortValue();
	}

	public short getShort(String key, short defaultValue) {
		return this.getDef(this.getOptionalBInteger(key), defaultValue, BInteger::shortValue);
	}

	public String getString(String key) {
		return this.getBString(key).getString();
	}

	public String getString(String key, Charset charset) {
		return this.getBString(key).getString(charset);
	}

	public String getString(String key, String defaultValue) {
		return this.getDef(this.getOptionalBString(key), defaultValue, BString::getString);
	}

	@Override
	public boolean isEmpty() {
		return this.getValue().isEmpty();
	}

	@Override
	public Set<String> keySet() {
		return this.getValue().keySet();
	}

	public BInteger put(String key, boolean value) {
		return (BInteger) this.put(key, BInteger.valueOf(value ? 1 : 0));
	}

	@Override
	public BValue<?> put(String key, BValue<?> value) {
		return this.getValue().put(key, value);
	}

	public BString put(String key, byte[] value) {
		return (BString) this.put(key, BString.valueOf(value));
	}

	public BInteger put(String key, int value) {
		return (BInteger) this.put(key, BInteger.valueOf(value));
	}

	public BInteger put(String key, long value) {
		return (BInteger) this.put(key, BInteger.valueOf(value));
	}

	public BInteger put(String key, short value) {
		return (BInteger) this.put(key, BInteger.valueOf(value));
	}

	public BString put(String key, String value) {
		return (BString) this.put(key, BString.valueOf(value));
	}

	@Override
	public void putAll(Map<? extends String, ? extends BValue<?>> m) {
		this.getValue().putAll(m);
	}

	public void putIfPresent(String key, BValue<?> value) {
		Optional.of(value).ifPresent(v -> this.put(key, v));
	}

	public void putIfPresent(String key, byte[] value) {
		this.putIfPresent(key, BString.valueOf(value));
	}

	public void putIfPresent(String key, int value) {
		this.putIfPresent(key, BInteger.valueOf(value));
	}

	public void putIfPresent(String key, long value) {
		this.putIfPresent(key, BInteger.valueOf(value));
	}

	public void putIfPresent(String key, short value) {
		this.putIfPresent(key, BInteger.valueOf(value));
	}

	public void putIfPresent(String key, String value) {
		this.putIfPresent(key, BString.valueOf(value));
	}

	@Override
	public BValue<?> remove(Object key) {
		return this.getValue().remove(key);
	}

	@Override
	public int size() {
		return this.getValue().size();
	}

	@Override
	public Collection<BValue<?>> values() {
		return this.getValue().values();
	}
}