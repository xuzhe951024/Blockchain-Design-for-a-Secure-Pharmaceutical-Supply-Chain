package com.rbpsc.common.utiles;

import java.io.File;

public class FileUtils {
    /**
     * Deletes all files with a specific suffix in the specified directory.
     *
     * @param directoryPath Directory path
     * @param fileExtension The suffix of the file to be deleted
     */
    public static void deleteFilesWithExtension(String directoryPath, String fileExtension) {
        File directory = new File(directoryPath);

        if (directory.exists() && directory.isDirectory()) {
            File[] files = directory.listFiles((dir, name) -> name.toLowerCase().endsWith(fileExtension));

            if (files != null) {
                for (File file : files) {
                    if (!file.delete()) {
                        System.out.println("Failed to delete file: " + file.getName());
                    } else {
                        System.out.println("Deleted file: " + file.getName());
                    }
                }
            }
        } else {
            System.out.println("Directory does not exist or is not a directory.");
        }
    }

}
