package bencode;

public final class BInteger implements BValue<Long> {

  private final Long value;

  private BInteger(long value) {
    this.value = Long.valueOf(value);
  }

  public static BInteger valueOf(long value) {
    return new BInteger(value);
  }

  @Override
  public Long getValue() {
    return this.value;
  }

  @Override
  public boolean equals(Object obj) {
    return obj instanceof BInteger other && this.value == other.value;
  }

  @Override
  public int hashCode() {
    return Long.hashCode(this.value);
  }

  @Override
  public String toString() {
    return "BInteger[" + this.value + "]";
  }

}
