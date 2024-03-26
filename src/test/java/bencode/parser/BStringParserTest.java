package bencode.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import bencode.io.BEncodeParser;
import bencode.value.BString;

class BStringParserTest {

	private static final BEncodeParser PARSER = new BEncodeParser();

	private final String str_empty = "0:";
	private final BString bstr_empty = BString.valueOf("");

	private final String str_bytes = "8:" + new String(new byte[] { 7, 6, 5, 4, 3, 2, 1, 0 });
	private final BString bstr_bytes = BString.valueOf(new byte[] { 7, 6, 5, 4, 3, 2, 1, 0 });

	private final String str_string = "7:bString";
	private final BString bstr_string = BString.valueOf("bString");

	@Test
	void testReadEmpty() throws IOException {
		BString actual_bval = (BString) PARSER.readBValueFromString(this.str_empty);
		BString actual_bstr = PARSER.readBStringFromString(this.str_empty);

		assertEquals(this.bstr_empty, actual_bval);
		assertEquals(this.bstr_empty, actual_bstr);
	}

	@Test
	void testWriteEmpty() throws IOException {
		String actual_bval = PARSER.writeBValueToString(this.bstr_empty);
		String actual_bstr = PARSER.writeBStringToString(this.bstr_empty);

		assertEquals(this.str_empty, actual_bval);
		assertEquals(this.str_empty, actual_bstr);
	}

	@Test
	void testReadBytes() throws IOException {
		BString actual_bval = (BString) PARSER.readBValueFromString(this.str_bytes);
		BString actual_bstr = PARSER.readBStringFromString(this.str_bytes);

		assertEquals(this.bstr_bytes, actual_bval);
		assertEquals(this.bstr_bytes, actual_bstr);
	}

	@Test
	void testWriteBytes() throws IOException {
		String actual_bval = PARSER.writeBValueToString(this.bstr_bytes);
		String actual_bstr = PARSER.writeBStringToString(this.bstr_bytes);

		assertEquals(this.str_bytes, actual_bval);
		assertEquals(this.str_bytes, actual_bstr);
	}

	@Test
	void testReadString() throws IOException {
		BString actual_bval = (BString) PARSER.readBValueFromString(this.str_string);
		BString actual_bstr = PARSER.readBStringFromString(this.str_string);

		assertEquals(this.bstr_string, actual_bval);
		assertEquals(this.bstr_string, actual_bstr);
	}

	@Test
	void testWriteString() throws IOException {
		String actual_bval = PARSER.writeBValueToString(this.bstr_string);
		String actual_bstr = PARSER.writeBStringToString(this.bstr_string);

		assertEquals(this.str_string, actual_bval);
		assertEquals(this.str_string, actual_bstr);
	}
}
