package fileshare.client;

import java.io.File;
import java.util.Vector;
/**
 * Class to scan directory for files.
 * @author Cameron
 *
 */
public class MusicScanner {
	String path;
	private static Vector<String> files = new Vector<String>();

	MusicScanner(String directory) {
		path = directory;
		scanDirectory();
	}

	/**
	 * scan the directory
	 */
	void scanDirectory() {
		File dir = new File(path);
		files.clear();
		for (File d : dir.listFiles()) {
			if (!d.isDirectory()) {
				String fname = d.getName();
				if (fname.endsWith(".mp3")) {
					if (!files.contains(fname))
					files.add(fname);
				}
			}
		}
	}
	/**
	 * give a vector containing the files
	 * @return files vector
	 */
	Vector<String> getFileList() {
		return files;
	}
	
	void deleteFile(String fname) {
		(new File(path+File.separatorChar +fname)).delete();
	}
}
