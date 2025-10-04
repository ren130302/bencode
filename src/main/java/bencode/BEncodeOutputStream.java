package bencode;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Map;

public final class BEncodeOutputStream {

  private ByteBuffer buffer;
  private static final int DEFAULT_CAPACITY = 1024;

  public BEncodeOutputStream() {
    this.buffer = ByteBuffer.allocate(DEFAULT_CAPACITY);
  }

  private void ensureCapacity(int additional) {
    if (this.buffer.remaining() < additional) {
      int newCapacity = Math.max(this.buffer.capacity() * 2, this.buffer.position() + additional);
      ByteBuffer newBuf = ByteBuffer.allocate(newCapacity);
      this.buffer.flip();
      newBuf.put(this.buffer);
      this.buffer = newBuf;
    }
  }

  public void writeByte(byte b) {
    this.ensureCapacity(1);
    this.buffer.put(b);
  }

  public void writeBBytes(BBytes value) throws IOException {
    byte[] bytes = value.getValue();
    this.writeLongDirect(bytes.length);
    this.writeByte(BEncodeConstants.CORON);
    this.writeBytes(bytes);
  }

  public void writeBInt(BInteger value) throws IOException {
    this.writeByte(BEncodeConstants.INTEGER);
    this.writeLongDirect(value.getValue());
    this.writeByte(BEncodeConstants.END);
  }

  public void writeBList(BList<?> list) throws IOException {
    this.writeByte(BEncodeConstants.LIST);
    for (BValue<?> v : list.getValue()) {
      this.writeBValue(v);
    }
    this.writeByte(BEncodeConstants.END);
  }

  public void writeBDict(BDictionary dict) throws IOException {
    this.writeByte(BEncodeConstants.DICTIONARY);
    for (Map.Entry<BBytes, BValue<?>> e : dict.getValue().entrySet()) {
      this.writeBBytes(e.getKey());
      this.writeBValue(e.getValue());
    }
    this.writeByte(BEncodeConstants.END);
  }

  public void writeBValue(BValue<?> value) throws IOException {
    if (value instanceof BBytes v) {
      this.writeBBytes(v);
    } else if (value instanceof BInteger v) {
      this.writeBInt(v);
    } else if (value instanceof BList<?> v) {
      this.writeBList(v);
    } else if (value instanceof BDictionary v) {
      this.writeBDict(v);
    }
  }

  private void writeBytes(byte[] bytes) {
    this.ensureCapacity(bytes.length);
    this.buffer.put(bytes);
  }

  private void writeLongDirect(long value) {
    if (value == 0) {
      this.writeByte((byte) '0');
      return;
    }
    boolean negative = value < 0;
    long n = negative ? -value : value;
    int len = 0;
    long t = n;
    while (t > 0) {
      t /= 10;
      len++;
    }
    if (negative) {
      len++;
    }
    this.ensureCapacity(len);
    int pos = this.buffer.position() + len - 1;
    while (n > 0) {
      this.buffer.put(pos--, (byte) ('0' + n % 10));
      n /= 10;
    }
    if (negative) {
      this.buffer.put(this.buffer.position(), (byte) '-');
    }
    this.buffer.position(this.buffer.position() + len);
  }

  public byte[] toByteArray() {
    ByteBuffer copy = this.buffer.duplicate();
    copy.flip();
    byte[] arr = new byte[copy.remaining()];
    copy.get(arr);
    return arr;
  }
}
