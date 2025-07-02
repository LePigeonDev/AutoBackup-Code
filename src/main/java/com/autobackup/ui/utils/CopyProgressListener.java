package com.autobackup.ui.utils;


@FunctionalInterface
public interface CopyProgressListener {
    /**
     * @param fileName   le nom du fichier qui vient d'être copié
     * @param copied     le nombre de fichiers copiés jusqu'ici
     * @param total      le nombre total de fichiers qu'il y a à copier
     */
    void onFileCopied(String fileName, long copied, long total);
}
