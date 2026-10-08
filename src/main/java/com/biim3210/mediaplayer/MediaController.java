package com.biim3210.mediaplayer;

import java.io.File;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.StringProperty;


public class MediaController {

    private final PlayerEngine engine;
    private final PlaylistManager playlist;
    private final VolumeModel volumeModel;

    public MediaController(PlayerEngine engine, PlaylistManager playlist, VolumeModel volumeModel) {
        this.engine = engine;
        this.playlist = playlist;
        this.volumeModel = volumeModel;
    }

    // ----- Playback -----

    public void togglePlayPause() {
        if (engine.hasMedia()) {
            engine.togglePlayPause();
        } else {
            playlist.startPlayback();
        }
    }

    public void stopMedia() {
        engine.stop();
    }

    // ----- Playlist -----

    public void playNext() {
        playlist.selectNext();
    }

    public void playPrevious() {
        playlist.selectPrevious();
    }

    public void addFile(File file) {
        playlist.addUserFile(file);
    }

    public void removeSelectedItem() {
        playlist.removeSelected();
    }

    // ----- Volume -----

    public void increaseVolume() {
        volumeModel.increase();
        engine.setStatus("Volume: " + volumeModel.getPercent() + "%");
    }

    public void decreaseVolume() {
        volumeModel.decrease();
        engine.setStatus("Volume: " + volumeModel.getPercent() + "%");
    }

    public void toggleMute() {
        volumeModel.toggleMute();
        engine.setStatus(volumeModel.mutedProperty().get() ? "Muted." : "Unmuted.");
    }

    public void seekTo(double seconds) {
        engine.seekTo(seconds);
    }

    // ----- Shutdown -----

    public void shutdown() {
        engine.release();
    }

    // ----- Values the UI displays -----

    public TimeModel timeModel() {
        return engine.timeModel();
    }

    public BooleanProperty playingProperty() {
        return engine.playingProperty();
    }

    public StringProperty statusMessageProperty() {
        return engine.statusMessageProperty();
    }

    public StringProperty nowPlayingProperty() {
        return engine.nowPlayingProperty();
    }

    public DoubleProperty volumeProperty() {
        return volumeModel.volumeProperty();
    }

    public BooleanProperty mutedProperty() {
        return volumeModel.mutedProperty();
    }
}
