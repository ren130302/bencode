package bencode.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import bencode.BValueParsers;
import bencode.values.BInteger;

class BIntegerParserTest {

	private static final BValueParsers PARSER = new BValueParsers();

	private static final String str_zero = "i0e";
	private static final BInteger bint_zero = BInteger.valueOf(0);

	private static final String str_negative = "i-100e";
	private static final BInteger bint_negative = BInteger.valueOf(-100);

	private static final String str_positive = "i100e";
	private static final BInteger bint_positive = BInteger.valueOf(100);

	@Test
	void testReadZero() throws IOException {
		BInteger actual_bval = (BInteger) PARSER.readBValueFromString(BIntegerParserTest.str_zero);
		BInteger actual_bint = PARSER.readBIntegerFromString(BIntegerParserTest.str_zero);

		assertEquals(BIntegerParserTest.bint_zero, actual_bval);
		assertEquals(BIntegerParserTest.bint_zero, actual_bint);
	}

	@Test
	void testWriteZero() throws IOException {
		String actual_bval = PARSER.writeBValueToString(BIntegerParserTest.bint_zero);
		String actual_bint = PARSER.writeBIntegerToString(BIntegerParserTest.bint_zero);

		assertEquals(BIntegerParserTest.str_zero, actual_bval);
		assertEquals(BIntegerParserTest.str_zero, actual_bint);
	}

	@Test
	void testReadNegativeValue() throws IOException {
		BInteger actual_bval = (BInteger) PARSER.readBValueFromString(BIntegerParserTest.str_negative);
		BInteger actual_bint = PARSER.readBIntegerFromString(BIntegerParserTest.str_negative);

		assertEquals(BIntegerParserTest.bint_negative, actual_bval);
		assertEquals(BIntegerParserTest.bint_negative, actual_bint);
	}

	@Test
	void testWriteNegativeValue() throws IOException {
		String actual_bval = PARSER.writeBValueToString(BIntegerParserTest.bint_negative);
		String actual_bint = PARSER.writeBIntegerToString(BIntegerParserTest.bint_negative);

		assertEquals(BIntegerParserTest.str_negative, actual_bval);
		assertEquals(BIntegerParserTest.str_negative, actual_bint);
	}

	@Test
	void testReadPositiveValue() throws IOException {
		BInteger actual_bval = (BInteger) PARSER.readBValueFromString(BIntegerParserTest.str_positive);
		BInteger actual_bint = PARSER.readBIntegerFromString(BIntegerParserTest.str_positive);

		assertEquals(BIntegerParserTest.bint_positive, actual_bval);
		assertEquals(BIntegerParserTest.bint_positive, actual_bint);
	}

	@Test
	void testWritePositiveValue() throws IOException {
		String actual_bval = PARSER.writeBValueToString(BIntegerParserTest.bint_positive);
		String actual_bint = PARSER.writeBIntegerToString(BIntegerParserTest.bint_positive);

		assertEquals(BIntegerParserTest.str_positive, actual_bval);
		assertEquals(BIntegerParserTest.str_positive, actual_bint);
	}

}
