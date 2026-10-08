package com.biim3210.mediaplayer;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;


public class MediaItem {

    private final String displayName;
    private final String source;      
    private final boolean bundled;    
    private final StringProperty duration = new SimpleStringProperty("Added "); 

    public MediaItem(String displayName, String source, boolean bundled) {
        this.displayName = displayName;
        this.source = source;
        this.bundled = bundled;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getSource() {
        return source;
    }

    public boolean isBundled() {
        return bundled;
    }

    public StringProperty durationProperty() {
        return duration;
    }

    public void setDuration(String text) {
        duration.set(text);
    }

    
    @Override
    public String toString() {
        return bundled ? displayName + "  (bundled)" : displayName;
    }
}
