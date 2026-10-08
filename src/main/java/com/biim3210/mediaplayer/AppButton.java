package com.biim3210.mediaplayer;

import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;


public class AppButton extends Button {

    public AppButton(String text) {
        super(text);
        setFocusTraversable(false);
    }

   
    public AppButton(Icon icon, String styleClass) {
        this("");
        setGraphic(icon);
        setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        getStyleClass().add(styleClass);
    }

   
    public AppButton(String text, Icon icon) {
        this(text);
        setGraphic(icon);
        setGraphicTextGap(8);
        getStyleClass().add("text-button");
    }
}
