package bencode.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import bencode.io.BEncodeParser;
import bencode.value.BDictionary;
import bencode.value.BList;

class BDictionaryParserTest {
	private static final BEncodeParser PARSER = new BEncodeParser();

	private static final String str_empty = "de";
	private static final BDictionary bdict_empty = BDictionary.create();

	private static final String str_bstr = "d0:0:e";
	private static final BDictionary bdict_bstr = BDictionary.create();

	private static final String str_bint = "d0:i0ee";
	private static final BDictionary bdict_bint = BDictionary.create();

	private static final String str_blist = "d0:lee";
	private static final BDictionary bdict_blist = BDictionary.create();

	private static final String str_bdict = "d0:dee";
	private static final BDictionary bdict_bdict = BDictionary.create();

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		bdict_bstr.put("", "");
		bdict_bint.put("", 0);
		bdict_blist.put("", BList.create());
		bdict_bdict.put("", BDictionary.create());
	}

	@Test
	void testReadEmpty() throws IOException {
		BDictionary actual_bval = (BDictionary) PARSER.readBValueFromString(BDictionaryParserTest.str_empty);
		BDictionary actual_bdict = PARSER.readBDictionaryFromString(BDictionaryParserTest.str_empty);

		assertEquals(BDictionaryParserTest.bdict_empty, actual_bval);
		assertEquals(BDictionaryParserTest.bdict_empty, actual_bdict);
	}

	@Test
	void testWriteEmpty() throws IOException {
		String actual_bval = PARSER.writeBValueToString(BDictionaryParserTest.bdict_empty);
		String actual_bdict = PARSER.writeBDictionaryToString(BDictionaryParserTest.bdict_empty);

		assertEquals(BDictionaryParserTest.str_empty, actual_bval);
		assertEquals(BDictionaryParserTest.str_empty, actual_bdict);
	}

	@Test
	void testReadBString() throws IOException {
		BDictionary actual_bval = (BDictionary) PARSER.readBValueFromString(str_bstr);
		BDictionary actual_bdict = PARSER.readBDictionaryFromString(str_bstr);

		assertEquals(bdict_bstr, actual_bval);
		assertEquals(bdict_bstr, actual_bdict);
	}

	@Test
	void testWriteBString() throws IOException {
		String actual_bval = PARSER.writeBValueToString(bdict_bstr);
		String actual_bdict = PARSER.writeBDictionaryToString(bdict_bstr);

		assertEquals(str_bstr, actual_bval);
		assertEquals(str_bstr, actual_bdict);
	}

	@Test
	void testReadBInteger() throws IOException {
		BDictionary actual_bval = (BDictionary) PARSER.readBValueFromString(str_bint);
		BDictionary actual_bdict = PARSER.readBDictionaryFromString(str_bint);

		assertEquals(bdict_bint, actual_bval);
		assertEquals(bdict_bint, actual_bdict);
	}

	@Test
	void testWriteBInteger() throws IOException {
		String actual_bval = PARSER.writeBValueToString(BDictionaryParserTest.bdict_bint);
		String actual_bdict = PARSER.writeBDictionaryToString(BDictionaryParserTest.bdict_bint);

		assertEquals(BDictionaryParserTest.str_bint, actual_bval);
		assertEquals(BDictionaryParserTest.str_bint, actual_bdict);
	}

	@Test
	void testReadBList() throws IOException {
		BDictionary actual_bval = (BDictionary) PARSER.readBValueFromString(BDictionaryParserTest.str_blist);
		BDictionary actual_bdict = PARSER.readBDictionaryFromString(BDictionaryParserTest.str_blist);

		assertEquals(BDictionaryParserTest.bdict_blist, actual_bval);
		assertEquals(BDictionaryParserTest.bdict_blist, actual_bdict);
	}

	@Test
	void testWriteBList() throws IOException {
		String actual_bval = PARSER.writeBValueToString(BDictionaryParserTest.bdict_blist);
		String actual_bdict = PARSER.writeBDictionaryToString(BDictionaryParserTest.bdict_blist);

		assertEquals(BDictionaryParserTest.str_blist, actual_bval);
		assertEquals(BDictionaryParserTest.str_blist, actual_bdict);
	}

	@Test
	void testReadBDictionary() throws IOException {
		BDictionary actual_bval = (BDictionary) PARSER.readBValueFromString(BDictionaryParserTest.str_bdict);
		BDictionary actual_bdict = PARSER.readBDictionaryFromString(BDictionaryParserTest.str_bdict);

		assertEquals(BDictionaryParserTest.bdict_bdict, actual_bval);
		assertEquals(BDictionaryParserTest.bdict_bdict, actual_bdict);
	}

	@Test
	void testWriteBDictionary() throws IOException {
		String actual_bval = PARSER.writeBValueToString(BDictionaryParserTest.bdict_bdict);
		String actual_bdict = PARSER.writeBDictionaryToString(BDictionaryParserTest.bdict_bdict);

		assertEquals(BDictionaryParserTest.str_bdict, actual_bval);
		assertEquals(BDictionaryParserTest.str_bdict, actual_bdict);
	}
}
