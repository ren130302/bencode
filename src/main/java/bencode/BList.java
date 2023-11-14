package bencode;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

import lombok.NonNull;
import lombok.Value;

@Value(staticConstructor = "create")
public final class BList implements BValue<List<BValue<?>>>, List<BValue<?>> {

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

	@Override
	public BValueType getType() {
		return BValueType.LIST;
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
	public boolean add(BValue<?> e) {
		return this.getValue().add(e);
	}

	public void add(byte[] element) {
		this.getValue().add(BString.valueOf(element));
	}

	@Override
	public void add(int index, BValue<?> element) {
		this.getValue().add(index, element);
	}

	public void add(Number element) {
		this.getValue().add(BInteger.valueOf(element));
	}

	public void add(String element) {
		this.getValue().add(BString.valueOf(element));
	}

	@Override
	public boolean addAll(Collection<? extends BValue<?>> c) {
		return this.getValue().addAll(c);
	}

	@Override
	public boolean addAll(int index, Collection<? extends BValue<?>> c) {
		return this.getValue().addAll(index, c);
	}

	@Override
	public void clear() {
		this.getValue().clear();
	}

	@Override
	public boolean contains(Object o) {
		return this.getValue().contains(o);
	}

	@Override
	public BValue<?> get(int index) {
		return this.getValue().get(index);
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

	public Byte[] getBytes(int index) {
		return this.getBString(index).getValue();
	}

	public int getInt(int index) {
		return this.getBInteger(index).intValue();
	}

	public List<BValue<?>> getList(int index) {
		return this.getBList(index).getValue();
	}

	public long getLong(int index) {
		return this.getBInteger(index).longValue();
	}

	public Map<String, BValue<?>> getMap(int index) {
		return this.getBDictionary(index).getValue();
	}

	public Optional<BDictionary> getOptionalBDictionary(int index) {
		return this.getOptionalBValue(index, BDictionary.class::cast);
	}

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
		return this.getBInteger(index).shortValue();
	}

	/* string */

	public String getString(int index) {
		return this.getBString(index).getString();
	}

	@Override
	public int indexOf(Object o) {
		return this.getValue().indexOf(o);
	}

	@Override
	public boolean isEmpty() {
		return this.getValue().isEmpty();
	}

	@Override
	public Iterator<BValue<?>> iterator() {
		return this.getValue().iterator();
	}

	@Override
	public int lastIndexOf(Object o) {
		return this.getValue().lastIndexOf(o);
	}

	@Override
	public ListIterator<BValue<?>> listIterator() {
		return this.getValue().listIterator();
	}

	@Override
	public ListIterator<BValue<?>> listIterator(int index) {
		return this.getValue().listIterator(index);
	}

	@Override
	public boolean remove(Object o) {
		return this.getValue().remove(o);
	}

	@Override
	public BValue<?> remove(int index) {
		return this.getValue().remove(index);
	}

	@Override
	public BValue<?> set(int index, BValue<?> element) {
		return this.getValue().set(index, element);
	}

	@Override
	public int size() {
		return this.getValue().size();
	}

	@Override
	public Stream<BValue<?>> stream() {
		return this.getValue().stream();
	}

	@Override
	public List<BValue<?>> subList(int fromIndex, int toIndex) {
		return this.getValue().subList(fromIndex, toIndex);
	}

	@Override
	public BValue<?>[] toArray() {
		return (BValue[]) this.getValue().toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		return this.getValue().toArray(a);
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return this.getValue().containsAll(c);
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		return this.getValue().removeAll(c);
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		return this.getValue().retainAll(c);
	}
}