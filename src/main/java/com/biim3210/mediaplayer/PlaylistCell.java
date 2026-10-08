package com.biim3210.mediaplayer;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;


public class PlaylistCell extends ListCell<MediaItem> {

    private final Icon icon = new Icon(Icon.NOTE, 20);
    private final Label nameLabel = new Label();
    private final Label durationLabel = new Label();
    private final HBox content;

    public PlaylistCell() {
        StackPane iconBox = new StackPane(icon);
        iconBox.getStyleClass().add("cell-icon-box");

        nameLabel.getStyleClass().add("cell-title");
        durationLabel.getStyleClass().add("cell-subtitle");

        content = new HBox(12, iconBox, new VBox(2, nameLabel, durationLabel));
        content.setAlignment(Pos.CENTER_LEFT);

        
        selectedProperty().addListener((obs, wasSelected, isSelected) -> updateIcon());
    }

    @Override
    protected void updateItem(MediaItem item, boolean empty) {
        super.updateItem(item, empty);
        durationLabel.textProperty().unbind();

        if (empty || item == null) {
            setText(null);
            setGraphic(null);
            return;
        }
        nameLabel.setText(item.toString());
        durationLabel.textProperty().bind(item.durationProperty());
        updateIcon();
        setText(null);
        setGraphic(content);
    }

    private void updateIcon() {
        icon.setPath(isSelected() ? Icon.PLAY : Icon.NOTE);
    }
}
