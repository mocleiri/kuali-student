package org.kuali.student.sqlOrganizer;

import java.io.File;

/**
 * Created with IntelliJ IDEA.
 * User: lsymms
 * Date: 12/30/13
 * Time: 3:10 PM
 * To change this template use File | Settings | File Templates.
 */
public class DirManipulationUtils {
    public static void delEmptyDirs(File dir) {
        if (dir.isDirectory()) {
            for (File file : dir.listFiles()) {
                delEmptyDirs(file);
            }
            // might be empty after recursion
            if (dir.listFiles().length == 0) {
                dir.delete();
            }
        }

    }


    public static void deleteDirFiles(File dir) {
        File[] files = dir.listFiles();

        if (files != null) {
            for (File myFile : files) {
                if (myFile.isDirectory()) {
                    deleteDirFiles(myFile);
                } else {
                    myFile.delete();
                }

            }
        }
    }

    public static boolean mkDirCascaded(File path) {
        boolean ret = false;
        if (!path.exists()) {
            File parent = path.getParentFile();
            if (!parent.exists()) {
                mkDirCascaded(parent);
            }
            path.mkdir();
            System.out.println("created directory " + path + " successfully");
            ret = true;
        }
        return ret;
    }


    public static void cleanOutputDir(File path) {

        boolean isDirectoryCreated = mkDirCascaded(path);

        if (isDirectoryCreated) {
            System.out.println("created directory " + path + " successfully");

        } else {
            deleteDirFiles(path);  // Invoke recursive method
        }
    }

    public static void cascadeDelDir(File dir) {
        if (dir.isDirectory()) {
            for (File file : dir.listFiles()) {
                cascadeDelDir(file);
            }
            // might be empty after recursion
            if (dir.listFiles().length == 0) {
                dir.delete();
            }
        } else {
            dir.delete();
        }
        System.out.println("deleted directory and it's contents " + dir);
    }

}
