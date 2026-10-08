package com.biim3210.mediaplayer;

import java.util.Locale;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.Slider;

/** A Slider whose track is blue up to the thumb (the normal JavaFX slider has no filled part). */
public class FilledSlider extends Slider {

    public FilledSlider(double min, double max, double value) {
        super(min, max, value);
        valueProperty().addListener((obs, oldValue, newValue) -> updateFill());
        minProperty().addListener((obs, oldValue, newValue) -> updateFill());
        maxProperty().addListener((obs, oldValue, newValue) -> updateFill());
        widthProperty().addListener((obs, oldValue, newValue) -> updateFill());
        skinProperty().addListener((obs, oldSkin, newSkin) -> Platform.runLater(this::updateFill));
    }

    private void updateFill() {
        Node track = lookup(".track");
        if (track == null) {
            return; // skin not created yet
        }
        double range = getMax() - getMin();
        double percent = range <= 0 ? 0 : (getValue() - getMin()) / range * 100.0;
        track.setStyle(String.format(Locale.ROOT,
                "-fx-background-color: linear-gradient(to right, #3b82f6 %1$.1f%%, #2a3347 %1$.1f%%);",
                percent));
    }
}
