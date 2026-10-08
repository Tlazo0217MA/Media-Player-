package com.biim3210.mediaplayer;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;


public class TopBar extends StackPane {

    public TopBar(Stage stage, MediaController controller, Runnable toggleSidebar) {
        getStyleClass().add("top-bar");

       
        AppButton menuButton = new AppButton(new Icon(Icon.MENU, 22), "icon-button");
        menuButton.setOnAction(event -> toggleSidebar.run());
        StackPane.setAlignment(menuButton, Pos.CENTER_LEFT);

       
        Label title = new Label(" Media Player");
        title.getStyleClass().add("app-title");
        HBox titleBox = new HBox(10, new Icon(Icon.LOGO, 30).accent(), title);
        titleBox.setAlignment(Pos.CENTER);
        titleBox.setMaxWidth(HBox.USE_PREF_SIZE);

       
        AppButton addButton = new AppButton("Add", new Icon(Icon.ADD, 20));
        addButton.setOnAction(event -> FileAdder.chooseAndAdd(stage, controller));
        StackPane.setAlignment(addButton, Pos.CENTER_RIGHT);

        getChildren().addAll(titleBox, menuButton, addButton);
    }
}
