package bencode;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

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
	public void test() throws IOException {

		try (InputStream inputStream = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream("bittorrent-v2-test.torrent")) {
//			log.fine(text);
			BDictionary desirialized = this.valueParsers.getBDictionaryParser()
					.readFromByteBuffer(ByteBuffer.wrap(inputStream.readAllBytes()));
//			log.fine("" + desirialized);
			System.out.println(desirialized);
			ByteBuffer serialized = this.valueParsers.getBDictionaryParser().writeToByteBuffer(desirialized);
//			log.fine(serialized);
			System.out.println(serialized);
		}

		try (InputStream inputStream = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream("bittorrent-v2-hybrid-test.torrent")) {
//			log.fine(text);
			BDictionary desirialized = this.valueParsers.getBDictionaryParser()
					.readFromByteBuffer(ByteBuffer.wrap(inputStream.readAllBytes()));
//			log.fine("" + desirialized);
			System.out.println(desirialized);
			ByteBuffer serialized = this.valueParsers.getBDictionaryParser().writeToByteBuffer(desirialized);
//			log.fine(serialized);
			System.out.println(serialized);
		}
		BString bString = BString.valueOf("text");
		this.assertBValue(bString);
		this.assertBString(bString);

		BString bStringEmpty = BString.valueOf("");
		this.assertBValue(bStringEmpty);
		this.assertBString(bStringEmpty);

		BInteger bInteger = BInteger.valueOf(100);
		this.assertBValue(bInteger);
		this.assertBInteger(bInteger);

		BList bListEmpty = BList.create(List.of());
		this.assertBValue(bListEmpty);
		this.assertBList(bListEmpty);

		BList bList = BList.create(List.of(bInteger, bInteger, bInteger, bInteger, bInteger));
		this.assertBValue(bList);
		this.assertBList(bList);

		BDictionary bDictEmpty = BDictionary.create(Map.of());
		this.assertBValue(bDictEmpty);
		this.assertBDictionary(bDictEmpty);

		BDictionary bDict = BDictionary.create(Map.of());
		this.assertBValue(bDict);
		this.assertBDictionary(bDict);
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
