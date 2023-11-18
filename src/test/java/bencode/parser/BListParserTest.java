package bencode.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import bencode.BList;

class BListParserTest {

	private static final BValueParsers PARSER = new BValueParsers();

	private static final BValueParser PARSER_BVAL = PARSER.getBValueParser();
	private static final BListParser PARSER_BLIST = PARSER.getBListParser();

	private final String str_empty = "le";
	private final BList<?> blist_empty = BList.create();

	@Test
	void testReadEmpty() throws IOException {
		BList<?> actual_bval = (BList<?>) PARSER_BVAL.readFromString(this.str_empty);
		BList<?> actual_bint = PARSER_BLIST.readFromString(this.str_empty);

		assertEquals(this.blist_empty, actual_bval);
		assertEquals(this.blist_empty, actual_bint);
	}

	@Test
	void testWriteEmpty() throws IOException {
		String actual_bval = PARSER_BVAL.writeToString(this.blist_empty);
		String actual_bint = PARSER_BLIST.writeToString(this.blist_empty);

		assertEquals(this.str_empty, actual_bval);
		assertEquals(this.str_empty, actual_bint);
	}

}
