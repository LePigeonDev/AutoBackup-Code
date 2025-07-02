package com.autobackup.ui.controller;

import java.io.File;
import java.io.IOException;

import javafx.collections.ObservableList;

import java.util.HashSet;
import java.util.Set;

public class ListController {

    private File outputDirectory;
    private Set<File> listFolders = new HashSet<>();

    public Set<File> listAddFolders(Set<File> folders) {
        for (File element : folders) {
            try {
                File canon = element.getCanonicalFile();
                if (listFolders.add(canon)) {
                    System.out.println("L'élément " + element + " a bien été ajouté !");
                } else {
                    System.out.println("L'élément " + element + " est déjà présent !");
                }
            } catch (IOException e) {
                System.err.println("Impossible de canoniser " + element);
            }
        }
        return listFolders;
    }

    public boolean listDeleteFolders(ObservableList<File> foldersToDelete) {
        System.out.println("listDeleteFolders : " + foldersToDelete);
        boolean removed = listFolders.removeAll(foldersToDelete);
        System.out.println("listFolders : " + listFolders);
        return removed;
    }

    public Set<File> getListFolders() {
        return listFolders;
    }

    public File getOutputDirectory() {
        return outputDirectory;
    }

    public void setOutputDirectory(File directory) {
        this.outputDirectory = directory;
    }

    public void clearFolders() {
        listFolders.clear();
    }
}
