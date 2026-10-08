#  Assignment 2 – JavaFX Media Player

## 1. Project Overview

A simple desktop media player written in JavaFX. It plays MP3 audio files, has a playlist, and can be controlled with both on-screen buttons and keyboard shortcuts.

The design is deliberately small: three Java classes, one stylesheet, and sample media. The goal is code that is easy to read, run, demonstrate and explain.

## 2. Assignment Requirements Implemented

**Part A – Basic Media Player**
- JavaFX application (`Main extends Application`).
- `MediaView` shows video.
- Bundled sample media (`sample.mp3`) loads and plays.
- Play, Pause and Stop work.

**Part B – Keyboard Controls** (see the table in section 18)

**Part C – Playlist Management**
- `ListView` displays the playlist.
- Add uses `FileChooser`.
- Remove deletes the selected item.
- Selecting an item automatically loads and plays it.

## 3. Project Folder Structure

```
BIIM3210-MediaPlayer/
├── pom.xml                                  Maven build configuration
├── README.md                                this file
└── src/main/
    ├── java/com/biim3210/mediaplayer/
    │   ├── Main.java                        creates objects, connects them, shows the window
    │   │
    │   │   --- Logic (no screen code) ---
    │   ├── MediaController.java             the one class the UI calls (buttons + keys)
    │   ├── PlayerEngine.java                owns the MediaPlayer (load/play/pause/stop/dispose/seek)
    │   ├── PlaylistManager.java             add / remove / next / previous / select
    │   ├── VolumeModel.java                 volume (0.0-1.0) and mute flag
    │   ├── TimeModel.java                   current time and total length
    │   ├── MediaItem.java                   one playlist entry (data only)
    │   ├── MediaFileTypes.java              list of supported file extensions
    │   ├── KeyboardHandler.java             all keyboard shortcuts
    │   │
    │   │   --- Screen (what you see) ---
    │   ├── TopBar.java                      menu button, title, Add button
    │   ├── PlaylistPanel.java               the sidebar: ListView + Remove button, show/hide
    │   ├── PlaylistCell.java                how one playlist row looks
    │   ├── VideoArea.java                   the MediaView area
    │   ├── ControlBar.java                  rounded panel: buttons + seek bar + volume
    │   ├── SeekBar.java                     time labels and progress slider
    │   ├── VolumeControl.java               volume slider
    │   ├── StatusLine.java                  status message and key hints
    │   │
    │   │   --- Small helpers ---
    │   ├── Icon.java                        vector icons (drawn with SVG paths, no images)
    │   ├── FilledSlider.java                slider with a blue filled track
    │   ├── AppButton.java                   button that does not take keyboard focus
    │   ├── FileAdder.java                   opens the FileChooser
    │   └── ErrorDialog.java                 error message box
    └── resources/
        ├── css/style.css                    colours and layout styling
        └── media/
            └── sample.mp3                   bundled sample audio
```

Every file is short. Answer to "where is ...?":

| If the lecturer asks... | Open this file |
|---|---|
| Where is the keyboard code? | `KeyboardHandler.java` |
| Where is Play / Pause / Stop? | `PlayerEngine.java` |
| Where is the MediaPlayer created and disposed? | `PlayerEngine.java` (`load`, `release`) |
| Where is Add / Remove / Next / Previous? | `PlaylistManager.java` |
| Where is the FileChooser? | `FileAdder.java` |
| Where is volume and mute? | `VolumeModel.java` |
| Where is the sidebar show/hide? | `PlaylistPanel.java` (`toggle`) and `TopBar.java` (menu button) |
| Where is the seek bar? | `SeekBar.java` |
| Where is the MediaView? | `Main.java` (created) and `VideoArea.java` (displayed) |
| Where are the buttons? | `TopBar.java`, `ControlBar.java`, `PlaylistPanel.java` |

## 4. Purpose of Each Class

