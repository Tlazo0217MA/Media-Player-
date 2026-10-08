package com.biim3210.mediaplayer;

import java.util.function.Consumer;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;


public class PlayerEngine {

    private final MediaView mediaView;
    private final VolumeModel volumeModel;
    private final TimeModel timeModel = new TimeModel();

    private MediaPlayer mediaPlayer;               
    private boolean errorAlreadyReported = false;  

    private final BooleanProperty playing = new SimpleBooleanProperty(false);
    private final StringProperty statusMessage = new SimpleStringProperty("Ready");
    private final StringProperty nowPlaying = new SimpleStringProperty("No media loaded");

    private Runnable endOfMediaAction = () -> { };
    private Consumer<String> errorReporter = message -> { };

    public PlayerEngine(MediaView mediaView, VolumeModel volumeModel) {
        this.mediaView = mediaView;
        this.volumeModel = volumeModel;

       
        volumeModel.volumeProperty().addListener((obs, oldValue, newValue) -> {
            if (mediaPlayer != null) {
                mediaPlayer.setVolume(newValue.doubleValue());
            }
        });
        volumeModel.mutedProperty().addListener((obs, oldValue, newValue) -> {
            if (mediaPlayer != null) {
                mediaPlayer.setMute(newValue);
            }
        });
    }

   
    public void load(MediaItem item, boolean autoPlay) {
        release();
        errorAlreadyReported = false;

        try {
            Media media = new Media(item.getSource());
            MediaPlayer player = new MediaPlayer(media);
            mediaPlayer = player;

            player.setVolume(volumeModel.volumeProperty().get());
            player.setMute(volumeModel.mutedProperty().get());

            player.statusProperty().addListener((obs, oldStatus, newStatus) ->
                    playing.set(newStatus == MediaPlayer.Status.PLAYING));
            player.currentTimeProperty().addListener((obs, oldTime, newTime) ->
                    timeModel.currentSecondsProperty().set(newTime.toSeconds()));
            player.setOnReady(() -> handleReady(player, item));
            player.setOnEndOfMedia(() -> endOfMediaAction.run());
            player.setOnError(() -> handleError(player, item, player.getError()));
            media.setOnError(() -> handleError(player, item, media.getError()));

            mediaView.setMediaPlayer(player);
            nowPlaying.set(item.getDisplayName());

            if (autoPlay) {
                player.play();
                setStatus("Playing: " + item.getDisplayName());
            } else {
                setStatus("Loaded: " + item.getDisplayName());
            }
        } catch (RuntimeException e) {
            handleError(null, item, e);
        }
    }

    
    public void release() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.dispose();
            mediaPlayer = null;
        }
        mediaView.setMediaPlayer(null);
        playing.set(false);
        timeModel.reset();
    }

   
    public void unload() {
        release();
        nowPlaying.set("No media loaded");
    }

    public boolean hasMedia() {
        return mediaPlayer != null;
    }

    

    public void togglePlayPause() {
        if (mediaPlayer != null && mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
            pause();
        } else {
            play();
        }
    }

    public void play() {
        if (mediaPlayer == null) {
            setStatus("Nothing to play.");
            return;
        }
        mediaPlayer.play();
        setStatus("Playing: " + nowPlaying.get());
    }

    public void pause() {
        if (mediaPlayer == null) {
            setStatus("Nothing to pause.");
            return;
        }
        mediaPlayer.pause(); // keeps the position
        setStatus("Paused: " + nowPlaying.get());
    }

    public void stop() {
        if (mediaPlayer == null) {
            setStatus("Nothing to stop.");
            return;
        }
        mediaPlayer.stop(); // back to the beginning (unlike pause)
        setStatus("Stopped.");
    }

    /** Jump to a position (used by the seek bar). */
    public void seekTo(double seconds) {
        if (mediaPlayer != null) {
            mediaPlayer.seek(Duration.seconds(seconds));
        }
    }

    /** The media is ready, so its length is known now. */
    private void handleReady(MediaPlayer player, MediaItem item) {
        Duration total = player.getTotalDuration();
        if (player != mediaPlayer || total.isUnknown() || total.isIndefinite()) {
            return;
        }
        timeModel.totalSecondsProperty().set(total.toSeconds());
        item.setDuration(TimeModel.format(total.toSeconds()));
    }

    // ---------- Errors and status ----------

    private void handleError(MediaPlayer source, MediaItem item, Exception error) {
        if (source != null && source != mediaPlayer) {
            return; // error from a player we already replaced
        }
        if (errorAlreadyReported) {
            return;
        }
        errorAlreadyReported = true;

        String reason = (error != null && error.getMessage() != null)
                ? error.getMessage() : "unknown error";
        playing.set(false);
        reportError("Cannot play \"" + item.getDisplayName() + "\".\n"
                + "The file may be corrupted or use an unsupported format.\n(" + reason + ")");
    }

    /** Shows the first line in the status bar and the full text in an error dialog. */
    public void reportError(String message) {
        setStatus(message.split("\n")[0]);
        errorReporter.accept(message);
    }

    public void setStatus(String message) {
        statusMessage.set(message);
    }

    public void setEndOfMediaAction(Runnable action) {
        this.endOfMediaAction = action;
    }

    public void setErrorReporter(Consumer<String> errorReporter) {
        this.errorReporter = errorReporter;
    }

    public TimeModel timeModel() {
        return timeModel;
    }

    public BooleanProperty playingProperty() {
        return playing;
    }

    public StringProperty statusMessageProperty() {
        return statusMessage;
    }

    public StringProperty nowPlayingProperty() {
        return nowPlaying;
    }
}
