package bencode;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.junit.jupiter.api.Test;

import com.dampcake.bencode.Bencode;
import com.dampcake.bencode.Type;

import bencode.json.BEncodeJson;
import bencode.values.BDictionary;

//@Log
public class ParseTest {
	private final BValueParsers valueParsers = new BValueParsers(StandardCharsets.UTF_8);

	@Test
	public void torrent() throws IOException {
		this.assertTorrent("bittorrent-v2-test.torrent");
		this.assertTorrent("bittorrent-v2-hybrid-test.torrent");
	}

	private void assertTorrent(String filename) throws IOException {
		final byte[] data;
		try (InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(filename)) {
			data = inputStream.readAllBytes();
		}

		Map<?, ?> m = new Bencode().decode(data, Type.DICTIONARY);
//		System.out.println(BEncodeJson.get().writeValueAsString(m));

		BDictionary desirialized = this.valueParsers.readBDictionaryFromBytes(data);
		byte[] serialized = this.valueParsers.writeBDictionaryToBytes(desirialized);
		BDictionary desirialized2 = this.valueParsers.readBDictionaryFromBytes(serialized);
		byte[] serialized2 = this.valueParsers.writeBDictionaryToBytes(desirialized2);

		assertArrayEquals(serialized, serialized2);
		assertEquals(desirialized.toString(), desirialized2.toString());
		assertEquals(BEncodeJson.get().writeValueAsString(desirialized), BEncodeJson.get().writeValueAsString(m));
	}
}
