package com.biim3210.mediaplayer;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.MediaView;

/**
 * The large rounded area in the middle. It contains the MediaView (video picture).
 * Behind it: a music icon and the file name, which stay visible for audio files.
 */
public class VideoArea extends StackPane {

    public VideoArea(MediaView mediaView, MediaController controller) {
        Icon bigIcon = new Icon(Icon.NOTE, 96);
        bigIcon.setOpacity(0.3);

        Label nowPlayingLabel = new Label();
        nowPlayingLabel.textProperty().bind(controller.nowPlayingProperty());
        nowPlayingLabel.getStyleClass().add("now-playing-label");

        VBox placeholder = new VBox(18, bigIcon, nowPlayingLabel);
        placeholder.setAlignment(Pos.CENTER);

        getStyleClass().add("video-pane");
        setMinSize(0, 0); // lets the area shrink with the window
        getChildren().addAll(placeholder, mediaView);

        // The video fills the area; the aspect ratio is kept.
        mediaView.fitWidthProperty().bind(widthProperty());
        mediaView.fitHeightProperty().bind(heightProperty());
    }
}
