package fileshare.client;

import java.io.File;
import java.util.Vector;

public class MusicScanner {
	String path;
	private static Vector<String> files = new Vector<String>();

	MusicScanner(String directory) {
		path = directory;
		scanDirectory();
	}

	void scanDirectory() {
		File dir = new File(path);
		for (File d : dir.listFiles()) {
			if (!d.isDirectory()) {
				String fname = d.getName();
				if (fname.endsWith(".mp3")) {
					files.add(fname);
				}
			}
		}
	}

	Vector<String> getFileList() {
		return files;
	}
}