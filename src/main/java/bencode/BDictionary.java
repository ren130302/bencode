package bencode;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

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
		return (BDictionary) this.get(key);
	}

	public BInteger getBInteger(String key) {
		return (BInteger) this.get(key);
	}

	public BList getBList(String key) {
		return (BList) this.get(key);
	}

	public BString getBString(String key) {
		return (BString) this.get(key);
	}

	@Override
	public boolean isEmpty() {
		return this.getValue().isEmpty();
	}

	@Override
	public Set<String> keySet() {
		return this.getValue().keySet();
	}

	@Override
	public BValue<?> put(String key, BValue<?> value) {
		return this.getValue().put(key, value);
	}

	@Override
	public void putAll(Map<? extends String, ? extends BValue<?>> m) {
		this.getValue().putAll(m);
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