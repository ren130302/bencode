package bencode;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

class BEncodeFileReadTest {

  private byte[] readFile(String resourceName) throws IOException {
    Path path = Path.of("src/test/resources", resourceName);
    return Files.readAllBytes(path);
  }

  private BValue<?> parseBEncode(byte[] data) {
    BEncodeInputStream in = new BEncodeInputStream(data);
    return in.readBValue();
  }

  private byte[] encodeBValue(BValue<?> value) {
    BEncodeOutputStream out = new BEncodeOutputStream();
    out.writeBValue(value);
    return out.toByteArray();
  }

  @Test
  void testHybridTorrentRoundTrip() throws IOException {
    byte[] original = this.readFile("bittorrent-v2-hybrid-test.torrent");
    BValue<?> parsed = this.parseBEncode(original);
    assertNotNull(parsed);

    byte[] reencoded = this.encodeBValue(parsed);
    assertArrayEquals(original, reencoded);
  }

  @Test
  void testV2TorrentRoundTrip() throws IOException {
    byte[] original = this.readFile("bittorrent-v2-test.torrent");
    BValue<?> parsed = this.parseBEncode(original);
    assertNotNull(parsed);

    byte[] reencoded = this.encodeBValue(parsed);
    assertArrayEquals(original, reencoded);
  }
}
