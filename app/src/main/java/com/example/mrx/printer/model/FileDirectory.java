package com.example.mrx.printer.model;

import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.LogUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Administrator on 2017/9/18.
 */

public class FileDirectory {

    private static final int FILE_DIR = 1;
    private static final int FILE_PRINT = 2;
    private static final int FILE_OTHER = 3;

    public List<File> fileList;

    public boolean isRoot;

    public boolean isDiskDir;

    public void setRoot(boolean root) {
        isRoot = root;
    }

    public int fileNum() {
        if (fileList == null) {
            return 0;
        }
        return fileList.size();
    }

    public FileDirectory( List<String> diskList) {
        isRoot = true;
        isDiskDir = true;
        if (diskList.size() > 1) {
            fileList = new ArrayList<>();
            for (String diskPath : diskList) {
                fileList.add(new File(diskPath));
            }
        } else if (diskList.size() == 1) {
            isDiskDir = false;
            File file = FileUtils.getFileByPath(diskList.get(0));
            init(file);
        }
    }

    public FileDirectory(String dirPath) {
        File file = FileUtils.getFileByPath(dirPath);
        init(file);
    }

    public FileDirectory(File file) {
        init(file);
    }

    private void init(File file) {
        if (FileUtils.isDir(file)) {
            fileList = FileUtils.listFilesInDir(file);
            Collections.sort(fileList, new Comparator<File>() {
                @Override
                public int compare(File file, File t1) {
                    int firstType = getFileType(file);
                    int secondType = getFileType(t1);
                    if (firstType > secondType) {
                        return 1;
                    } else if (firstType < secondType) {
                        return -1;
                    } else {
                        String firstName = file.getName();
                        String secondName = t1.getName();
                        return firstName.compareTo(secondName);
                    }
                }
            });
        }
    }

    private int getFileType(File file) {
        if (FileUtils.isDir(file)) {
            return FILE_DIR;
        } else if (isPrintFile(file)) {
            return FILE_PRINT;
        } else {
            return FILE_OTHER;
        }
    }

    public static boolean isPrintFile(File file) {
        if ("gcode".contentEquals(FileUtils.getFileExtension(file))) {
            return true;
        } else {
            return false;
        }
    }

}
