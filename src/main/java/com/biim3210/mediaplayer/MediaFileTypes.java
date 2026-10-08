package com.biim3210.mediaplayer;

import java.util.Locale;


public final class MediaFileTypes {

    private static final String[] EXTENSIONS =
            {".mp3"};

    private MediaFileTypes() { }

    
    public static boolean isSupported(String fileName) {
        String lower = fileName.toLowerCase(Locale.ROOT);
        for (String extension : EXTENSIONS) {
            if (lower.endsWith(extension)) {
                return true;
            }
        }
        return false;
    }

   
    public static String[] fileChooserPatterns() {
        String[] patterns = new String[EXTENSIONS.length];
        for (int i = 0; i < EXTENSIONS.length; i++) {
            patterns[i] = "*" + EXTENSIONS[i];
        }
        return patterns;
    }
}