| Class | Responsibility |
|---|---|
| `Main` | Creates the objects, connects them, builds the layout and shows the window. No playback logic. |
| `MediaController` | The single entry point for the UI. Every button and every key calls a method here, and it passes the work to the right class. This is why a button and its key can never behave differently. |
| `PlayerEngine` | Owns the one `MediaPlayer`. Creates `Media` + `MediaPlayer`, plays, pauses, stops, seeks, disposes. Knows nothing about the playlist. |
| `PlaylistManager` | Manages the `ObservableList`: add bundled/user files, remove, next, previous, and loads the selected item into the `PlayerEngine`. |
| `VolumeModel` | Stores the volume (0.0-1.0) and mute flag. Muting never changes the volume number. |
| `TimeModel` | Stores the current position and total length in seconds, and formats them as `m:ss`. |
| `MediaItem` | One playlist entry: display name, media source (URI string), whether it is bundled, and its duration text. Only stores information. |
| `MediaFileTypes` | The one list of supported extensions (used for validation and the FileChooser filter). |
| `KeyboardHandler` | Registers the Scene key filter and maps each key to a `MediaController` method. |
| `TopBar`, `PlaylistPanel`, `PlaylistCell`, `VideoArea`, `ControlBar`, `SeekBar`, `VolumeControl`, `StatusLine` | Small UI building blocks, one per part of the window. |
| `Icon` | A vector icon drawn from an SVG path (no emojis, no image files). |
| `FilledSlider` | A `Slider` whose track is coloured blue up to the thumb. |
| `AppButton` | A `Button` with focus traversal switched off, so Space cannot click a focused button. |
| `FileAdder` | Opens the `FileChooser` and passes the chosen file to the controller. |
| `ErrorDialog` | Shows error messages in an alert box. |

**How the classes call each other:**

```
Buttons / Keys  ->  MediaController  ->  PlaylistManager  ->  PlayerEngine  ->  MediaPlayer
                                     ->  VolumeModel   ----^
```

## 5. Important JavaFX Components

| Component | Purpose |
|---|---|
| `Media` | Represents the media **source** (which file/URI). It does not play anything. |
| `MediaPlayer` | **Controls playback**: play, pause, stop, volume, mute. One `MediaPlayer` is created for one `Media`. |
| `MediaView` | A node that **displays the video** of a `MediaPlayer`. For audio-only files it stays empty, so the file name is shown behind it. |
| `ListView` | Displays the playlist. Its selection model tells us which item is current. |
| `ObservableList` | The list that stores the playlist. The `ListView` watches it, so adding/removing items updates the screen automatically. |
| `FileChooser` | The standard "open file" dialog used by the Add button. |

The relationship: `Media` → `MediaPlayer` → `MediaView`.

## 6. How Media Loading Works

Every playlist item holds a **URI string**. To play it:

```java
Media media = new Media(item.getSource());
MediaPlayer player = new MediaPlayer(media);
mediaView.setMediaPlayer(player);
```

Where the URI string comes from depends on whether the media is bundled or chosen by the user (next section).

## 7. `getClass().getResource(...)` vs `file.toURI().toString()`

**Bundled media** (packaged inside the project):

```java
String mediaPath = getClass().getResource("/media/sample.mp3").toExternalForm();
Media media = new Media(mediaPath);
```

**User-selected media** (chosen with `FileChooser`):

```java
File file = fileChooser.showOpenDialog(stage);
String mediaPath = file.toURI().toString();
Media media = new Media(mediaPath);
```

**Why they are different:**

- A bundled file lives *inside the application* (in `src/main/resources`, which becomes part of the classpath/JAR). Its real location on disk changes depending on the computer and how the program is run, and it may even be inside a `.jar` file where there is no normal file path. `getResource(...)` asks Java to **find the file on the classpath**, wherever that is. `toExternalForm()` turns the found location into a URL string that `Media` accepts.
- A user-selected file lives **outside the application**, anywhere on the user's disk. It is not on the classpath, so `getResource` cannot find it. But `FileChooser` already gives us a real `File`, and `file.toURI().toString()` converts it into the URI string `Media` needs (it also correctly encodes spaces and special characters).

| | Bundled media | User-selected media |
|---|---|---|
| Location | Inside the project | Anywhere on the computer |
| How found | `getClass().getResource("/media/...")` | `FileChooser` returns a `File` |
| Convert to string | `.toExternalForm()` | `file.toURI().toString()` |
| Method in code | `PlaylistManager.addBundled` | `PlaylistManager.addUserFile` |

## 8. Why Bundled Media Is Stored in `resources`

- No hard-coded absolute paths such as `C:\Users\John\Desktop\song.mp3`, which only work on one computer.
- The media travels with the project, so the lecturer can run it anywhere.
- Maven copies `src/main/resources` onto the classpath automatically, and into the JAR if the project is packaged.

No online URLs are used anywhere in the project.

## 9. How Playlist Management Works

The playlist is an `ObservableList<MediaItem>` created in `Main` and shown by a `ListView<MediaItem>`. **The selected row in the ListView is the current item.** `PlaylistManager` listens to the selection:

```java
selectionModel.selectedIndexProperty().addListener((obs, oldIndex, newIndex) -> {
    int index = newIndex.intValue();
    if (!ignoreSelectionChanges && index >= 0 && index < items.size()) {
        engine.load(items.get(index), true);
    }
});
```

So clicking an item, pressing Next/Previous, or finishing a song all work the same way: change the selection → the listener loads and plays that item.

## 10. How Add Works

