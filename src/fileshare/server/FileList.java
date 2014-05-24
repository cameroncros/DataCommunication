package fileshare.server;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

/**
 * This class maintains the list of files for every peer
 * @author Cameron
 *
 */
public class FileList {
	Vector<String> allFiles = new Vector<String>();
	Map<String, Vector<String>> clientsFiles = new HashMap<String, Vector<String>>();
	/**
	 * Add client.
	 * @param host
	 */
	void AddClient(String host) {
		clientsFiles.remove(host);
		clientsFiles.put(host, new Vector<String>());
	}
	/**
	 * Delete the client and remove their files
	 * @param host
	 */
	void DeleteClient(String host) {
		clientsFiles.remove(host);
		deleteFiles(host);
	}
	/**
	 * Delete the files
	 * @param host
	 */
	private void deleteFiles(String host) {
		allFiles.clear();
		clientsFiles.remove(host);
		for (String server : clientsFiles.keySet()) {
			for (String file : clientsFiles.get(server)) {
				if (!allFiles.contains(file)) {
					allFiles.add(file);
				}
			}
		}
	}
	/**
	 * Save the filelist to the disk. One file per peer.
	 */
	void saveFileList() {
		try {


			for (String server : clientsFiles.keySet()) {
				FileWriter fw = new FileWriter(new File(server + ".files"));
				for (String file : clientsFiles.get(server)) {
					fw.write(file);
					fw.write("\n");
				}
				fw.close();
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * Add files from a host
	 * @param host - host that owns the files
	 * @param files - add the files
	 */
	void addFiles(String host, Vector<String> files) {
		deleteFiles(host);
		Vector<String> hosts = clientsFiles.get(host);
		if (hosts != null) {
			for (String filename : files) {
				hosts.add(filename);
				if (!allFiles.contains(filename)) {
					allFiles.add(filename);
				}
			}
			saveFileList();
		}

	}

	/**
	 * search through all the files for ones that match the values we want
	 * @param values - search terms that match. 
	 * @return vector containing files that match. Results only have to match one of the terms.
	 */
	Vector<String> search(Vector<String> values) {
		Vector<String> results = new Vector<String>();
		for (String file : allFiles) {
			boolean contains = true;
			for (String val : values) {
				if (!file.contains(val)) {
					contains = false;
					break;
				}
			}
			if (contains) {
				results.add(file);
			}
		}
		return results;
	}
/**
 * Finds all the peers that have the file
 * @param values - filename
 * @return vector containing the peers that have the file
 */
	Vector<String> findPeersForFile(Vector<String> values) {
		String file = values.firstElement();
		Vector<String> results = new Vector<String>();
		if (values.size() == 1) {
			for (String server : clientsFiles.keySet()) {
				Vector<String> filelist = clientsFiles.get(server);
				if (filelist.contains(file)) {
					results.add(server);
				}
			}
		}
		return results;
	}

}
