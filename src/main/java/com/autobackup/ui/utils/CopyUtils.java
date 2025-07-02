package com.autobackup.ui.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Set;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;

public class CopyUtils {

    /**
     * Copie en parallèle les dossiers dans un sous-répertoire daté (jj.MM.aaaa),
     * tout en remontant le nom du fichier courant et un vrai pourcentage via le listener.
     */
    public static void copyDirectories(Set<File> sources,
                                       File destination,
                                       CopyProgressListener listener)
                                       throws InterruptedException 
    {
        // --- Création du dossier daté ---
        String dateFolderName = LocalDate.now()
                                        .format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        File datedDest = new File(destination, dateFolderName);
        if (!datedDest.exists()) {
            datedDest.mkdirs();
        }

        // 1. On compte d'abord le total de fichiers
        long totalFiles = sources.stream()
            .flatMap(src -> {
                try {
                    return Files.walk(src.toPath());
                } catch (IOException e) {
                    return Stream.<Path>empty();
                }
            })
            .filter(Files::isRegularFile)
            .count();

        // 2. Préparation de la copie parallèle
        // (on remplace 'destination' par 'datedDest' ici)
        File wampDest = new File(datedDest, "Wamp");
        boolean hasProj = sources.stream()
                                 .anyMatch(f -> "projects".equalsIgnoreCase(f.getName()));
        if (hasProj && !wampDest.exists()) {
            wampDest.mkdirs();
        }

        int threads = Runtime.getRuntime().availableProcessors();
        ExecutorService exec = Executors.newFixedThreadPool(threads);
        AtomicLong counter = new AtomicLong(0);

        for (File srcRoot : sources) {
            exec.submit(() -> {
                try {
                    Files.walkFileTree(srcRoot.toPath(), new SimpleFileVisitor<>() {
                        @Override
                        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                            // Calcul du path de destination (avec datedDest)
                            Path relative = srcRoot.toPath().relativize(file);
                            Path baseDest = 
                                "projects".equalsIgnoreCase(srcRoot.getName())
                                ? wampDest.toPath()
                                : datedDest.toPath();
                            Path target = baseDest.resolve(srcRoot.getName()).resolve(relative);

                            try {
                                Files.createDirectories(target.getParent());
                                Files.copy(file, target, StandardCopyOption.REPLACE_EXISTING);
                            } catch (IOException ioe) {
                                System.err.println("Erreur copie " + file + " : " + ioe);
                            }

                            // Notification au listener
                            long done = counter.incrementAndGet();
                            listener.onFileCopied(
                                file.getFileName().toString(),
                                done,
                                totalFiles
                            );
                            return FileVisitResult.CONTINUE;
                        }
                    });
                } catch (IOException ex) {
                    System.err.println("Erreur de parcours " + srcRoot + " : " + ex);
                }
            });
        }

        exec.shutdown();
        exec.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
    }
}