1. User clicks **Add** → `FileAdder.chooseAndAdd()` opens a `FileChooser` (filtered to supported media types).
2. If the user cancels, `showOpenDialog` returns `null` and `addUserFile` simply returns – nothing happens.
3. `addUserFile` checks the file exists and has a supported extension. Otherwise an error dialog explains the problem.
4. A `MediaItem` is created using `file.toURI().toString()` and added to the `ObservableList`.
5. The `ListView` updates automatically. If nothing was selected yet, the new item is selected and starts playing.

## 11. How Remove Works

1. User selects an item and clicks **Remove** (`PlaylistManager.removeSelected()`).
2. If nothing is selected, a status message is shown and nothing else happens.
3. Otherwise the selected item is the loaded one, so the `MediaPlayer` is stopped and disposed, the item is removed from the list, and the selection is cleared.
4. While doing this, selection events are temporarily ignored (`ignoreSelectionChanges`) so that removing does not accidentally load a different item.

The player ends in a clean "nothing loaded" state; the user then picks the next item.

## 12. How Selecting a Playlist Item Works

`PlayerEngine.load(item, autoPlay)` does the following (called by `PlaylistManager`):

1. Get the selected `MediaItem`.
2. Stop and dispose the old `MediaPlayer`.
3. Create a new `Media` from the item's source.
4. Create a new `MediaPlayer`.
5. Connect it to the `MediaView`.
6. Apply the current volume and mute settings.
7. Call `play()` (the first bundled item at start-up is only loaded, not played).

## 13. How Next and Previous Work

`PlaylistManager.selectNext()` / `selectPrevious()` just change the ListView selection by +1 / −1; the selection listener does the loading.

**Boundary behaviour: stop at the ends (no wrap-around).** At the last item, Next shows "This is the last item in the playlist." At the first item, Previous shows "This is the first item in the playlist." This avoids confusing jumps and makes `IndexOutOfBoundsException` impossible. When a song finishes naturally, the next one plays automatically; after the last song, playback stops.

## 14. How Play/Pause Works

`MediaController.togglePlayPause()`:
- No player loaded → `PlaylistManager.startPlayback()` (or tells the user the playlist is empty).
- Otherwise → `PlayerEngine.togglePlayPause()`: if the player is `PLAYING` it pauses, otherwise it plays.

The Space key and the Play/Pause button both call `togglePlayPause()`. The button label changes between "Play" and "Pause" automatically because it is bound to `playingProperty()`.

## 15. How Stop Works

`PlayerEngine.stop()` calls `mediaPlayer.stop()`, which returns to the **beginning** of the media. This differs from Pause, which keeps the current position and resumes from there. The S key and the Stop button use this method.

## 16. How Volume Works

- `MediaPlayer` volume ranges from `0.0` to `1.0`.
- `VolumeModel` keeps the volume in a `DoubleProperty` (start value 0.5).
- Up/Down change it by `0.1`, clamped with `Math.max(0.0, Math.min(1.0, value))`, so it can never go below 0 or above 1. The value is rounded to avoid floating-point drift (e.g. `0.30000000000000004`).
- The volume `Slider` is bound bidirectionally to the property, so the slider, the percentage label and the player always agree. A listener in `PlayerEngine` passes every change to `mediaPlayer.setVolume(...)`.
- Each newly created `MediaPlayer` receives the current volume, so the volume survives changing tracks.

## 17. How Mute Works

`VolumeModel.toggleMute()` flips a `BooleanProperty` and a listener in `PlayerEngine` calls `mediaPlayer.setMute(...)`. **The volume value is never changed**, so unmuting restores the previous volume exactly. The M key and the Mute button use the same method; the button text switches between "Mute" and "Unmute". Pressing Up/Down while muted un-mutes, so the user hears the change.

## 18. How Keyboard Controls Work

A key **event filter** is registered on the `Scene` in `KeyboardHandler`:

```java
scene.addEventFilter(KeyEvent.KEY_PRESSED, this::handleKeyPressed);
```

A filter sees the key press *before* any control does. Therefore the shortcuts work regardless of which control has focus, and the event is consumed so that:
- Space does not also "click" a focused button,
- Up/Down do not move the ListView selection or the volume Slider by themselves.

(As a result, use the mouse, N and P to change the playlist selection – not the arrow keys.) Buttons (`AppButton`) are also set non-focus-traversable.

| Key | Action | Method called |
|---|---|---|
| Space | Play / Pause toggle | `togglePlayPause()` |
| S | Stop | `stopMedia()` |
| N | Next item | `playNext()` |
| P | Previous item | `playPrevious()` |
| Up Arrow | Volume +0.1 | `increaseVolume()` |
| Down Arrow | Volume −0.1 | `decreaseVolume()` |
| M | Mute / Unmute | `toggleMute()` |

