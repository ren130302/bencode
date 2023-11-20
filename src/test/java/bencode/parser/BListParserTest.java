package bencode.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import bencode.BList;

class BListParserTest {

	private static final BValueParsers PARSER = new BValueParsers();

	private final String str_empty = "le";
	private final BList<?> blist_empty = BList.create();

	@Test
	void testReadEmpty() throws IOException {
		BList<?> actual_bval = (BList<?>) PARSER.readBValueFromString(this.str_empty);
		BList<?> actual_blist = PARSER.readBListFromString(this.str_empty);

		assertEquals(this.blist_empty, actual_bval);
		assertEquals(this.blist_empty, actual_blist);
	}

	@Test
	void testWriteEmpty() throws IOException {
		String actual_bval = PARSER.writeBValueToString(this.blist_empty);
		String actual_blist = PARSER.writeBListToString(this.blist_empty);

		assertEquals(this.str_empty, actual_bval);
		assertEquals(this.str_empty, actual_blist);
	}

}
