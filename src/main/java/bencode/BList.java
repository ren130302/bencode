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

	public boolean add(BValue<?> e) {
		return this.value.add(e);
	}

	public void add(byte[] element) {
		this.value.add(BString.valueOf(element));
	}

	public void add(int index, BValue<?> element) {
		this.value.add(index, element);
	}

	public void add(Number element) {
		this.value.add(BInteger.valueOf(element));
	}

	public void add(String element) {
		this.value.add(BString.valueOf(element));
	}

	public boolean addAll(Collection<? extends BValue<?>> c) {
		return this.value.addAll(c);
	}

	public boolean addAll(int index, Collection<? extends BValue<?>> c) {
		return this.value.addAll(index, c);
	}

	public void clear() {
		this.value.clear();
	}

	@Override
	public BList clone() {
		try {
			return (BList) super.clone();
		} catch (CloneNotSupportedException cnse) {
			final BList result = create(new ArrayList<>(this.value));

			return result;
		}
	}

	public boolean contains(Object o) {
		return this.value.contains(o);
	}

	public boolean containsAll(Collection<BValue<?>> c) {
		return this.value.containsAll(c);
	}

	public void forEach(Consumer<BValue<?>> action) {
		Objects.requireNonNull(action);
		for (BValue<?> t : this.value) {
			action.accept(t);
		}
	}

	public BValue<?> get(int index) {
		return this.value.get(index);
	}

	public BDictionary getBDictionary(int index) {
		return this.getOptionalBDictionary(index).get();
	}

	public BInteger getBInteger(int index) {
		return this.getOptionalBInteger(index).get();
	}

	public BList getBList(int index) {
		return this.getOptionalBList(index).get();
	}

	public BString getBString(int index) {
		return this.getOptionalBString(index).get();
	}

	/* dictionary */

	public Byte[] getBytes(int index) {
		return this.getBString(index).getValue();
	}

	public int getInt(int index) {
		return this.getBInteger(index).getInt();
	}

	public List<BValue<?>> getList(int index) {
		return this.getBList(index).getValue();
	}

	/* list */

	public long getLong(int index) {
		return this.getBInteger(index).getLong();
	}

	public Map<String, BValue<?>> getMap(int index) {
		return this.getBDictionary(index).getValue();
	}

	public Optional<BDictionary> getOptionalBDictionary(int index) {
		return this.getOptionalBValue(index, BDictionary.class::cast);
	}

	/* integer */

	public Optional<BInteger> getOptionalBInteger(int index) {
		return this.getOptionalBValue(index, BInteger.class::cast);
	}

	public Optional<BList> getOptionalBList(int index) {
		return this.getOptionalBValue(index, BList.class::cast);
	}

	public Optional<BString> getOptionalBString(int index) {
		return this.getOptionalBValue(index, BString.class::cast);
	}

	public <T extends BValue<?>> Optional<T> getOptionalBValue(int index, Function<BValue<?>, T> castFunc) {
		return Optional.ofNullable(castFunc.apply(this.get(index)));
	}

	public short getShort(int index) {
		return this.getBInteger(index).getShort();
	}

	/* string */

	public String getString(int index) {
		return this.getBString(index).getString();
	}

	@Override
	public BValueType getType() {
		return BValueType.LIST;
	}

	public int indexOf(BValue<?> o) {
		return this.value.indexOf(o);
	}

	public boolean isEmpty() {
		return this.value.isEmpty();
	}

	public Iterator<BValue<?>> iterator() {
		return this.value.iterator();
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

	public boolean remove(BValue<?> o) {
		return this.value.remove(o);
	}

	public BValue<?> remove(int index) {
		return this.value.remove(index);
	}

	public boolean removeAll(Collection<BValue<?>> c) {
		return this.value.removeAll(c);
	}

	public boolean retainAll(Collection<BValue<?>> c) {
		return this.value.retainAll(c);
	}

	public BValue<?> set(int index, BValue<?> element) {
		return this.value.set(index, element);
	}

	public int size() {
		return this.value.size();
	}

	public Stream<BValue<?>> stream() {
		return this.value.stream();
	}

	public List<BValue<?>> subList(int fromIndex, int toIndex) {
		return this.value.subList(fromIndex, toIndex);
	}

	public BValue<?>[] toArray() {
		return (BValue[]) this.value.toArray();
	}

	public BValue<?>[] toArray(BValue<?>[] a) {
		return this.value.toArray(a);
	}

	@Override
	public String toString() {
		final StringBuffer buffer = new StringBuffer();
		buffer.append('[');
		buffer.append(String.join(", ", this.getValue().stream().map(BValue::toString).toList()));
		buffer.append(']');
		return buffer.toString();
	}
}