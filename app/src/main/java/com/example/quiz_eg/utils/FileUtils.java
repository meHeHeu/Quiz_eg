package com.example.quiz_eg.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public final class FileUtils {
	/**
	 * @param root - starting path
	 * @param extensions - include files with given extensions
	 * @return - list of files in subdirectories of root
	 */
	public static List<File> get(File root, String[] extensions) {
		List<File> fileList = new ArrayList<File>();
		if(root.exists())
			for(File file : root.listFiles()) {
				if (file.isDirectory() && !file.isHidden())
					fileList.addAll(get(file, extensions));
				else
					for (String ext : extensions)
						if (file.getName().endsWith(ext)) {
							fileList.add(file);
							break;
						}
			}
		return fileList;
	}

	private FileUtils() {
	}
}
