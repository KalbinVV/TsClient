package org.kalbinvv.tsclient;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.stream.Collectors;

public class Loader {

	private final String resourcesPath;

	Loader(String resourcesPath){
		this.resourcesPath = resourcesPath;
	}

	URL getFileURL(String path) {
		URL url = null;
		File file = new File(resourcesPath + File.separator + path);
		try {
			url = file.toURI().toURL();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return url;
	}

	boolean isResourcesFolderExist() {
		File file = new File(resourcesPath);
		return file.exists();
	}

	void copyFilesFromResources(String[] resources) {
		for(String resource : resources) {
			InputStream inputStream = getClass().getResourceAsStream(resource);
			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
			String content = reader.lines().collect(Collectors.joining(System.lineSeparator()));
			File file = new File(resourcesPath + File.separator + resource);
			file.getParentFile().mkdirs();
			BufferedWriter bufferedWriter = null;
			try {
				bufferedWriter = new BufferedWriter(new FileWriter(file));
				bufferedWriter.write(content);
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					bufferedWriter.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
