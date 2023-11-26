package torrent.common;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import bencode.io.BValueParsers;
import bencode.values.BDictionary;

public final class TorrentFile {

	static TorrentFile load(Path path) {
		if (Files.notExists(path)) {
			throw new IllegalArgumentException("Not exists -> " + path.toAbsolutePath());
		}

		try (InputStream inputStream = ClassLoader.getSystemResourceAsStream(path.toString())) {
			final byte[] data = inputStream.readAllBytes();
			final BDictionary bDictionary = new BValueParsers().readBDictionaryFromBytes(data);
			final TorrentFile torrentFile = new TorrentFile();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

}
