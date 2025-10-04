package bencode;

import java.io.IOException;
import java.nio.ByteBuffer;

public final class BEncodeInputStream {

  private final ByteBuffer buffer;

  public BEncodeInputStream(byte[] bytes) {
    this.buffer = ByteBuffer.wrap(bytes);
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

  public void unread() throws IOException {
    if (this.buffer.position() == 0) {
      throw new IOException("Cannot unread at position 0");
    }
    this.buffer.position(this.buffer.position() - 1);
  }

  public byte[] readBytes(int length) throws IOException {
    if (this.buffer.remaining() < length) {
      throw new IOException("Unexpected EOF");
    }
    byte[] dst = new byte[length];
    this.buffer.get(dst);
    return dst;
  }

  public BBytes readBBytes() throws IOException {
    // length parsing
    int length = 0;
    int c;
    while ((c = this.read()) != -1) {
      if (c == BEncodeConstants.CORON) {
        break;
      }
      length = length * 10 + (c - '0');
      if (length < 0) {
        throw new IOException("Byte length overflow");
      }
    }
    if (c != BEncodeConstants.CORON) {
      throw new IOException("Expected ':' after length");
    }
    return BBytes.valueOf(this.readBytes(length));
  }

  public BInteger readBInteger() throws IOException {
    if (this.read() != BEncodeConstants.INTEGER) {
      throw new IOException("Expected 'i'");
    }
    long value = 0;
    boolean negative = false;
    int c = this.read();
    if (c == '-') {
      negative = true;
      c = this.read();
    }
    if (c < '0' || c > '9') {
      throw new IOException("Invalid integer");
    }
    value = c - '0';
    while ((c = this.read()) != BEncodeConstants.END) {
      if (c < '0' || c > '9') {
        throw new IOException("Invalid integer digit");
      }
      value = value * 10 + (c - '0');
    }
    return BInteger.valueOf(negative ? -value : value);
  }

  @SuppressWarnings("unchecked")
  public <E extends BValue<?>> BList<E> readBList() throws IOException {
    if (this.read() != BEncodeConstants.LIST) {
      throw new IOException("Expected 'l'");
    }
    BList.Builder<E> builder = BList.builder();
    while (true) {
      int c = this.peek();
      if (c == BEncodeConstants.END) {
        this.read();
        break;
      }
      builder.add((E) this.readBValue());
    }
    return builder.build();
  }

  public BDictionary readBDict() throws IOException {
    if (this.read() != BEncodeConstants.DICTIONARY) {
      throw new IOException("Expected 'd'");
    }
    BDictionary.Builder builder = BDictionary.builder();
    while (true) {
      int c = this.peek();
      if (c == BEncodeConstants.END) {
        this.read();
        break;
      }
      BBytes key = this.readBBytes();
      BValue<?> value = this.readBValue();
      builder.put(key, value);
    }
    return builder.build();
  }

  public BValue<?> readBValue() throws IOException {
    int c = this.peek();
    if (c >= '0' && c <= '9') {
      return this.readBBytes();
    }
    switch (c) {
      case BEncodeConstants.INTEGER:
        return this.readBInteger();
      case BEncodeConstants.LIST:
        return this.readBList();
      case BEncodeConstants.DICTIONARY:
        return this.readBDict();
      default:
        break;
    }
    throw new IOException("Unknown BEncode type: '" + (char) c + "'");
  }
}
