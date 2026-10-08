package com.biim3210.mediaplayer;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;


public class StatusLine extends VBox {

    public StatusLine(MediaController controller) {
        super(2);
        setAlignment(Pos.CENTER);

        Label statusLabel = new Label();
        statusLabel.textProperty().bind(controller.statusMessageProperty());
        statusLabel.getStyleClass().add("status-label");

        Label hintLabel = new Label(
                "Space Play/Pause  |  S Stop  |  N Next  |  P Previous  |  Up/Down Volume  |  M Mute");
        hintLabel.getStyleClass().add("hint-label");

        getChildren().addAll(statusLabel, hintLabel);
    }
}
