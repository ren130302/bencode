package bencode;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import bencode.io.BEncodeParser;
import bencode.value.BDictionary;
import bencode.value.BList;
import bencode.value.BString;

//@Log
public class ParseTest {
	private final BEncodeParser valueParsers = new BEncodeParser(StandardCharsets.UTF_8);

	static byte[] v2;
	static byte[] v2_hybrid;

	@BeforeAll
	static void set() throws IOException {
		v2 = getFileData("bittorrent-v2-test.torrent");
		v2_hybrid = getFileData("bittorrent-v2-hybrid-test.torrent");
	}

	private static byte[] getFileData(String filename) throws IOException {
		try (InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(filename)) {
			return inputStream.readAllBytes();
		}
	}

	@Test
	public void torrent() throws IOException {
		BDictionary file_v2 = this.assertTorrent(v2);

		BDictionary file_v2_hybrid = this.assertTorrent(v2_hybrid);

		final String createdBy = file_v2_hybrid.getBString("created by").getString();
		final long creationDate = file_v2_hybrid.getBInteger("creation date").longValue();

		final BDictionary info = file_v2_hybrid.getBDictionary("info");
		final BDictionary fileTree = info.getBDictionary("file tree");
		for (BString key : fileTree.keySet()) {
			final String filename = key.getString();
			final BDictionary value = fileTree.getBDictionary(filename).getBDictionary("");
			final long length = value.getBInteger("length").longValue();
			final String piecesRoot = value.getBString("pieces root").getString();
			for (byte c : piecesRoot.getBytes()) {
				System.out.println(Integer.toHexString(Byte.toUnsignedInt(c)));
			}
		}
		final BList<?> files = info.getBList("files");
		final long metVersion = info.getBInteger("meta version").longValue();
		final String name = info.getBString("name").getString();
		final long pieceLength = info.getBInteger("piece length").longValue();
		final String pieces = info.getBString("pieces").getString();

		final BDictionary pieceLayers = file_v2_hybrid.getBDictionary("piece layers");
	}

	private BDictionary assertTorrent(byte[] data) throws IOException {
		BDictionary desirialized = this.valueParsers.readBDictionaryFromBytes(data);
		byte[] serialized = this.valueParsers.writeBDictionaryToBytes(desirialized);
		BDictionary desirialized2 = this.valueParsers.readBDictionaryFromBytes(serialized);
		byte[] serialized2 = this.valueParsers.writeBDictionaryToBytes(desirialized2);

		assertArrayEquals(serialized, serialized2);
		assertEquals(desirialized.toString(), desirialized2.toString());

		return desirialized;
	}
}
