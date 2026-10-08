package com.biim3210.mediaplayer;

import javafx.geometry.Pos;
import javafx.scene.layout.HBox;


public class VolumeControl extends HBox {

    public VolumeControl(MediaController controller) {
        super(10);
        setAlignment(Pos.CENTER);

        FilledSlider slider = new FilledSlider(0.0, 1.0, controller.volumeProperty().get());
        slider.setPrefWidth(150);
        slider.setFocusTraversable(false);
        
        slider.valueProperty().bindBidirectional(controller.volumeProperty());

        getChildren().addAll(new Icon(Icon.VOLUME_LOW, 20), slider, new Icon(Icon.VOLUME_HIGH, 22));
    }
}
