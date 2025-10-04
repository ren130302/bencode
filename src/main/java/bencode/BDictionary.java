package bencode;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public final class BDictionary implements BValue<Map<BBytes, BValue<?>>> {

  private static final BDictionary EMPTY = new BDictionary(Map.of());

  private final Map<BBytes, BValue<?>> value;

  private BDictionary(Map<BBytes, BValue<?>> value) {
    this.value = Map.copyOf(value);
  }

  @Override
  public Map<BBytes, BValue<?>> getValue() {
    return this.value;
  }

  @Override
  public boolean equals(Object obj) {
    return obj instanceof BDictionary other && this.value.equals(other.value);
  }

  @Override
  public int hashCode() {
    return this.value.hashCode();
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder("BDictionary{");
    boolean first = true;
    for (Map.Entry<BBytes, BValue<?>> entry : this.value.entrySet()) {
      if (!first) {
        sb.append(", ");
      }
      sb.append(entry.getKey().toString()).append("=").append(entry.getValue().toString());
      first = false;
    }
    sb.append("}");
    return sb.toString();
  }

  public static Builder builder() {
    return new Builder();
  }

  public static final class Builder {
    private final Map<BBytes, BValue<?>> buffer = new LinkedHashMap<>();

    public Builder put(String key, BValue<?> value) {
      this.buffer.put(BBytes.valueOf(key), Objects.requireNonNull(value));
      return this;
    }

    public Builder put(BBytes key, BValue<?> value) {
      this.buffer.put(Objects.requireNonNull(key), Objects.requireNonNull(value));
      return this;
    }

    public BDictionary build() {
      if (this.buffer.isEmpty()) {
        return EMPTY;
      }
      return new BDictionary(this.buffer);
    }
  }
}
