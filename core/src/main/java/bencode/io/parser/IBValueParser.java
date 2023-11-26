package bencode.io.parser;

import java.nio.charset.Charset;

import bencode.io.BValueDeserializer;
import bencode.io.BValueParsers;
import bencode.io.BValueSerializer;
import bencode.values.BValue;

public interface IBValueParser<T extends BValue<?>> extends BValueSerializer<T>, BValueDeserializer<T> {

	BValueParsers getParsers();

	default Charset getCharset() {
		return this.getParsers().getCharset();
	}

}
