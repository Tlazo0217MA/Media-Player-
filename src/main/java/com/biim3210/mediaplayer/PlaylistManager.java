package com.biim3210.mediaplayer;

import java.io.File;
import java.net.URL;

import javafx.collections.ObservableList;
import javafx.scene.control.MultipleSelectionModel;

public class PlaylistManager {

    private final ObservableList<MediaItem> items;
    private final MultipleSelectionModel<MediaItem> selection;
    private final PlayerEngine engine;

    private boolean ignoreSelectionChanges = false; // true while WE change the selection

    public PlaylistManager(ObservableList<MediaItem> items,
                           MultipleSelectionModel<MediaItem> selection,
                           PlayerEngine engine) {
        this.items = items;
        this.selection = selection;
        this.engine = engine;

       
        selection.selectedIndexProperty().addListener((obs, oldIndex, newIndex) -> {
            int index = newIndex.intValue();
            if (!ignoreSelectionChanges && index >= 0 && index < items.size()) {
                engine.load(items.get(index), true);
            }
        });

        engine.setEndOfMediaAction(this::handleEndOfMedia);
    }

   
    public boolean addBundled(String resourcePath, String displayName) {
        URL url = getClass().getResource(resourcePath);
        if (url == null) {
            return false; 
        }
        items.add(new MediaItem(displayName, url.toExternalForm(), true));
        return true;
    }

   
    public void addUserFile(File file) {
        if (file == null) {
            return; 
        }
        if (!file.isFile()) {
            engine.reportError("The file could not be found:\n" + file.getName());
            return;
        }
        if (!MediaFileTypes.isSupported(file.getName())) {
            engine.reportError("Unsupported file type: " + file.getName()
                    + "\nSupported: MP3");
            return;
        }

        MediaItem item = new MediaItem(file.getName(), file.toURI().toString(), false);
        items.add(item);
        engine.setStatus("Added: " + item.getDisplayName());

        if (selection.getSelectedIndex() < 0) {
            selection.select(item); 
        }
    }

  

    public void removeSelected() {
        int index = selection.getSelectedIndex();
        if (index < 0) {
            engine.setStatus("Select an item in the playlist first, then click Remove.");
            return;
        }

        String name = items.get(index).getDisplayName();

        // Ignore selection events so removing does not load another item by accident.
        ignoreSelectionChanges = true;
        try {
            engine.unload();            // the selected item is the loaded one
            items.remove(index);
            selection.clearSelection();
        } finally {
            ignoreSelectionChanges = false;
        }
        engine.setStatus("Removed: " + name);
    }

    // ---------- Select / Next / Previous ----------

    /** Loads the first item without playing it (used once at start-up). */
    public void loadFirstPaused() {
        if (items.isEmpty()) {
            return;
        }
        ignoreSelectionChanges = true;
        try {
            selection.select(0);
        } finally {
            ignoreSelectionChanges = false;
        }
        engine.load(items.get(0), false);
    }

    /** Play pressed but no player exists yet. */
    public void startPlayback() {
        if (items.isEmpty()) {
            engine.setStatus("The playlist is empty. Click Add to choose a media file.");
            return;
        }
        int index = selection.getSelectedIndex();
        if (index >= 0) {
            engine.load(items.get(index), true);
        } else {
            selection.select(0);
        }
    }

    /** Next item. Stops at the end (no wrap-around). */
    public void selectNext() {
        int index = selection.getSelectedIndex();
        if (items.isEmpty()) {
            engine.setStatus("The playlist is empty.");
        } else if (index < 0) {
            selection.select(0);
        } else if (index >= items.size() - 1) {
            engine.setStatus("This is the last item in the playlist.");
        } else {
            selection.select(index + 1);
        }
    }

    /** Previous item. Stops at the start (no wrap-around). */
    public void selectPrevious() {
        int index = selection.getSelectedIndex();
        if (items.isEmpty()) {
            engine.setStatus("The playlist is empty.");
        } else if (index < 0) {
            selection.select(0);
        } else if (index == 0) {
            engine.setStatus("This is the first item in the playlist.");
        } else {
            selection.select(index - 1);
        }
    }

    /** When a track finishes: play the next one, or stop after the last. */
    private void handleEndOfMedia() {
        int index = selection.getSelectedIndex();
        if (index >= 0 && index < items.size() - 1) {
            selection.select(index + 1);
        } else {
            engine.stop();
            engine.setStatus("Reached the end of the playlist.");
        }
    }
}
