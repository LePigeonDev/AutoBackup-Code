module autobackup {
    // JavaFX modules
    requires javafx.controls;
    requires javafx.fxml;
    requires transitive javafx.graphics; // inclut javafx.base et expose javafx.graphics

    // Commons IO (automatic module name)
    requires org.apache.commons.io;

    // Swing (pour JFileChooser)
    requires java.desktop;

    // Ouvrir vos packages de controllers à FXMLLoader
    opens com.autobackup to javafx.fxml;
    opens com.autobackup.ui.controller to javafx.fxml;

    // Exporter votre package principal
    exports com.autobackup;
}
