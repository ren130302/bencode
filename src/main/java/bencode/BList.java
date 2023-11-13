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
public final class BList implements BValue<List<BValue<?>>> {

	private static final long serialVersionUID = 1551379075504078235L;
	private final @NonNull List<BValue<?>> value;

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

	public Iterator<BValue<?>> iterator() {
		return this.value.iterator();
	}

	public BValue<?>[] toArray() {
		return (BValue[]) this.value.toArray();
	}

	public BValue<?>[] toArray(BValue<?>[] a) {
		return this.value.toArray(a);
	}

	public boolean add(BValue<?> e) {
		return this.value.add(e);
	}

	public boolean remove(BValue<?> o) {
		return this.value.remove(o);
	}

	public boolean containsAll(Collection<BValue<?>> c) {
		return this.value.containsAll(c);
	}

	public boolean addAll(Collection<? extends BValue<?>> c) {
		return this.value.addAll(c);
	}

	public boolean addAll(int index, Collection<? extends BValue<?>> c) {
		return this.value.addAll(index, c);
	}

	public boolean removeAll(Collection<BValue<?>> c) {
		return this.value.removeAll(c);
	}

	public boolean retainAll(Collection<BValue<?>> c) {
		return this.value.retainAll(c);
	}

	public void clear() {
		this.value.clear();
	}

	public BValue<?> get(int index) {
		return this.value.get(index);
	}

	public <T extends BValue<?>> Optional<T> getOptionalBValue(int index, Function<BValue<?>, T> castFunc) {
		return Optional.ofNullable(castFunc.apply(this.get(index)));
	}

	/* dictionary */

	public Optional<BDictionary> getOptionalBDictionary(int index) {
		return this.getOptionalBValue(index, BDictionary.class::cast);
	}

	public BDictionary getBDictionary(int index) {
		return this.getOptionalBDictionary(index).get();
	}

	public Map<String, BValue<?>> getMap(int index) {
		return this.getBDictionary(index).getValue();
	}

	/* list */

	public Optional<BList> getOptionalBList(int index) {
		return this.getOptionalBValue(index, BList.class::cast);
	}

	public BList getBList(int index) {
		return this.getOptionalBList(index).get();
	}

	public List<BValue<?>> getList(int index) {
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

	public BValue<?> set(int index, BValue<?> element) {
		return this.value.set(index, element);
	}

	public void add(int index, BValue<?> element) {
		this.value.add(index, element);
	}

	public BValue<?> remove(int index) {
		return this.value.remove(index);
	}

	public int indexOf(BValue<?> o) {
		return this.value.indexOf(o);
	}

	public int lastIndexOf(BValue<?> o) {
		return this.value.lastIndexOf(o);
	}

	public ListIterator<BValue<?>> listIterator() {
		return this.value.listIterator();
	}

	public ListIterator<BValue<?>> listIterator(int index) {
		return this.value.listIterator(index);
	}

	public List<BValue<?>> subList(int fromIndex, int toIndex) {
		return this.value.subList(fromIndex, toIndex);
	}

	public void forEach(Consumer<BValue<?>> action) {
		Objects.requireNonNull(action);
		for (BValue<?> t : this.value) {
			action.accept(t);
		}
	}

	public Stream<BValue<?>> stream() {
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
		buffer.append(String.join(", ", this.getValue().stream().map(BValue::toString).toList()));
		buffer.append(']');
		return buffer.toString();
	}

	@Override
	public BValueType getType() {
		return BValueType.LIST;
	}
}