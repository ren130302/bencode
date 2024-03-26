package bencode.io;

import static bencode.io.ConstCharacter.CORON;
import static bencode.io.ConstCharacter.DICTIONARY;
import static bencode.io.ConstCharacter.END;
import static bencode.io.ConstCharacter.INTEGER;
import static bencode.io.ConstCharacter.LIST;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;
import java.nio.charset.Charset;

public final class BEncodeOutputStream implements Closeable, Flushable {

    private final ByteArrayOutputStream _stream;
    private final Charset _charset;

    public BEncodeOutputStream() {
        this(Charset.defaultCharset());
    }

    public BEncodeOutputStream(String charsetName) {
        this._stream = new ByteArrayOutputStream();
        this._charset = Charset.forName(charsetName);
    }

    public BEncodeOutputStream(Charset charset) {
        this._stream = new ByteArrayOutputStream();
        this._charset = charset;
    }

    @Override
    public void flush() throws IOException {
        this._stream.flush();
    }

    @Override
    public void close() throws IOException {
        this._stream.close();
    }

    public String getString() {
        return new String(this._stream.toByteArray());
    }

    public String getString(Charset charset) {
        return new String(this._stream.toByteArray(), charset);
    }

    public String getString(String charsetName) {
        return new String(this._stream.toByteArray(), Charset.forName(charsetName));
    }

    public byte[] getBytes() {
        return this._stream.toByteArray();
    }

    public void writeInt(int b) throws IOException {
        this._stream.write(Integer.toString(b).getBytes(this._charset));
    }

    public void writeChar(char b) throws IOException {
        this._stream.write(b);
    }

    public void writeBytes(byte[] b) throws IOException {
        this._stream.write(b);
    }

    public void writeLong(long b) throws IOException {
        this._stream.write(Long.toString(b).getBytes(this._charset));
    }

    public void writeIntCode() throws IOException {
        this.writeChar(INTEGER);
    }

    public void writeDictCode() throws IOException {
        this.writeChar(DICTIONARY);
    }

    public void writeListCode() throws IOException {
        this.writeChar(LIST);
    }

    public void writeEndCode() throws IOException {
        this.writeChar(END);
    }

    public void writeCoronCode() throws IOException {
        this.writeChar(CORON);
    }

    public IllegalArgumentException unknownBEncodeType(Object obj) {
        return new IllegalArgumentException("Unknown bencode type. " + obj);
    }

}
