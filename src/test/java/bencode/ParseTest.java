package bencode;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import bencode.parser.BDictionaryParser;
import bencode.parser.BIntegerParser;
import bencode.parser.BListParser;
import bencode.parser.BStringParser;
import bencode.parser.BValueParser;
import bencode.parser.BValueParsers;

//@Log
public class ParseTest {
	private final BValueParsers valueParsers = new BValueParsers(StandardCharsets.UTF_8);

	@Test
	public void torrent() throws IOException {
		this.assertTorrent("bittorrent-v2-test.torrent");
		this.assertTorrent("bittorrent-v2-hybrid-test.torrent");
	}

	private void assertTorrent(String filename) throws IOException {
		final ByteBuffer data;
		try (InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(filename)) {
			data = ByteBuffer.wrap(inputStream.readAllBytes());
		}
		BDictionary desirialized = this.valueParsers.getBDictionaryParser().readFromByteBuffer(data);
		ByteBuffer serialized = this.valueParsers.getBDictionaryParser().writeToByteBuffer(desirialized);

//		BDictionary desirialized2 = this.valueParsers.getBDictionaryParser().readFromByteBuffer(serialized);
//		System.out.println(data);
//		System.out.println(serialized);
		BDictionary desirialized2 = this.valueParsers.getBDictionaryParser().readFromByteBuffer(serialized);
		ByteBuffer serialized2 = this.valueParsers.getBDictionaryParser().writeToByteBuffer(desirialized2);
//		System.out.println(serialized);
//		System.out.println(serialized2);

		assertEquals(desirialized.toString(), desirialized2.toString());
	}

	private void assertBValue(BValue<?> bValue) throws IOException {
		final BValueParser valueParser = this.valueParsers.getBValueParser();

		String serialized = valueParser.writeToString(bValue);
		System.out.println(serialized);

		BValue<?> desirialized = valueParser.readFromString(serialized);
		System.out.println(desirialized);

		Assertions.assertEquals(bValue, desirialized);
	}

	private void assertBString(BString bValue) throws IOException {
		final BStringParser valueParsers = this.valueParsers.getBStringParser();

		String serialized = valueParsers.writeToString(bValue);
		System.out.println(serialized);

		BString desirialized = valueParsers.readFromString(serialized);
		System.out.println(desirialized);

		Assertions.assertEquals(bValue, desirialized);
	}

	private void assertBInteger(BInteger bValue) throws IOException {
		final BIntegerParser valueParsers = this.valueParsers.getBIntegerParser();

		String serialized = valueParsers.writeToString(bValue);
		System.out.println(serialized);

		BInteger desirialized = valueParsers.readFromString(serialized);
		System.out.println(desirialized);

		Assertions.assertEquals(bValue, desirialized);
	}

	private void assertBList(BList bValue) throws IOException {
		final BListParser valueParsers = this.valueParsers.getBListParser();

		String serialized = valueParsers.writeToString(bValue);
		System.out.println(serialized);

		BList desirialized = valueParsers.readFromString(serialized);
		System.out.println(desirialized);

		Assertions.assertEquals(bValue, desirialized);
	}

	private void assertBDictionary(BDictionary bValue) throws IOException {
		final BDictionaryParser valueParsers = this.valueParsers.getBDictionaryParser();

		String serialized = valueParsers.writeToString(bValue);
		System.out.println(serialized);

		BDictionary desirialized = valueParsers.readFromString(serialized);
		System.out.println(desirialized);

		Assertions.assertEquals(bValue, desirialized);
	}
}
