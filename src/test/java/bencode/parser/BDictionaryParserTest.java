package bencode.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import bencode.BDictionary;

class BDictionaryParserTest {
	private static final BValueParsers PARSER = new BValueParsers();

	private static final BValueParser PARSER_BVAL = PARSER.getBValueParser();
	private static final BDictionaryParser PARSER_BDICT = PARSER.getBDictionaryParser();

	private final String str_empty = "de";
	private final BDictionary bdict_empty = BDictionary.create();

	@Test
	void testReadEmpty() throws IOException {
		BDictionary actual_bval = (BDictionary) PARSER_BVAL.readFromString(this.str_empty);
		BDictionary actual_bint = PARSER_BDICT.readFromString(this.str_empty);

		assertEquals(this.bdict_empty, actual_bval);
		assertEquals(this.bdict_empty, actual_bint);
	}

	@Test
	void testWriteEmpty() throws IOException {
		String actual_bval = PARSER_BVAL.writeToString(this.bdict_empty);
		String actual_bint = PARSER_BDICT.writeToString(this.bdict_empty);

		assertEquals(this.str_empty, actual_bval);
		assertEquals(this.str_empty, actual_bint);
	}

}
