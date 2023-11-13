package bencode;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

import lombok.NonNull;
import lombok.Value;

@Value(staticConstructor = "create")
public final class BList implements IBValue<List<IBValue<?>>> {

	private static final long serialVersionUID = 1551379075504078235L;
	private final @NonNull List<IBValue<?>> value;

	@Override
	public BList clone() {
		try {
			return (BList) super.clone();
		} catch (CloneNotSupportedException cnse) {
			final BList result = create(new ArrayList<>(this.value));

			return result;
		}
	}

	public int size() {
		return this.value.size();
	}

	public boolean isEmpty() {
		return this.value.isEmpty();
	}

	public boolean contains(Object o) {
		return this.value.contains(o);
	}

	public Iterator<IBValue<?>> iterator() {
		return this.value.iterator();
	}

	public IBValue<?>[] toArray() {
		return (IBValue[]) this.value.toArray();
	}

	public IBValue<?>[] toArray(IBValue<?>[] a) {
		return this.value.toArray(a);
	}

	public boolean add(IBValue<?> e) {
		return this.value.add(e);
	}

	public boolean remove(IBValue<?> o) {
		return this.value.remove(o);
	}

	public boolean containsAll(Collection<IBValue<?>> c) {
		return this.value.containsAll(c);
	}

	public boolean addAll(Collection<? extends IBValue<?>> c) {
		return this.value.addAll(c);
	}

	public boolean addAll(int index, Collection<? extends IBValue<?>> c) {
		return this.value.addAll(index, c);
	}

	public boolean removeAll(Collection<IBValue<?>> c) {
		return this.value.removeAll(c);
	}

	public boolean retainAll(Collection<IBValue<?>> c) {
		return this.value.retainAll(c);
	}

	public void clear() {
		this.value.clear();
	}

	public IBValue<?> get(int index) {
		return this.value.get(index);
	}

	public <T extends IBValue<?>> Optional<T> getOptionalBValue(int index, Function<IBValue<?>, T> castFunc) {
		return Optional.ofNullable(castFunc.apply(this.get(index)));
	}

	/* dictionary */

	public Optional<BDictionary> getOptionalBDictionary(int index) {
		return this.getOptionalBValue(index, BDictionary.class::cast);
	}

	public BDictionary getBDictionary(int index) {
		return this.getOptionalBDictionary(index).get();
	}

	public Map<String, IBValue<?>> getMap(int index) {
		return this.getBDictionary(index).getValue();
	}

	/* list */

	public Optional<BList> getOptionalBList(int index) {
		return this.getOptionalBValue(index, BList.class::cast);
	}

	public BList getBList(int index) {
		return this.getOptionalBList(index).get();
	}

	public List<IBValue<?>> getList(int index) {
		return this.getBList(index).getValue();
	}

	/* integer */

	public Optional<BInteger> getOptionalBInteger(int index) {
		return this.getOptionalBValue(index, BInteger.class::cast);
	}

	public BInteger getBInteger(int index) {
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

	public BString getBString(int index) {
		return this.getOptionalBString(index).get();
	}

	public Byte[] getBytes(int index) {
		return this.getBString(index).getValue();
	}

	public String getString(int index) {
		return this.getBString(index).getString();
	}

	public IBValue<?> set(int index, IBValue<?> element) {
		return this.value.set(index, element);
	}

	public void add(int index, IBValue<?> element) {
		this.value.add(index, element);
	}

	public IBValue<?> remove(int index) {
		return this.value.remove(index);
	}

	public int indexOf(IBValue<?> o) {
		return this.value.indexOf(o);
	}

	public int lastIndexOf(IBValue<?> o) {
		return this.value.lastIndexOf(o);
	}

	public ListIterator<IBValue<?>> listIterator() {
		return this.value.listIterator();
	}

	public ListIterator<IBValue<?>> listIterator(int index) {
		return this.value.listIterator(index);
	}

	public List<IBValue<?>> subList(int fromIndex, int toIndex) {
		return this.value.subList(fromIndex, toIndex);
	}

	public void forEach(Consumer<IBValue<?>> action) {
		Objects.requireNonNull(action);
		for (IBValue<?> t : this.value) {
			action.accept(t);
		}
	}

	public Stream<IBValue<?>> stream() {
		return this.value.stream();
	}

	public void add(String element) {
		this.value.add(BString.valueOf(element));
	}

	public void add(byte[] element) {
		this.value.add(BString.valueOf(element));
	}

	public void add(Number element) {
		this.value.add(BInteger.valueOf(element));
	}

	@Override
	public String toString() {
		final StringBuffer buffer = new StringBuffer();
		buffer.append('[');
		buffer.append(String.join(", ", this.getValue().stream().map(IBValue::toString).toList()));
		buffer.append(']');
		return buffer.toString();
	}

	@Override
	public BValueType getType() {
		return BValueType.LIST;
	}
}