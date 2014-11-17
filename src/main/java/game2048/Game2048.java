package game2048;

import javafx.application.Application;
import javafx.application.ConditionalFeature;
import javafx.application.Platform;
import javafx.geometry.Bounds;
import javafx.geometry.Rectangle2D;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 * @author bruno.borges@oracle.com
 */
public class Game2048 extends Application {

    private GameManager gameManager1;
    private GameManager gameManager2;

    private Bounds gameBounds;
    private final static int MARGIN = 36;

    private static final String JOYSTICK_1_UID = "gSq";
    private static final String JOYSTICK_2_UID = "hDp";
    private static final String DISPLAY_UID = "kTU";
    private static final String DUAL_BUTTON_UID = "j3V";

    @Override
    public void init() {
        // Downloaded from https://01.org/clear-sans/blogs
        // The font may be used and redistributed under the terms of the Apache License, Version 2.0.
        Font.loadFont(Game2048.class.getResource("ClearSans-Bold.ttf").toExternalForm(), 10.0);
    }

    @Override
    public void start(Stage primaryStage) {
        gameManager1 = new GameManager();
        gameManager1.setPlayerNum(1);
        gameBounds = gameManager1.getLayoutBounds();

        gameManager2 = new GameManager();
        gameManager2.setPlayerNum(2);
//        gameBounds = gameManager2.getLayoutBounds();

        HBox root = new HBox();
        root.setSpacing(20);
        root.getChildren().add(gameManager1);
        root.getChildren().add(gameManager2);

        Scene scene = new Scene(root);
        scene.getStylesheets().add("game2048/game.css");
//        addKeyHandler(scene);
//        addSwipeHandlers(scene);
        addTinkerforgeJoystickHandler(gameManager1, JOYSTICK_1_UID);
        addTinkerforgeJoystickHandler(gameManager2, JOYSTICK_2_UID);

        addTinkerforge7SegmentDisplay();


        if (isARMDevice()) {
            primaryStage.setFullScreen(true);
            primaryStage.setFullScreenExitHint("");
        }

        if (Platform.isSupported(ConditionalFeature.INPUT_TOUCH)) {
            scene.setCursor(Cursor.NONE);
        }

        Rectangle2D visualBounds = Screen.getPrimary().getVisualBounds();
        double factor = Math.min(visualBounds.getWidth() / (gameBounds.getWidth() + MARGIN),
                visualBounds.getHeight() / (gameBounds.getHeight() + MARGIN));
        primaryStage.setTitle("2048FX");
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(gameBounds.getWidth() / 2d);
        primaryStage.setMinHeight(gameBounds.getHeight() / 2d);

        //Doubled with for two games
        primaryStage.setWidth((gameBounds.getWidth() + MARGIN) * 2);
        primaryStage.setHeight((gameBounds.getHeight() + MARGIN) * factor);
        primaryStage.show();
    }

    private void addTinkerforge7SegmentDisplay() {
        try {
            new TinkerforgeDisplayHandler(DISPLAY_UID, DUAL_BUTTON_UID);
        } catch (
                Exception e) {
            System.err.println("Tinkerforge not connectetd");
        }
    }

    private void addTinkerforgeJoystickHandler(GameManager gameManager, String uid) {
        try {
            new TinkerforgeJoystickHandler(gameManager, uid);
        } catch (
                Exception e) {
            System.err.println("Tinkerforge not connectetd");
        }
    }

    private boolean isARMDevice() {
        return System.getProperty("os.arch").toUpperCase().contains("ARM");
    }

//    private void addKeyHandler(Scene scene) {
//        scene.setOnKeyPressed(ke -> {
//            KeyCode keyCode = ke.getCode();
//            if (keyCode.equals(KeyCode.S)) {
//                gameManager.saveSession();
//                return;
//            }
//            if (keyCode.equals(KeyCode.R)) {
//                gameManager.restoreSession();
//                return;
//            }
//            if (keyCode.equals(KeyCode.P)) {
//                gameManager.pauseGame();
//                return;
//            }
//            if (keyCode.equals(KeyCode.Q) || keyCode.equals(KeyCode.ESCAPE)) {
//                gameManager.quitGame();
//                return;
//            }
//            if (keyCode.isArrowKey()) {
//                Direction direction = Direction.valueFor(keyCode);
//                gameManager.move(direction);
//            }
//        });
//    }

//    private void addSwipeHandlers(Scene scene) {
//        scene.setOnSwipeUp(e -> gameManager.move(Direction.UP));
//        scene.setOnSwipeRight(e -> gameManager.move(Direction.RIGHT));
//        scene.setOnSwipeLeft(e -> gameManager.move(Direction.LEFT));
//        scene.setOnSwipeDown(e -> gameManager.move(Direction.DOWN));
//    }

    @Override
    public void stop() {
        gameManager1.saveRecord();
        gameManager2.saveRecord();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
