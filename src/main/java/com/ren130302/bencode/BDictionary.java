package com.ren130302.bencode;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.checkerframework.checker.nullness.qual.NonNull;

import com.google.common.collect.Maps;

import lombok.Value;

@Value(staticConstructor = "create")
public final class BDictionary implements BValue {

	private static final long serialVersionUID = -7574359365654348201L;
	private final @NonNull Map<String, BValue> map;

	@Override
	public BDictionary clone() {
		try {
			return (BDictionary) super.clone();
		} catch (CloneNotSupportedException cnse) {
			final BDictionary result = create(this.getMap());

			return result;
		}
	}

	public static final Pattern PATTERN = Pattern.compile("(?s)(?m)^d(?<entries>.*)e$");

	public static BDictionary newBDictionary() {
		return create(Maps.newHashMap());
	}

	public int size() {
		return this.getMap().size();
	}

	public boolean isEmpty() {
		return this.getMap().isEmpty();
	}

	public boolean containsKey(Object key) {
		return this.getMap().containsKey(key);
	}

	public boolean containsValue(Object value) {
		return this.getMap().containsValue(value);
	}

	public BValue get(Object key) {
		return this.getMap().get(key);
	}

	public BValue put(String key, BValue value) {
		return this.getMap().put(key, value);
	}

	public BValue remove(Object key) {
		return this.getMap().remove(key);
	}

	public void putAll(Map<? extends String, ? extends BValue> m) {
		this.getMap().putAll(m);
	}

	public void clear() {
		this.getMap().clear();
	}

	public Set<String> keySet() {
		return this.getMap().keySet();
	}

	public Collection<BValue> values() {
		return this.getMap().values();
	}

	public Set<Entry<String, BValue>> entrySet() {
		return this.getMap().entrySet();
	}

	public void putIfPresent(String key, @Nullable BValue value) {
		Optional.of(value).ifPresent(v -> this.put(key, v));
	}

	public <T extends BValue, V> V getDef(Optional<T> optional, V defaultValue, Function<T, V> func) {
		return optional.isPresent() ? func.apply(optional.get()) : defaultValue;
	}

	public <T extends BValue> Optional<T> getOptionalBValue(String key, Function<BValue, T> castFunc) {
		return Optional.ofNullable(castFunc.apply(this.get(key)));
	}

	/* dictionary */

	public Optional<BDictionary> getOptionalBDictionary(String key) {
		return this.getOptionalBValue(key, BDictionary.class::cast);
	}

	public @Nullable BDictionary getBDictionary(String key) {
		return this.getOptionalBDictionary(key).get();
	}

	public @Nullable Map<String, BValue> getDictionary(String key) {
		return this.getBDictionary(key).getMap();
	}

	/* string */

	public Optional<BString> getOptionalBString(String key) {
		return this.getOptionalBValue(key, BString.class::cast);
	}

	public @Nullable BString getBString(String key) {
		return this.getOptionalBString(key).get();
	}

	public byte[] getBytes(String key) {
		return this.getBString(key).getBytes();
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

	public @Nullable BList getBList(String key) {
		return this.getOptionalBList(key).get();
	}

	/* integer */

	public Optional<BInteger> getOptionalBInteger(String key) {
		return this.getOptionalBValue(key, BInteger.class::cast);
	}

	public @Nullable BInteger getBInteger(String key) {
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

	public void forEach(BiConsumer<BString, BValue> action) {
		Objects.requireNonNull(action);
		for (Map.Entry<String, BValue> entry : this.getMap().entrySet().stream().sorted(Entry.comparingByKey())
				.collect(Collectors.toSet())) {
			BString k;
			BValue v;
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

	public static String print(final @Nonnull BDictionary value) {
		final StringBuffer buffer = new StringBuffer();
		final StringBuffer entries = new StringBuffer();

		value.forEach((k, v) -> {
			entries.append(BValue.print(k).replace(System.lineSeparator(), System.lineSeparator() + "\t"))
					.append(System.lineSeparator());
			entries.append(BValue.print(v).replace(System.lineSeparator(), System.lineSeparator() + "\t"))
					.append(System.lineSeparator());
			entries.append(System.lineSeparator());
		});

		return buffer.append("'dictionary':").append('{').append(System.lineSeparator()).append(entries).append('}')
				.toString();
	}

	public static String writeToString(final @Nonnull BDictionary value) {
		final StringBuffer buffer = new StringBuffer();

		buffer.append(BValue.DICTIONARY);

		value.forEach((k, v) -> {
			BValue.writeToString(k);
			BValue.writeToString(v);
		});

		buffer.append(BValue.END);

		return buffer.toString();
	}

	public static byte[] writeToBytes(final @Nonnull BDictionary value) {
		return writeToString(value).getBytes();
	}

	public static ByteBuffer writeToByteBuffer(final @Nonnull BDictionary value) {
		return ByteBuffer.wrap(writeToBytes(value));
	}

	public static BDictionary readFromByteBuffer(final @Nonnull ByteBuffer data) throws IOException {
		return readFromByteBuffer(data, new AtomicInteger());
	}

	public static BDictionary readFromByteBuffer(final @Nonnull ByteBuffer byteBuffer,
			final @Nonnull AtomicInteger indicator) throws IOException {
		int c = BValueUtils.getNextIndicator(byteBuffer, indicator);

		if (c != DICTIONARY) {
			throw new IllegalArgumentException("Expected 'd', not '" + (char) c + "'");
		}

		indicator.set(0);

		final Map<String, BValue> map = Maps.newHashMap();

		BString key = null;
		BValue value = null;
		c = BValueUtils.getNextIndicator(byteBuffer, indicator);

		while (c != END) {
			key = BString.readFromByteBuffer(byteBuffer, indicator);
			value = BValue.readFromByteBuffer(byteBuffer, indicator);

			map.put(key.getString(), value);

			c = BValueUtils.getNextIndicator(byteBuffer, indicator);
		}

		indicator.set(0);

		final BDictionary result = BDictionary.create(map);

		return result;
	}

	public static BDictionary readFromBytes(final @Nonnull byte[] array) throws IOException {
		return readFromByteBuffer(ByteBuffer.wrap(array));
	}

	public static BDictionary readFromString(final @Nonnull String data) throws IOException {
		return readFromBytes(data.getBytes());
	}
}