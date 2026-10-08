package com.biim3210.mediaplayer;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;


public class Main extends Application {

    @Override
    public void start(Stage stage) {
        
        MediaView mediaView = new MediaView();
        mediaView.setPreserveRatio(true);

        ObservableList<MediaItem> items = FXCollections.observableArrayList();
        ListView<MediaItem> listView = new ListView<>(items);

        VolumeModel volumeModel = new VolumeModel();
        PlayerEngine engine = new PlayerEngine(mediaView, volumeModel);
        PlaylistManager playlist = new PlaylistManager(items, listView.getSelectionModel(), engine);
        MediaController controller = new MediaController(engine, playlist, volumeModel);

        engine.setErrorReporter(message -> ErrorDialog.show(stage, message));
        loadBundledMedia(playlist, engine);

        
        PlaylistPanel playlistPanel = new PlaylistPanel(listView, controller);
        VideoArea videoArea = new VideoArea(mediaView, controller);
        HBox.setHgrow(videoArea, Priority.ALWAYS);
        HBox middle = new HBox(16, playlistPanel, videoArea); 

        VBox bottom = new VBox(8, new ControlBar(controller), new StatusLine(controller));
        bottom.setAlignment(Pos.CENTER);

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(16));
        root.setTop(new TopBar(stage, controller, playlistPanel::toggle));
        root.setCenter(middle);
        root.setBottom(bottom);
        BorderPane.setMargin(middle, new Insets(16, 0, 16, 0));

        Scene scene = new Scene(root, 1180, 720);
        scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
        new KeyboardHandler(controller).install(scene);

        // 3. Show the window
        stage.setTitle(" Media Player");
        stage.setMinWidth(900);
        stage.setMinHeight(580);
        stage.setScene(scene);
        stage.setOnCloseRequest(event -> controller.shutdown());
        stage.show();
    }

    
    private void loadBundledMedia(PlaylistManager playlist, PlayerEngine engine) {
        boolean found = playlist.addBundled("/media/sample.mp3", "Sample Audio");

        if (found) {
            playlist.loadFirstPaused(); 
        } else {
            engine.setStatus("No bundled media found. Click Add to choose a file.");
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
