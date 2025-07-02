package com.autobackup.ui.controller;

import java.io.File;
import java.util.Set;

import com.autobackup.ui.utils.DialogUtils;

public class PathController {

    public Set<File> foldersBackup() {
        Set<File> folderList = DialogUtils.foldersBackupOpenDialog();

        if (folderList.isEmpty()) {
            System.out.println("Aucun dossier selectionne.");
        } else {
            for (File file : folderList) {
                System.out.println(file);
            }
        }
        return folderList;
    }

    public File outputDirectory() {
        File directory = DialogUtils.outputDirectoryOpenDialog();

        if (directory == null) {
            System.out.println("Aucun repertoire selectionne.");
        } else {
            System.out.println(directory);
        }
        return directory;
    }
}
