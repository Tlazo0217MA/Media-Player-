package com.biim3210.mediaplayer;

import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;


public class ControlBar extends VBox {

    private static final double SIDE_WIDTH = 240; // wide enough for the volume control

    public ControlBar(MediaController controller) {
        super(14);
        getStyleClass().add("control-panel");
        setMaxWidth(1000);

        getChildren().addAll(new SeekBar(controller), createButtonRow(controller));
    }

    private HBox createButtonRow(MediaController controller) {
        
        HBox leftPart = new HBox(createMuteButton(controller));
        leftPart.setAlignment(Pos.CENTER_LEFT);
        setFixedWidth(leftPart);

        
        HBox rightPart = new HBox(new VolumeControl(controller));
        rightPart.setAlignment(Pos.CENTER_RIGHT);
        setFixedWidth(rightPart);

        
        Region spaceLeft = new Region();
        HBox.setHgrow(spaceLeft, Priority.ALWAYS);
        Region spaceRight = new Region();
        HBox.setHgrow(spaceRight, Priority.ALWAYS);

        HBox row = new HBox(leftPart, spaceLeft, createTransportButtons(controller),
                spaceRight, rightPart);
        row.setAlignment(Pos.CENTER);
        return row;
    }

    private void setFixedWidth(Region region) {
        region.setMinWidth(SIDE_WIDTH);
        region.setPrefWidth(SIDE_WIDTH);
        region.setMaxWidth(SIDE_WIDTH);
    }

    private AppButton createMuteButton(MediaController controller) {
        Icon icon = new Icon(Icon.VOLUME_HIGH, 24);
        AppButton button = new AppButton(icon, "transport-button");
        button.setOnAction(event -> controller.toggleMute());
       
        controller.mutedProperty().addListener((obs, oldValue, muted) ->
                icon.setPath(muted ? Icon.VOLUME_MUTED : Icon.VOLUME_HIGH));
        return button;
    }

    private HBox createTransportButtons(MediaController controller) {
        AppButton stopButton = new AppButton(new Icon(Icon.STOP, 22), "transport-button");
        stopButton.setOnAction(event -> controller.stopMedia());

        AppButton previousButton = new AppButton(new Icon(Icon.PREVIOUS, 30), "transport-button");
        previousButton.setOnAction(event -> controller.playPrevious());

        Icon playPauseIcon = new Icon(Icon.PLAY, 30);
        AppButton playPauseButton = new AppButton(playPauseIcon, "play-button");
        playPauseButton.setOnAction(event -> controller.togglePlayPause());
        // The picture follows the player: Pause symbol while playing, Play symbol otherwise.
        controller.playingProperty().addListener((obs, oldValue, playing) ->
                playPauseIcon.setPath(playing ? Icon.PAUSE : Icon.PLAY));

        AppButton nextButton = new AppButton(new Icon(Icon.NEXT, 30), "transport-button");
        nextButton.setOnAction(event -> controller.playNext());

        HBox transport = new HBox(18, stopButton, previousButton, playPauseButton, nextButton);
        transport.setAlignment(Pos.CENTER);
        return transport;
    }
}