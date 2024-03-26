package bencode.value;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class BList<E extends BValue<?>> implements BValue<List<E>>, List<E> {

    private static final long serialVersionUID = 1551379075504078235L;

    public static <T extends BValue<?>> BList<T> create() {
        return new BList<>();
    }

    public static <E extends BValue<?>> BList<E> create(Collection<E> c) {
        return new BList<>(c);
    }

    private final ArrayList<E> value;

    private BList() {
        this.value = new ArrayList<>();
    }

    private BList(Collection<E> c) {
        this.value = new ArrayList<>(c);
    }

    @Override
    public List<E> getValue() {
        return this;
    }

    @Override
    public boolean equals(Object obj) {
        return this.getValue().equals(obj);
    }

    @Override
    public int hashCode() {
        return this.getValue().hashCode();
    }

    @SuppressWarnings("unchecked")
    @Override
    public BList<E> clone() {
        return new BList<>((Collection<E>) this.value.clone());
    }

    @Override
    public int size() {
        return this.getValue().size();
    }

    @Override
    public boolean isEmpty() {
        return this.getValue().isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return this.getValue().contains(o);
    }

    @Override
    public Iterator<E> iterator() {
        return this.getValue().iterator();
    }

    @Override
    public Object[] toArray() {
        return this.getValue().toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return this.getValue().toArray(a);
    }

    @Override
    public boolean add(E e) {
        return this.getValue().add(e);
    }

    @Override
    public boolean remove(Object o) {
        return this.getValue().remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return this.getValue().containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        return this.getValue().addAll(c);
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        return this.getValue().addAll(index, c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return this.getValue().removeAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return this.getValue().retainAll(c);
    }

    @Override
    public void clear() {
        this.getValue().clear();
    }

    @Override
    public E get(int index) {
        return this.getValue().get(index);
    }

    @Override
    public E set(int index, E element) {
        return this.getValue().set(index, element);
    }

    @Override
    public void add(int index, E element) {
        this.getValue().add(index, element);
    }

    @Override
    public E remove(int index) {
        return this.getValue().remove(index);
    }

    @Override
    public int indexOf(Object o) {
        return this.getValue().indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return this.getValue().lastIndexOf(o);
    }

    @Override
    public ListIterator<E> listIterator() {
        return this.getValue().listIterator();
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        return this.getValue().listIterator(index);
    }

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        return this.getValue().subList(fromIndex, toIndex);
    }
}
