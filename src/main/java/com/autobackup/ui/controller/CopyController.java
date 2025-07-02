package com.autobackup.ui.controller;

import com.autobackup.ui.utils.CopyUtils;
import javafx.concurrent.Task;

import java.io.File;
import java.util.Set;

public class CopyController {

    public Task<Boolean> createCopyTask(Set<File> sources, File destination) {
        return new Task<>() {
            @Override
            protected Boolean call() throws Exception {
                if (sources.isEmpty() || destination == null) {
                    return false;
                }

                try {
                    CopyUtils.copyDirectories(
                        sources,
                        destination,
                        (fileName, done, total) -> {
                            updateMessage(fileName);
                            updateProgress(done, total);
                        }
                    );
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return false;
                }

                return true;
            }
        };
    }

}
