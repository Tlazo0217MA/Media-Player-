package com.biim3210.mediaplayer;

import javafx.scene.control.Alert;
import javafx.stage.Stage;

/** Shows an error message box. */
public final class ErrorDialog {

    private ErrorDialog() { }

    public static void show(Stage owner, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.initOwner(owner);
        alert.setTitle("Media Player");
        alert.setHeaderText("Something went wrong");
        alert.setContentText(message);
        alert.show();
    }
}
