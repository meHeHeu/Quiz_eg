package com.example.quiz_eg;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MyUtils {

    // Disallows MyUtils object initialization
    private MyUtils () {

    }

    // Fisher–Yates shuffle
    public static <T> void ShuffleArray(T[] array) {
        Random random = new Random();

        for(int i=array.length-1; i>0; --i) {

            int j = random.nextInt(i+1);

            T temp = array[j];
            array[j] = array[i];
            array[i] = temp;
        }
    }

    // Get list of files in subdirectories root and of extension in extensions array
    public static List<File> FileListByExtension(File root, String[] extensions) {

        List<File> fileList = new ArrayList<File>();
        if(root.exists())
            for(File file : root.listFiles()) {
                if (file.isDirectory() && !file.isHidden())
                    fileList.addAll(FileListByExtension(file, extensions));
                else
                    for (String ext : extensions)
                        if (file.getName().endsWith(ext)) {
                            fileList.add(file);
                            break;
                        }
            }

        return fileList;
    }

}
