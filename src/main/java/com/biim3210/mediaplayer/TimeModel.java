package com.biim3210.mediaplayer;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;


public class TimeModel {

    private final DoubleProperty currentSeconds = new SimpleDoubleProperty(0);
    private final DoubleProperty totalSeconds = new SimpleDoubleProperty(0);

    /** 87.4 seconds -> "1:27" */
    public static String format(double seconds) {
        int whole = (int) Math.max(0, Math.floor(seconds));
        return (whole / 60) + ":" + String.format("%02d", whole % 60);
    }

    public void reset() {
        currentSeconds.set(0);
        totalSeconds.set(0);
    }

    public double getCurrentSeconds() {
        return currentSeconds.get();
    }

    public DoubleProperty currentSecondsProperty() {
        return currentSeconds;
    }

    public DoubleProperty totalSecondsProperty() {
        return totalSeconds;
    }
}
