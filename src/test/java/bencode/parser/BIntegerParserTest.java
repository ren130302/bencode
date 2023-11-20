package bencode.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import bencode.BInteger;

class BIntegerParserTest {

	private static final BValueParsers PARSER = new BValueParsers();

	private final String str_zero = "i0e";
	private final BInteger bint_zero = BInteger.valueOf(0);

	private final String str_negative = "i-100e";
	private final BInteger bint_negative = BInteger.valueOf(-100);

	private final String str_positive = "i100e";
	private final BInteger bint_positive = BInteger.valueOf(100);

	@Test
	void testReadZero() throws IOException {
		BInteger actual_bval = (BInteger) PARSER.readBValueFromString(this.str_zero);
		BInteger actual_bint = PARSER.readBIntegerFromString(this.str_zero);

		assertEquals(this.bint_zero, actual_bval);
		assertEquals(this.bint_zero, actual_bint);
	}

	@Test
	void testWriteZero() throws IOException {
		String actual_bval = PARSER.writeBValueToString(this.bint_zero);
		String actual_bint = PARSER.writeBIntegerToString(this.bint_zero);

		assertEquals(this.str_zero, actual_bval);
		assertEquals(this.str_zero, actual_bint);
	}

	@Test
	void testReadNegativeValue() throws IOException {
		BInteger actual_bval = (BInteger) PARSER.readBValueFromString(this.str_negative);
		BInteger actual_bint = PARSER.readBIntegerFromString(this.str_negative);

		assertEquals(this.bint_negative, actual_bval);
		assertEquals(this.bint_negative, actual_bint);
	}

	@Test
	void testWriteNegativeValue() throws IOException {
		String actual_bval = PARSER.writeBValueToString(this.bint_negative);
		String actual_bint = PARSER.writeBIntegerToString(this.bint_negative);

		assertEquals(this.str_negative, actual_bval);
		assertEquals(this.str_negative, actual_bint);
	}

	@Test
	void testReadPositiveValue() throws IOException {
		BInteger actual_bval = (BInteger) PARSER.readBValueFromString(this.str_positive);
		BInteger actual_bint = PARSER.readBIntegerFromString(this.str_positive);

		assertEquals(this.bint_positive, actual_bval);
		assertEquals(this.bint_positive, actual_bint);
	}

	@Test
	void testWritePositiveValue() throws IOException {
		String actual_bval = PARSER.writeBValueToString(this.bint_positive);
		String actual_bint = PARSER.writeBIntegerToString(this.bint_positive);

		assertEquals(this.str_positive, actual_bval);
		assertEquals(this.str_positive, actual_bint);
	}

}
