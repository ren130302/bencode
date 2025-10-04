package bencode;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public final class BBytes implements BValue<byte[]> {
  private final byte[] value;
  private final int hash;

  private BBytes(byte[] value) {
    this.value = Arrays.copyOf(value, value.length);
    this.hash = Arrays.hashCode(value);
  }

  public static BBytes valueOf(byte[] bytes) {
    return new BBytes(bytes);
  }

  public static BBytes valueOf(String s) {
    return new BBytes(s.getBytes());
  }

  public static BBytes valueOf(String s, Charset charset) {
    return new BBytes(s.getBytes(charset));
  }

  @Override
  public byte[] getValue() {
    return Arrays.copyOf(this.value, this.value.length);
  }

  @Override
  public boolean equals(Object obj) {
    return obj instanceof BBytes other && Arrays.equals(this.value, other.value);
  }

  @Override
  public int hashCode() {
    return this.hash;
  }

  public String toString(Charset charset) {
    String str = new String(this.value, charset);
    StringBuilder sb = new StringBuilder();
    sb.append("BBytes[hex:");
    for (int i = 0; i < this.value.length; i++) {
      sb.append(String.format("%02X", this.value[i]));
      if (i < this.value.length - 1) {
        sb.append(" ");
      }
    }
    sb.append(", str:").append(str).append("]");
    return sb.toString();
  }

  @Override
  public String toString() {
    return this.toString(StandardCharsets.UTF_8);
  }

}