## 18a. User Interface: Sidebar and Seek Bar

**Layout:** a rounded top bar (menu button, title, Add button), a playlist sidebar on the left, the large video/media area, and a rounded control panel at the bottom.

**Sidebar (menu button):** The playlist sidebar is a `PlaylistPanel`. Clicking the menu button in the top bar (or the chevron button in the sidebar header) calls `PlaylistPanel.toggle()`:

```java
boolean show = !isVisible();
setVisible(show);   // hidden or shown
setManaged(show);   // when hidden it also gives its space back to the video area
```

Click once to hide the playlist, click again to show it. Playback is not affected, and the Add button and all keyboard shortcuts keep working while the sidebar is hidden.

**Seek bar:** `SeekBar` shows the current time (left), a progress slider, and the remaining time (right). While playing, the slider follows `TimeModel`. When the user drags or clicks the slider, `controller.seekTo(...)` calls `mediaPlayer.seek(...)`.

**Playlist durations:** a track's duration appears under its name once it has been loaded (until then it shows `--:--`).

**Icons:** all icons are SVG paths drawn by the `Icon` class, so no image files or emojis are needed.

**Stop button:** it sits to the left of Previous in the control panel (the assignment requires a Stop button).

## 19. Why `MediaPlayer.dispose()` Is Important

A `MediaPlayer` holds **native resources**: a decoder, buffers and an open handle on the media file. Java's garbage collector does not release them promptly. If a new `MediaPlayer` were created for every track without disposing the old one:

- the old player could keep playing (two sounds at once),
- memory and file handles would leak,
- the media file could stay locked.

So `PlayerEngine.release()` always runs `stop()` then `dispose()` before a new player is created, and also when an item is removed and when the window closes.

## 20. Error Handling

| Situation | What happens |
|---|---|
| Empty playlist, press Play/Next/Previous | Status message "The playlist is empty…" |
| Nothing selected, press Remove | Status message; no crash |
| FileChooser cancelled | `null` is checked; nothing happens |
| File missing / wrong extension | Error dialog explaining the problem |
| Corrupted or unsupported media | `setOnError` handlers (and a `try/catch`) show an error dialog and status message; the application keeps running |
| Next at last / Previous at first item | Status message; selection unchanged |
| Pause/Stop with nothing loaded | Status message |
| Removing the playing item | Player is stopped and disposed first |
| Bundled sample missing | Status message telling the user to use Add |

The status line at the bottom of the window shows the result of the latest action.

## 21. How to Run the Project

**Requirements:** JDK 17 or newer and Maven (internet access the first time, to download JavaFX).

```
cd BIIM3210-MediaPlayer
mvn clean javafx:run
```

**IntelliJ IDEA:** File → Open → select the folder (or `pom.xml`) → let Maven import → open the Maven tool window → Plugins → `javafx` → `javafx:run`.

**Eclipse / VS Code:** import as an existing Maven project and run `mvn javafx:run`.

Running `Main` directly with the normal "Run" button may fail with "JavaFX runtime components are missing" unless JavaFX is on the module path; using `javafx:run` avoids that.

## 22. How to Add Bundled Sample Media

1. Put the file in `src/main/resources/media/` (for example `lecture.mp3`).
2. Add one line in `Main.loadBundledMedia()`:

```java
playlist.addBundled("/media/lecture.mp3", "My Lecture");
```

3. Rebuild/run. The path starts with `/` and is relative to the `resources` folder.

This project uses **MP3 only**. The provided `sample.mp3` is a short generated test tone; replace it with your own MP3 of the same name if you prefer.

The `MediaView` is still part of the player (as the assignment requires). Because only audio is used, it shows no picture, so the file name is displayed in the video area instead. To allow more formats later, add the extension to the list in `MediaFileTypes.java`.

## 23. Assignment Checklist

- [x] JavaFX application
- [x] `MediaView` used for video
- [x] Media loads and plays
- [x] Play, Pause, Stop
- [x] Space = Play/Pause
- [x] S = Stop
- [x] N = Next
- [x] P = Previous
- [x] Up Arrow = Volume up (max 1.0)
- [x] Down Arrow = Volume down (min 0.0)
- [x] M = Mute/Unmute (volume value preserved)
- [x] `ListView` playlist
- [x] Add with `FileChooser`
- [x] Remove selected item
- [x] Selecting an item loads it into the player
- [x] Bundled media via `getClass().getResource(...).toExternalForm()`
- [x] User media via `file.toURI().toString()`
- [x] No hard-coded absolute paths, no online URLs
- [x] Old `MediaPlayer` stopped and disposed
- [x] Empty playlist, no selection, cancel, invalid file, boundaries handled
