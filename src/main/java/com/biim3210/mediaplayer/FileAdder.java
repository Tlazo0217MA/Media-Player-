package com.biim3210.mediaplayer;

import java.io.File;

import javafx.stage.FileChooser;
import javafx.stage.Stage;

/** Opens the FileChooser and gives the chosen file to the controller. */
public final class FileAdder {

    private FileAdder() { }

    public static void chooseAndAdd(Stage stage, MediaController controller) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select a media file");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Media files", MediaFileTypes.fileChooserPatterns()),
                new FileChooser.ExtensionFilter("All files", "*.*"));

        File file = fileChooser.showOpenDialog(stage);
        controller.addFile(file); // null (cancel) is handled inside addUserFile
    }
}
