package com.biim3210.mediaplayer;

import javafx.scene.layout.StackPane;
import javafx.scene.shape.SVGPath;

public class Icon extends StackPane {

    public static final String MENU = "M3 6h18v2H3z M3 11h18v2H3z M3 16h18v2H3z";
    public static final String PLAY = "M8 5v14l11-7z";
    public static final String PAUSE = "M6 5h4v14H6z M14 5h4v14h-4z";
    public static final String PREVIOUS = "M11 12l9-7v14z M2 12l9-7v14z";
    public static final String NEXT = "M13 12l-9-7v14z M22 12l-9-7v14z";
    public static final String STOP = "M6 6h12v12H6z";
    public static final String VOLUME_LOW = "M3 9v6h4l5 5V4L7 9z";
    public static final String VOLUME_HIGH = "M3 9v6h4l5 5V4L7 9H3zm13.5 3c0-1.77-1.02-3.29-2.5-4.03v8.05c1.48-.73 2.5-2.25 2.5-4.02zM14 3.23v2.06c2.89.86 5 3.54 5 6.71s-2.11 5.85-5 6.71v2.06c4.01-.91 7-4.49 7-8.77s-2.99-7.86-7-8.77z";
    public static final String VOLUME_MUTED = "M16.5 12c0-1.77-1.02-3.29-2.5-4.03v2.21l2.45 2.45c.03-.2.05-.41.05-.63zm2.5 0c0 .94-.2 1.82-.54 2.64l1.51 1.51C20.63 14.91 21 13.5 21 12c0-4.28-2.99-7.86-7-8.77v2.06c2.89.86 5 3.54 5 6.71zM4.27 3L3 4.27 7.73 9H3v6h4l5 5v-6.73l4.25 4.25c-.67.52-1.42.93-2.25 1.18v2.06c1.38-.31 2.63-.95 3.69-1.81L19.73 21 21 19.73l-9-9L4.27 3zM12 4L9.91 6.09 12 8.18V4z";
    public static final String NOTE = "M12 3v10.55c-.59-.34-1.27-.55-2-.55-2.21 0-4 1.79-4 4s1.79 4 4 4 4-1.79 4-4V7h4V3h-6z";
    public static final String TRASH = "M6 19c0 1.1.9 2 2 2h8c1.1 0 2-.9 2-2V7H6v12zM19 4h-3.5l-1-1h-5l-1 1H5v2h14V4z";
    public static final String ADD = "M13 7h-2v4H7v2h4v4h2v-4h4v-2h-4V7zm-1-5C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm0 18c-4.41 0-8-3.59-8-8s3.59-8 8-8 8 3.59 8 8-3.59 8-8 8z";
    public static final String CHEVRON_UP = "M7.41 15.41L12 10.83l4.59 4.58L18 14l-6-6-6 6z";
    public static final String LOGO = "M5 3l15 9-15 9z";

    private final SVGPath path = new SVGPath();

    public Icon(String pathData, double size) {
        path.setContent(pathData);
        path.getStyleClass().add("icon");
        path.setScaleX(size / 24.0);
        path.setScaleY(size / 24.0);

        setMinSize(size, size);
        setPrefSize(size, size);
        setMaxSize(size, size);
        setMouseTransparent(true);
        getChildren().add(path);
    }

   
    public void setPath(String pathData) {
        path.setContent(pathData);
    }

    
    public Icon accent() {
        path.getStyleClass().setAll("accent-icon");
        return this;
    }
}
