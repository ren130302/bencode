package bencode.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import bencode.BDictionary;

class BDictionaryParserTest {
	private static final BValueParsers PARSER = new BValueParsers();

	private final String str_empty = "de";
	private final BDictionary bdict_empty = BDictionary.create();

	@Test
	void testReadEmpty() throws IOException {
		BDictionary actual_bval = (BDictionary) PARSER.readBValueFromString(this.str_empty);
		BDictionary actual_bdict = PARSER.readBDictinaryFromString(this.str_empty);

		assertEquals(this.bdict_empty, actual_bval);
		assertEquals(this.bdict_empty, actual_bdict);
	}

	@Test
	void testWriteEmpty() throws IOException {
		String actual_bval = PARSER.writeBValueToString(this.bdict_empty);
		String actual_bdict = PARSER.writeBDictinaryToString(this.bdict_empty);

		assertEquals(this.str_empty, actual_bval);
		assertEquals(this.str_empty, actual_bdict);
	}

}
