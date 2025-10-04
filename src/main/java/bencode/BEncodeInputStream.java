package bencode;

import java.nio.ByteBuffer;
import java.util.Objects;

public final class BEncodeInputStream {

  private final ByteBuffer buffer;

  public BEncodeInputStream(byte[] bytes) {
    this.buffer = ByteBuffer.wrap(Objects.requireNonNull(bytes, "bytes must not be null"));
  }

  public boolean hasRemaining() {
    return this.buffer.hasRemaining();
  }

  public int read() {
    return this.hasRemaining() ? (this.buffer.get() & 0xFF) : -1;
  }

  public int peek() {
    return this.hasRemaining() ? (this.buffer.get(this.buffer.position()) & 0xFF) : -1;
  }

  public void unread() {
    if (this.buffer.position() == 0) {
      throw new IllegalStateException("Cannot unread at position 0");
    }
    this.buffer.position(this.buffer.position() - 1);
  }

  @FunctionalInterface
  public interface BReader<E> {
    E read(BEncodeInputStream in);
  }

  public BValue<?> readBValue() {
    int c = this.peek();
    if (c == -1) {
      throw new IllegalStateException("Unexpected EOF while reading BValue");
    }

    return switch (c) {
      case BEncodeConstants.INTEGER -> this.readBInteger();
      case BEncodeConstants.LIST -> this.readBList(BEncodeInputStream::readBValue);
      case BEncodeConstants.DICTIONARY -> this.readBDict();
      default -> {
        if ((c < '0') || (c > '9')) {
          throw new IllegalArgumentException("Unknown BEncode type: '" + (char) c + "'");
        }
        yield this.readBBytes();
      }
    };
  }

  public BBytes readBBytes() {
    long length = this.readDecimalNumber();
    return BBytes.valueOf(this.readBytesExact(length));
  }


  private byte[] readBytesExact(long length) {
    if (length > Integer.MAX_VALUE) {
      throw new IllegalArgumentException("Byte length too large: " + length);
    }
    int len = (int) length;
    if (this.buffer.remaining() < len) {
      throw new IllegalStateException("Unexpected EOF while reading " + len + " bytes");
    }
    byte[] dst = new byte[len];
    this.buffer.get(dst);
    return dst;
  }

  public BInteger readBInteger() {
    this.expectChar(BEncodeConstants.INTEGER, "'i' expected at start of integer");

    boolean negative = false;
    int c = this.read();
    if (c == '-') {
      negative = true;
      c = this.read();
      if (c == '0') {
        throw new IllegalArgumentException("Negative zero not allowed");
      }
    }

    if (c < '0' || c > '9') {
      throw new IllegalArgumentException("Invalid integer format");
    }

    long value = c - '0';
    while ((c = this.read()) != BEncodeConstants.END) {
      if (c < '0' || c > '9') {
        throw new IllegalArgumentException("Invalid integer format");
      }
      value = value * 10 + (c - '0');
      if (value < 0) {
        throw new IllegalArgumentException("Integer overflow");
      }
    }
    return BInteger.valueOf(negative ? -value : value);
  }

  public <E extends BValue<?>> BList<E> readBList(BReader<E> elementReader) {
    this.expectChar(BEncodeConstants.LIST, "'l' expected at start of list");
    BList.Builder<E> builder = BList.builder();
    int c;
    while ((c = this.peek()) != BEncodeConstants.END) {
      if (c == -1) {
        throw new IllegalStateException("Unexpected EOF while reading list");
      }
      builder.add(elementReader.read(this));
    }
    this.read(); // consume 'e'
    return builder.build();
  }

  public BList<?> readBList() {
    return this.readBList(BEncodeInputStream::readBValue);
  }

  public BDictionary readBDict() {
    this.expectChar(BEncodeConstants.DICTIONARY, "'d' expected at start of dictionary");
    BDictionary.Builder builder = BDictionary.builder();
    BBytes previousKey = null;

    int c;
    while ((c = this.peek()) != BEncodeConstants.END) {
      if (c == -1) {
        throw new IllegalStateException("Unexpected EOF while reading dictionary");
      }

      BBytes key = this.readBBytes();

      if (previousKey != null && previousKey.compareTo(key) >= 0) {
        throw new IllegalArgumentException("Dictionary keys not in sorted order");
      }

      previousKey = key;

      BValue<?> value = this.readBValue();
      builder.put(key, value);
    }
    this.read(); // consume 'e'
    return builder.build();
  }

  private long readDecimalNumber() {
    long result = 0;
    int c = this.read();
    if (c < '0' || c > '9') {
      throw new IllegalArgumentException(
          "Expected digit while reading number, got: '" + (char) c + "'");
    }
    do {
      result = result * 10 + (c - '0');
      if (result < 0) {
        throw new IllegalArgumentException("Byte length overflow detected");
      }
      c = this.read();
    } while (c >= '0' && c <= '9');

    if (c != BEncodeConstants.CORON) {
      throw new IllegalArgumentException("':' expected after number, but got: '" + (char) c + "'");
    }
    return result;
  }

  private void expectChar(int expected, String message) {
    int c = this.read();
    if (c != expected) {
      throw new IllegalArgumentException(message + ", but got: '" + (char) c + "'");
    }
  }

}
