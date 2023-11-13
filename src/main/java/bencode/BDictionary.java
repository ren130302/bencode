package bencode;

import java.nio.charset.Charset;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.function.BiConsumer;
import java.util.function.Function;

import lombok.NonNull;
import lombok.Value;

@Value(staticConstructor = "create")
public final class BDictionary implements BValue<Map<String, BValue<?>>> {

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

	public int size() {
		return this.value.size();
	}

	public boolean isEmpty() {
		return this.value.isEmpty();
	}

	public boolean containsKey(Object key) {
		return this.value.containsKey(key);
	}

	public boolean containsValue(Object value) {
		return this.value.containsValue(value);
	}

	public BValue<?> get(Object key) {
		return this.value.get(key);
	}

	public BValue<?> put(String key, BValue<?> value) {
		return this.value.put(key, value);
	}

	public BValue<?> remove(Object key) {
		return this.value.remove(key);
	}

	public void putAll(Map<? extends String, ? extends BValue<?>> m) {
		this.value.putAll(m);
	}

	public void clear() {
		this.value.clear();
	}

	public Set<String> keySet() {
		return this.value.keySet();
	}

	public Collection<BValue<?>> values() {
		return this.value.values();
	}

	public Set<Entry<String, BValue<?>>> entrySet() {
		return this.value.entrySet();
	}

	public Set<Entry<BString, BValue<?>>> entrySetConverted() {
		final Map<BString, BValue<?>> result = new TreeMap<>();

		for (Entry<String, BValue<?>> entry : this.value.entrySet()) {
			result.put(BString.valueOf(entry.getKey()), entry.getValue());
		}

		return result.entrySet();
	}

	public void putIfPresent(String key, BValue<?> value) {
		Optional.of(value).ifPresent(v -> this.put(key, v));
	}

	public <T extends BValue<?>, V> V getDef(Optional<T> optional, V defaultValue, Function<T, V> func) {
		return optional.isPresent() ? func.apply(optional.get()) : defaultValue;
	}

	public <T extends BValue<?>> Optional<T> getOptionalBValue(String key, Function<BValue<?>, T> castFunc) {
		return Optional.ofNullable(castFunc.apply(this.get(key)));
	}

	/* dictionary */

	public Optional<BDictionary> getOptionalBDictionary(String key) {
		return this.getOptionalBValue(key, BDictionary.class::cast);
	}

	public BDictionary getBDictionary(String key) {
		return this.getOptionalBDictionary(key).get();
	}

	public Map<String, BValue<?>> getDictionary(String key) {
		return this.getBDictionary(key).getValue();
	}

	/* string */

	public Optional<BString> getOptionalBString(String key) {
		return this.getOptionalBValue(key, BString.class::cast);
	}

	public BString getBString(String key) {
		return this.getOptionalBString(key).get();
	}

	public Byte[] getBytes(String key) {
		return this.getBString(key).getValue();
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

	public BString put(String key, byte[] value) {
		return (BString) this.put(key, BString.valueOf(value));
	}

	public BString put(String key, String value) {
		return (BString) this.put(key, BString.valueOf(value));
	}

	public void putIfPresent(String key, byte[] value) {
		this.putIfPresent(key, BString.valueOf(value));
	}

	public void putIfPresent(String key, String value) {
		this.putIfPresent(key, BString.valueOf(value));
	}

	/* list */

	public Optional<BList> getOptionalBList(String key) {
		return this.getOptionalBValue(key, BList.class::cast);
	}

	public BList getBList(String key) {
		return this.getOptionalBList(key).get();
	}

	/* integer */

	public Optional<BInteger> getOptionalBInteger(String key) {
		return this.getOptionalBValue(key, BInteger.class::cast);
	}

	public BInteger getBInteger(String key) {
		return this.getOptionalBInteger(key).get();
	}

	public short getShort(String key) {
		return this.getBInteger(key).getShort();
	}

	public int getInt(String key) {
		return this.getBInteger(key).getInt();
	}

	public long getLong(String key) {
		return this.getBInteger(key).getLong();
	}

	public short getShort(String key, short defaultValue) {
		return this.getDef(this.getOptionalBInteger(key), defaultValue, BInteger::getShort);
	}

	public int getInt(String key, int defaultValue) {
		return this.getDef(this.getOptionalBInteger(key), defaultValue, BInteger::getInt);
	}

	public long getLong(String key, long defaultValue) {
		return this.getDef(this.getOptionalBInteger(key), defaultValue, BInteger::getLong);
	}

	public boolean getBool(String key) {
		return this.getLong(key, 0) == 1;
	}

	public BInteger put(String key, boolean value) {
		return (BInteger) this.put(key, BInteger.valueOf(value ? 1 : 0));
	}

	public BInteger put(String key, long value) {
		return (BInteger) this.put(key, BInteger.valueOf(value));
	}

	public BInteger put(String key, int value) {
		return (BInteger) this.put(key, BInteger.valueOf(value));
	}

	public BInteger put(String key, short value) {
		return (BInteger) this.put(key, BInteger.valueOf(value));
	}

	public void putIfPresent(String key, long value) {
		this.putIfPresent(key, BInteger.valueOf(value));
	}

	public void putIfPresent(String key, int value) {
		this.putIfPresent(key, BInteger.valueOf(value));
	}

	public void putIfPresent(String key, short value) {
		this.putIfPresent(key, BInteger.valueOf(value));
	}

	public void forEach(BiConsumer<BString, BValue<?>> action) {
		Objects.requireNonNull(action);
		for (Entry<String, BValue<?>> entry : this.entrySet()) {
			BString k;
			BValue<?> v;
			try {
				k = BString.valueOf(entry.getKey());
				v = entry.getValue();
			} catch (IllegalStateException ise) {
				// this usually means the entry is no longer in the map.
				throw new ConcurrentModificationException(ise);
			}
			action.accept(k, v);
		}
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
	public BValueType getType() {
		return BValueType.DICTIONARY;
	}
}