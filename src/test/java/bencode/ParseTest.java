package bencode;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;

import bencode.json.BEncodeJson;
import bencode.parser.BValueParsers;

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
		BDictionary desirialized = this.valueParsers.readBDictinaryFromBytes(data);
		byte[] serialized = this.valueParsers.writeBDictinaryToBytes(desirialized);

		System.out.println(BEncodeJson.get().writeValueAsString(desirialized));

//		BDictionary desirialized2 = this.valueParsers.getBDictionaryParser().readFromByteBuffer(serialized);
//		System.out.println(data);
//		System.out.println(serialized);
//		BDictionary desirialized2 = this.valueParsers.getBDictionaryParser().readFromByteBuffer(serialized);
//		ByteBuffer serialized2 = this.valueParsers.getBDictionaryParser().writeToByteBuffer(desirialized2);
//		System.out.println(serialized);
//		System.out.println(serialized2);

//		assertEquals(desirialized.toString(), desirialized2.toString());
	}
}
