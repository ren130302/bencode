package bencode.parser;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class TorrentBase64 {

	private static final char[] toBase64 = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E',
			'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
			'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u',
			'v', 'w', 'x', 'y', 'z', '.', '-' };

	private static final Map<Integer, Character> ENCODER;
	private static final Map<Character, Integer> DECODER;

	static {
		Map<Integer, Character> _en = new HashMap<>();
		Map<Character, Integer> _de = new HashMap<>();

		for (int i = 0; i < toBase64.length; i++) {
			_en.put(i, toBase64[i]);
			_de.put(toBase64[i], i);
		}

		ENCODER = Collections.unmodifiableMap(_en);
		DECODER = Collections.unmodifiableMap(_de);
	}

	public static String encode(byte[] data) {
		StringBuffer buf = new StringBuffer();
		for (byte _byte : data) {
			buf.append(ENCODER.get(Byte.toUnsignedInt(_byte)));
		}
		return buf.toString();
	}

	public static byte[] decode(String data) {
		return decode(data.toCharArray());
	}

	public static byte[] decode(char[] data) {
		StringBuffer buf = new StringBuffer();
		for (char _char : data) {
			buf.append(DECODER.get(_char));
		}
		return buf.toString().getBytes();
	}

	public static byte[] decode(byte[] data) {
		StringBuffer buf = new StringBuffer();
		for (byte _char : data) {
			buf.append(DECODER.get((char) Byte.toUnsignedInt(_char)));
		}
		return buf.toString().getBytes();
	}
}
