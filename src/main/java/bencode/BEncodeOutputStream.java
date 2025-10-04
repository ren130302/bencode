package bencode;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
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

  public void writeBBytes(BBytes value) {
    byte[] bytes = value.getValue();
    this.writeLongDirect(bytes.length);
    this.writeByte(BEncodeConstants.CORON);
    this.writeBytes(bytes);
  }

  public void writeBInt(BInteger value) {
    this.writeByte(BEncodeConstants.INTEGER);
    this.writeLongDirect(value.getValue());
    this.writeByte(BEncodeConstants.END);
  }

  public void writeBList(BList<?> list) {
    this.writeByte(BEncodeConstants.LIST);
    list.getValue().forEach(this::writeBValue);
    this.writeByte(BEncodeConstants.END);
  }

  public void writeBDict(BDictionary dict) {
    this.writeByte(BEncodeConstants.DICTIONARY);
    dict.getValue().entrySet().stream().sorted(Map.Entry.comparingByKey()).forEach(e -> {
      this.writeBBytes(e.getKey());
      this.writeBValue(e.getValue());
    });
    this.writeByte(BEncodeConstants.END);
  }

  public void writeBValue(BValue<?> value) {
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
    this.writeBytes(Long.toString(value).getBytes(StandardCharsets.US_ASCII));
  }


  public byte[] toByteArray() {
    ByteBuffer copy = this.buffer.duplicate();
    copy.flip();
    byte[] arr = new byte[copy.remaining()];
    copy.get(arr);
    return arr;
  }
}
