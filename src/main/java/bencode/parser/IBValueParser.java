package bencode.parser;

import java.nio.charset.Charset;

import bencode.BValue;

public interface IBValueParser<T extends BValue<?>> extends BValueSerializer<T>, BValueDeserializer<T> {

	BValueParsers getParsers();

	default Charset getCharset() {
		return this.getParsers().getCharset();
	}

}
