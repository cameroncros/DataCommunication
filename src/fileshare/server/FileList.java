package fileshare.server;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class FileList {
	Vector<String> allFiles = new Vector<String>();
	Map<String, Vector<String>> clientsFiles = new HashMap<String, Vector<String>>();
	
	void AddClient(String host) {
		clientsFiles.remove(host);
		clientsFiles.put(host, new Vector<String>());
	}
	void DeleteClient(String host) {
		clientsFiles.remove(host);
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
	
	void addFiles(String host, Vector<String> files) {
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
