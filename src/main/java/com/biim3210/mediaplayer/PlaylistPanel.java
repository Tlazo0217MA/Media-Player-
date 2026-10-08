package com.biim3210.mediaplayer;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;


public class PlaylistPanel extends VBox {

    private static final double WIDTH = 300;

    public PlaylistPanel(ListView<MediaItem> listView, MediaController controller) {
        getStyleClass().add("playlist-panel");
        setMinWidth(WIDTH);
        setPrefWidth(WIDTH);
        setMaxWidth(WIDTH);

        listView.setCellFactory(view -> new PlaylistCell());
        listView.setPlaceholder(new Label("Playlist is empty.\nClick Add to choose a file."));
        VBox.setVgrow(listView, Priority.ALWAYS);

        getChildren().addAll(createHeader(), listView, createFooter(controller));
    }

    private HBox createHeader() {
        Label heading = new Label("Playlist");
        heading.getStyleClass().add("section-label");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        AppButton collapseButton = new AppButton(new Icon(Icon.CHEVRON_UP, 22), "icon-button");
        collapseButton.getStyleClass().add("flat-button");
        collapseButton.setOnAction(event -> toggle());

        HBox header = new HBox(heading, spacer, collapseButton);
        header.getStyleClass().add("playlist-header");
        header.setAlignment(Pos.CENTER_LEFT);
        return header;
    }

    private HBox createFooter(MediaController controller) {
        AppButton removeButton = new AppButton("Remove", new Icon(Icon.TRASH, 20));
        removeButton.getStyleClass().add("flat-button");
        removeButton.setOnAction(event -> controller.removeSelectedItem());

        HBox footer = new HBox(removeButton);
        footer.getStyleClass().add("playlist-footer");
        return footer;
    }

    /** Sidebar behaviour: hidden panels also give their space back to the video area. */
    public void toggle() {
        boolean show = !isVisible();
        setVisible(show);
        setManaged(show);
    }
}
