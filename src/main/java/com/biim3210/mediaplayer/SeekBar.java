package com.biim3210.mediaplayer;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;


public class SeekBar extends HBox {

    public SeekBar(MediaController controller) {
        super(16);
        setAlignment(Pos.CENTER);
        TimeModel time = controller.timeModel();

        Label currentLabel = new Label("0:00");
        currentLabel.getStyleClass().add("time-label");

        Label remainingLabel = new Label("-0:00");
        remainingLabel.getStyleClass().add("time-label");
        remainingLabel.setAlignment(Pos.CENTER_RIGHT);

        FilledSlider slider = new FilledSlider(0, 1, 0);
        slider.setFocusTraversable(false);
        HBox.setHgrow(slider, Priority.ALWAYS);

        
        Runnable updateLabels = () -> {
            double current = time.getCurrentSeconds();
            double total = time.totalSecondsProperty().get();
            currentLabel.setText(TimeModel.format(current));
            remainingLabel.setText("-" + TimeModel.format(total - current));
        };

        
        time.currentSecondsProperty().addListener((obs, oldValue, newValue) -> {
            if (!slider.isValueChanging()) {
                slider.setValue(newValue.doubleValue());
            }
            updateLabels.run();
        });
        time.totalSecondsProperty().addListener((obs, oldValue, newValue) -> {
            slider.setMax(Math.max(1.0, newValue.doubleValue()));
            updateLabels.run();
        });

       
        slider.valueProperty().addListener((obs, oldValue, newValue) -> {
            boolean userMoved = slider.isValueChanging()
                    || Math.abs(newValue.doubleValue() - time.getCurrentSeconds()) > 1.0;
            if (userMoved) {
                controller.seekTo(newValue.doubleValue());
            }
        });

        getChildren().addAll(currentLabel, slider, remainingLabel);
    }
}
