package bencode;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class BList<E extends BValue<?>> implements BValue<List<E>> {

  private static final BList<?> EMPTY = new BList<>(List.of());

  private final List<E> value;

  private BList(List<E> value) {
    this.value = List.copyOf(value);
  }

  @Override
  public List<E> getValue() {
    return this.value;
  }

  @Override
  public boolean equals(Object obj) {
    return obj instanceof BList<?> other && this.value.equals(other.value);
  }

  @Override
  public int hashCode() {
    return this.value.hashCode();
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder("BList[");
    for (int i = 0; i < this.value.size(); i++) {
      sb.append(this.value.get(i).toString());
      if (i < this.value.size() - 1) {
        sb.append(", ");
      }
    }
    sb.append("]");
    return sb.toString();
  }

  public static <E extends BValue<?>> Builder<E> builder() {
    return new Builder<>();
  }

  public static final class Builder<E extends BValue<?>> {
    private final List<E> buffer = new ArrayList<>();

    public Builder<E> add(E value) {
      this.buffer.add(Objects.requireNonNull(value));
      return this;
    }

    public Builder<E> addAll(List<E> list) {
      this.buffer.addAll(list);
      return this;
    }

    @SuppressWarnings("unchecked")
    public BList<E> build() {
      if (this.buffer.isEmpty()) {
        return (BList<E>) EMPTY;
      }
      return new BList<>(this.buffer);
    }
  }
}
