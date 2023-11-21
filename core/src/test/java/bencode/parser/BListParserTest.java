package bencode.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import bencode.BValueParsers;
import bencode.values.BDictionary;
import bencode.values.BList;
import bencode.values.BList.BListBInteger;
import bencode.values.BList.BListBString;

class BListParserTest {

	private static final BValueParsers PARSER = new BValueParsers();

	private static final String str_empty = "le";
	private static final BList<?> blist_empty = BList.create();

	private static final String str_bstr = "l0:e";
	private static final BListBString blist_bstr = BList.createBString();

	private static final String str_bint = "li0ee";
	private static final BListBInteger blist_bint = BList.createBInteger();

	private static final String str_blist = "llee";
	private static final BList<BList<?>> blist_blist = BList.create();

	private static final String str_bdict = "ldee";
	private static final BList<BDictionary> blist_bdict = BList.create();

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		blist_bstr.add("");
		blist_bint.add(0);
		blist_blist.add(BList.create());
		blist_bdict.add(BDictionary.create());
	}

	@Test
	void testReadEmpty() throws IOException {
		BList<?> actual_bval = (BList<?>) PARSER.readBValueFromString(BListParserTest.str_empty);
		BList<?> actual_blist = PARSER.readBListFromString(BListParserTest.str_empty);

		assertEquals(BListParserTest.blist_empty, actual_bval);
		assertEquals(BListParserTest.blist_empty, actual_blist);
	}

	@Test
	void testWriteEmpty() throws IOException {
		String actual_bval = PARSER.writeBValueToString(BListParserTest.blist_empty);
		String actual_blist = PARSER.writeBListToString(BListParserTest.blist_empty);

		assertEquals(BListParserTest.str_empty, actual_bval);
		assertEquals(BListParserTest.str_empty, actual_blist);
	}

	@Test
	void testReadBString() throws IOException {
		BList<?> actual_bval = (BList<?>) PARSER.readBValueFromString(BListParserTest.str_bstr);
		BList<?> actual_blist = PARSER.readBListFromString(BListParserTest.str_bstr);

		assertEquals(BListParserTest.blist_bstr, actual_bval);
		assertEquals(BListParserTest.blist_bstr, actual_blist);
	}

	@Test
	void testWriteBString() throws IOException {
		String actual_bval = PARSER.writeBValueToString(BListParserTest.blist_bstr);
		String actual_blist = PARSER.writeBListToString(BListParserTest.blist_bstr);

		assertEquals(BListParserTest.str_bstr, actual_bval);
		assertEquals(BListParserTest.str_bstr, actual_blist);
	}

	@Test
	void testReadBInteger() throws IOException {
		BList<?> actual_bval = (BList<?>) PARSER.readBValueFromString(BListParserTest.str_bint);
		BList<?> actual_blist = PARSER.readBListFromString(BListParserTest.str_bint);

		assertEquals(BListParserTest.blist_bint, actual_bval);
		assertEquals(BListParserTest.blist_bint, actual_blist);
	}

	@Test
	void testWriteBInteger() throws IOException {
		String actual_bval = PARSER.writeBValueToString(BListParserTest.blist_bint);
		String actual_blist = PARSER.writeBListToString(BListParserTest.blist_bint);

		assertEquals(BListParserTest.str_bint, actual_bval);
		assertEquals(BListParserTest.str_bint, actual_blist);
	}

	@Test
	void testReadBList() throws IOException {
		BList<?> actual_bval = (BList<?>) PARSER.readBValueFromString(BListParserTest.str_blist);
		BList<?> actual_blist = PARSER.readBListFromString(BListParserTest.str_blist);

		assertEquals(BListParserTest.blist_blist, actual_bval);
		assertEquals(BListParserTest.blist_blist, actual_blist);
	}

	@Test
	void testWriteBList() throws IOException {
		String actual_bval = PARSER.writeBValueToString(BListParserTest.blist_blist);
		String actual_blist = PARSER.writeBListToString(BListParserTest.blist_blist);

		assertEquals(BListParserTest.str_blist, actual_bval);
		assertEquals(BListParserTest.str_blist, actual_blist);
	}

	@Test
	void testReadBDictionary() throws IOException {
		BList<?> actual_bval = (BList<?>) PARSER.readBValueFromString(BListParserTest.str_bdict);
		BList<?> actual_blist = PARSER.readBListFromString(BListParserTest.str_bdict);

		assertEquals(BListParserTest.blist_bdict, actual_bval);
		assertEquals(BListParserTest.blist_bdict, actual_blist);
	}

	@Test
	void testWriteBDictionary() throws IOException {
		String actual_bval = PARSER.writeBValueToString(BListParserTest.blist_bdict);
		String actual_blist = PARSER.writeBListToString(BListParserTest.blist_bdict);

		assertEquals(BListParserTest.str_bdict, actual_bval);
		assertEquals(BListParserTest.str_bdict, actual_blist);
	}

}
