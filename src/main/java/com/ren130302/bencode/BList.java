package com.ren130302.bencode;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import javax.annotation.Nonnull;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import com.google.common.collect.Lists;

import lombok.Value;

@Value(staticConstructor = "create")
public final class BList implements BValue {

	private static final long serialVersionUID = 1551379075504078235L;
	private final @NonNull List<BValue> list;

	@Override
	public BList clone() {
		try {
			return (BList) super.clone();
		} catch (CloneNotSupportedException cnse) {
			final BList result = create(this.getList());

			return result;
		}
	}

	public static final Pattern PATTERN = Pattern.compile("(?s)(?m)^l(?<items>.*)e$");

	public static BList newBList() {
		return create(Lists.newArrayList());
	}

	public int size() {
		return this.getList().size();
	}

	public boolean isEmpty() {
		return this.getList().isEmpty();
	}

	public boolean contains(Object o) {
		return this.getList().contains(o);
	}

	public Iterator<BValue> iterator() {
		return this.getList().iterator();
	}

	public BValue[] toArray() {
		return (BValue[]) this.getList().toArray();
	}

	public BValue[] toArray(BValue[] a) {
		return this.getList().toArray(a);
	}

	public boolean add(BValue e) {
		return this.getList().add(e);
	}

	public boolean remove(BValue o) {
		return this.getList().remove(o);
	}

	public boolean containsAll(Collection<BValue> c) {
		return this.getList().containsAll(c);
	}

	public boolean addAll(Collection<BValue> c) {
		return this.getList().addAll(c);
	}

	public boolean addAll(int index, Collection<BValue> c) {
		return this.getList().addAll(index, c);
	}

	public boolean removeAll(Collection<BValue> c) {
		return this.getList().removeAll(c);
	}

	public boolean retainAll(Collection<BValue> c) {
		return this.getList().retainAll(c);
	}

	public void clear() {
		this.getList().clear();
	}

	public BValue get(int index) {
		return this.getList().get(index);
	}

	public <T extends BValue> Optional<T> getOptionalBValue(int index, Function<BValue, T> castFunc) {
		return Optional.ofNullable(castFunc.apply(this.get(index)));
	}

	/* dictionary */

	public Optional<BDictionary> getOptionalBDictionary(int index) {
		return this.getOptionalBValue(index, BDictionary.class::cast);
	}

	public @Nullable BDictionary getBDictionary(int index) {
		return this.getOptionalBDictionary(index).get();
	}

	public Map<String, BValue> getMap(int index) {
		return this.getBDictionary(index).getMap();
	}

	/* list */

	public Optional<BList> getOptionalBList(int index) {
		return this.getOptionalBValue(index, BList.class::cast);
	}

	public @Nullable BList getBList(int index) {
		return this.getOptionalBList(index).get();
	}

	public List<BValue> getList(int index) {
		return this.getBList(index).getList();
	}

	/* integer */

	public Optional<BInteger> getOptionalBInteger(int index) {
		return this.getOptionalBValue(index, BInteger.class::cast);
	}

	public @Nullable BInteger getBInteger(int index) {
		return this.getOptionalBInteger(index).get();
	}

	public long getLong(int index) {
		return this.getBInteger(index).getLong();
	}

	public int getInt(int index) {
		return this.getBInteger(index).getInt();
	}

	public short getShort(int index) {
		return this.getBInteger(index).getShort();
	}

	/* string */

	public Optional<BString> getOptionalBString(int index) {
		return this.getOptionalBValue(index, BString.class::cast);
	}

	public @Nullable BString getBString(int index) {
		return this.getOptionalBString(index).get();
	}

	public byte[] getBytes(int index) {
		return this.getBString(index).getBytes();
	}

	public String getString(int index) {
		return this.getBString(index).getString();
	}

	public BValue set(int index, BValue element) {
		return this.getList().set(index, element);
	}

	public void add(int index, BValue element) {
		this.getList().add(index, element);
	}

	public BValue remove(int index) {
		return this.getList().remove(index);
	}

	public int indexOf(BValue o) {
		return this.getList().indexOf(o);
	}

	public int lastIndexOf(BValue o) {
		return this.getList().lastIndexOf(o);
	}

	public ListIterator<BValue> listIterator() {
		return this.getList().listIterator();
	}

	public ListIterator<BValue> listIterator(int index) {
		return this.getList().listIterator(index);
	}

	public List<BValue> subList(int fromIndex, int toIndex) {
		return this.getList().subList(fromIndex, toIndex);
	}

	public void forEach(Consumer<BValue> action) {
		Objects.requireNonNull(action);
		for (BValue t : this.getList()) {
			action.accept(t);
		}
	}

	public Stream<BValue> stream() {
		return this.getList().stream();
	}

	public void add(String element) {
		this.getList().add(BString.valueOf(element));
	}

	public void add(byte[] element) {
		this.getList().add(BString.valueOf(element));
	}

	public void add(Number element) {
		this.getList().add(BInteger.valueOf(element));
	}

	public static String print(final @Nonnull BList value) {
		final StringBuffer buffer = new StringBuffer();
		final StringBuffer items = new StringBuffer();

		value.forEach(v -> {
			items.append(v.toString().replace(System.lineSeparator(), System.lineSeparator() + "\t"));
			items.append(System.lineSeparator());
		});

		return buffer.append("'list':").append('[').append(System.lineSeparator()).append(items).append(']').toString();
	}

	public static String writeToString(final @Nonnull BList value) {
		final StringBuffer buffer = new StringBuffer();
		buffer.append(BValue.LIST);

		value.forEach(BValue::writeToString);

		buffer.append(BValue.END);

		return buffer.toString();
	}

	public static byte[] writeToBytes(final @Nonnull BList value) {
		return writeToString(value).getBytes();
	}

	public static ByteBuffer writeToByteBuffer(final @Nonnull BList value) {
		return ByteBuffer.wrap(writeToBytes(value));
	}

	public static BList readFromByteBuffer(final @Nonnull ByteBuffer byteBuffer) throws IOException {
		return readFromByteBuffer(byteBuffer, new AtomicInteger());
	}

	public static BList readFromByteBuffer(final @Nonnull ByteBuffer byteBuffer, final @Nonnull AtomicInteger indicator)
			throws IOException {
		int c = BValueUtils.getNextIndicator(byteBuffer, indicator);

		if (c != LIST) {
			throw new IllegalArgumentException("Expected 'l', not '" + (char) c + "'");
		}

		indicator.set(0);

		final List<BValue> list = Lists.newArrayList();

		BValue value = null;
		c = BValueUtils.getNextIndicator(byteBuffer, indicator);

		while (c != END) {
			value = BValue.readFromByteBuffer(byteBuffer, indicator);
			list.add(value);
			c = BValueUtils.getNextIndicator(byteBuffer, indicator);
		}

		indicator.set(0);

		final BList result = BList.create(list);

		return result;
	}

	public static BList readFromBytes(final @Nonnull byte[] data) throws IOException {
		return readFromByteBuffer(ByteBuffer.wrap(data));
	}

	public static BList readFromString(final @Nonnull String data) throws IOException {
		return readFromBytes(data.getBytes());
	}
}