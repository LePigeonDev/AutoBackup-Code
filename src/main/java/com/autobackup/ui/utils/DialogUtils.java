package com.autobackup.ui.utils;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JFileChooser;


public class DialogUtils {
    
    public static Set<File> foldersBackupOpenDialog() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Sélectionnez un ou plusieurs dossiers");
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setMultiSelectionEnabled(true);

        int result = chooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            return new HashSet<>(Arrays.asList(chooser.getSelectedFiles()));
        }

        return Collections.emptySet();
    }

    public static File outputDirectoryOpenDialog() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Sélectionnez un ou plusieurs dossiers");
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setMultiSelectionEnabled(false);

        int result = chooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            return chooser.getSelectedFile();
        }

        return null;
    }
}
