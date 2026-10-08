package com.biim3210.mediaplayer;

import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;

/**
 * All keyboard shortcuts live here.
 *
 * The handler is an event FILTER on the Scene, so it receives each key press BEFORE
 * any control (ListView, Slider, Button). That makes the shortcuts work whichever
 * control has focus, and consuming the event stops Space/arrows from doing anything else.
 */
public class KeyboardHandler {

    private final MediaController controller;

    public KeyboardHandler(MediaController controller) {
        this.controller = controller;
    }

    public void install(Scene scene) {
        scene.addEventFilter(KeyEvent.KEY_PRESSED, this::handleKeyPressed);
    }

    private void handleKeyPressed(KeyEvent event) {
        switch (event.getCode()) {
            case SPACE -> controller.togglePlayPause();
            case S -> controller.stopMedia();
            case N -> controller.playNext();
            case P -> controller.playPrevious();
            case UP -> controller.increaseVolume();
            case DOWN -> controller.decreaseVolume();
            case M -> controller.toggleMute();
            default -> {
                return; // not our key: let JavaFX handle it normally
            }
        }
        event.consume();
    }
}
