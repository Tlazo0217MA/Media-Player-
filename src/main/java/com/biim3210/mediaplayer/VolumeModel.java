package com.biim3210.mediaplayer;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;


public class VolumeModel {

    private static final double STEP = 0.1;

    private final DoubleProperty volume = new SimpleDoubleProperty(0.5);
    private final BooleanProperty muted = new SimpleBooleanProperty(false);

    public void increase() {
        change(volume.get() + STEP);
    }

    public void decrease() {
        change(volume.get() - STEP);
    }

    private void change(double newVolume) {
        double clamped = Math.max(0.0, Math.min(1.0, newVolume)); 
        volume.set(Math.round(clamped * 100.0) / 100.0);          
        muted.set(false);                                          
    }

    public void toggleMute() {
        muted.set(!muted.get());
    }

    public int getPercent() {
        return (int) Math.round(volume.get() * 100);
    }

    public DoubleProperty volumeProperty() {
        return volume;
    }

    public BooleanProperty mutedProperty() {
        return muted;
    }
}
