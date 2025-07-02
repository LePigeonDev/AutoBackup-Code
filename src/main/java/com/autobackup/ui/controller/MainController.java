package com.autobackup.ui.controller;

import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.io.File;
import java.util.HashSet;
import java.util.Set;
import javafx.animation.Timeline;
import javafx.util.Duration;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.animation.KeyFrame;
import javafx.beans.binding.Bindings;

public class MainController {

    @FXML
    private Button btnInputFolders;
    @FXML
    private Button btnOutputFolders;
    @FXML
    private Button btnCopy;
    @FXML
    private Button btnReset;
    @FXML
    private ListView<File> listView;
    @FXML
    private Label outDirectoryLabel;
    @FXML
    private Label succesLabel;
    @FXML
    private Label errorLabel;
    @FXML
    private Label stopwatch;
    @FXML
    private Label currentFileLabel;
    @FXML
    private Label percentLabel;
    @FXML
    private ContextMenu itemContextMenu;

    protected Set<File> listFolders = new HashSet<>();
    private final PathController backupCtrl = new PathController();
    private final ListController listController = new ListController();
    private final CopyController copyController = new CopyController();
    private File outputDirectory;
    private LongProperty secondes = new SimpleLongProperty(0);
    private Timeline timeline;

    @FXML
    private void initialize() {
        listView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    @FXML
    private void handleInputFolders() {
        Set<File> folders = backupCtrl.foldersBackup();
        Set<File> updated = listController.listAddFolders(folders);
        listFolders.addAll(updated);
        listView.getItems().setAll(updated);
    }

    @FXML
    private void handleOutputFolders() {
        File folders = backupCtrl.outputDirectory();
        if (folders != null) {
            btnOutputFolders.setDisable(true);
            listController.setOutputDirectory(folders);
            outputDirectory = listController.getOutputDirectory();
            outDirectoryLabel.setText(outputDirectory.getAbsolutePath());
        } else {
            System.err.println("Aucun dossier de destination séléctionné !");
            errorLabel.setText("Aucun dossier de destination séléctionné !");
        }
    }

    @FXML
    private void handleCopyFolders() {
        if (outputDirectory == null) {
            errorLabel.setText("Aucun dossier de destination sélectionné !");
            return;
        }
        Set<File> sources = listController.getListFolders();
        if (sources.isEmpty()) {
            errorLabel.setText("Aucun dossier source !");
            return;
        }

        errorLabel.setText(null);
        succesLabel.setText(null);
        btnCopy.setDisable(true);

        currentFileLabel.setText("Démarrage...");
        percentLabel.setText("0 %");

        Task<Boolean> task = copyController.createCopyTask(sources, outputDirectory);
        startStopwatch();
        percentLabel.textProperty().bind(
                Bindings.format("%.0f %%", task.progressProperty().multiply(100)));

        currentFileLabel.textProperty().bind(task.messageProperty());

        task.setOnSucceeded(evt -> {
            cleanupAfterCopy();
            if (task.getValue()) {
                succesLabel.setText("Copie terminée !");
            } else {
                errorLabel.setText("Copie interrompue.");
            }
        });

        task.setOnFailed(evt -> {
            cleanupAfterCopy();
            errorLabel.setText("Erreur : " + task.getException().getMessage());
        });

        task.setOnCancelled(evt -> {
            cleanupAfterCopy();
            errorLabel.setText("Copie annulée.");
        });

        Thread copyThread = new Thread(task, "Copy-Thread");
        copyThread.setDaemon(true);
        copyThread.start();
    }

    @FXML
    private void onDeleteItem() {
        ObservableList<File> foldersToDelete = listView.getSelectionModel().getSelectedItems();
        System.out.println("onDeleteItem : " + foldersToDelete);
        if (!foldersToDelete.isEmpty()) {
            boolean ifDelete = listController.listDeleteFolders(foldersToDelete);
            if (ifDelete) {
                listFolders.clear();
                listFolders.addAll(listController.getListFolders());
                listView.getItems().removeAll(foldersToDelete);
            }
        }
    }

    @FXML
    private void handleReset() {
        listFolders.clear();
        listView.getItems().clear();
        outDirectoryLabel.setText(null);
        outputDirectory = null;
        succesLabel.setText(null);
        errorLabel.setText(null);
        listController.setOutputDirectory(null);
        listController.clearFolders();
        btnCopy.setDisable(false);
        btnInputFolders.setDisable(false);
        btnOutputFolders.setDisable(false);
        secondes.set(0);
    }

    private void startStopwatch() {
        StringBinding timeBinding = Bindings.createStringBinding(() -> {
            long s = secondes.get();
            long h = s / 3600;
            long m = (s % 3600) / 60;
            long sec = s % 60;
            return String.format("%02d:%02d:%02d", h, m, sec);
        }, secondes);

        stopwatch.textProperty().bind(timeBinding);

        secondes.set(0);

        timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            secondes.set(secondes.get() + 1);
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void clear() {
        listFolders.clear();
        listView.getItems().clear();
        outDirectoryLabel.setText(null);
        outputDirectory = null;
        listController.setOutputDirectory(null);
        listController.clearFolders();
        currentFileLabel.setText(null);
        percentLabel.setText(null);
    }

    private void cleanupAfterCopy() {
        timeline.stop();
        btnCopy.setDisable(false);
        btnInputFolders.setDisable(false);
        btnOutputFolders.setDisable(false);
        clear();
    }

}
