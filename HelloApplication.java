package com.example.demo2;


import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.text.Font;
import javafx.stage.Stage;



public class HelloApplication extends Application {
    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) {

        Group root = new Group();
        Scene scene = new Scene(root, 1600, 1000);
        primaryStage.setScene(scene);
        primaryStage.setTitle("STICKMAN GAME");
        Image icon = new Image(getClass().getResource("/icon.jpg").toExternalForm());
        primaryStage.getIcons().add(icon);
        primaryStage.setFullScreen(true);
        Font.loadFont(getClass().getResourceAsStream("/Fonts/Valorax-lg25V.otf"), 14);

        String videoPath = getClass().getResource("/vid1.mp4").toExternalForm();
        Media media = new Media(videoPath);

        // Create a MediaPlayer to play the video
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setAutoPlay(true);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE); // Loop indefinitely

        // Create a MediaView to display the video
        MediaView mediaView = new MediaView(mediaPlayer);

        // Set the size of the MediaView to cover the entire scene
        mediaView.setFitWidth(scene.getWidth());
        mediaView.setFitHeight(scene.getHeight());
        mediaView.setX(-50);

        // Add the MediaView to the root
        root.getChildren().add(mediaView);


        Button playButton = new Button("START");
        playButton.setLayoutX(700);
        playButton.setLayoutY(500);
        playButton.setPrefWidth(100);
        playButton.setPrefHeight(50);
        playButton.setStyle("-fx-background-color: black; -fx-text-fill: white; -fx-font-weight: bold;");
        playButton.setFont(Font.font("Verdana", 14));

        playButton.setOnAction(e -> playVid2(primaryStage));
        root.getChildren().add(playButton);



        MenuBar menuBar = new MenuBar();
        Menu fileMenu = new Menu("MENU BAR");

        // Music Option
        CheckMenuItem musicMenuItem = new CheckMenuItem("Turn Music On/Off");
        musicMenuItem.setSelected(true); // Set initial state
        musicMenuItem.setOnAction(event -> {
            // Add logic to turn music on or off
            boolean isMusicOn = musicMenuItem.isSelected();
            System.out.println("Music is " + (isMusicOn ? "On" : "Off"));
        });

        // Help/About Option
        MenuItem helpMenuItem = new MenuItem("Help/About");
        helpMenuItem.setOnAction(event -> showHelpDialog());

        // Check Score Option
        MenuItem checkScoreMenuItem = new MenuItem("Check Your Score");
        checkScoreMenuItem.setOnAction(event -> {
            // Add logic to check and display the score
            System.out.println("Checking the score...");
        });

        fileMenu.getItems().addAll(musicMenuItem, helpMenuItem, checkScoreMenuItem);
        menuBar.getMenus().add(fileMenu);

        root.getChildren().add(menuBar);

        VBox vBox = new VBox(menuBar);
        vBox.setLayoutX(700);
        vBox.setLayoutY(600);
        vBox.setPrefHeight(400);
        vBox.setPrefWidth(100);

        root.getChildren().add(vBox);

        primaryStage.show();

    }

    private void showHelpDialog() {
        Alert helpDialog = new Alert(Alert.AlertType.INFORMATION);
        helpDialog.setTitle("Help/About");
        helpDialog.setHeaderText("Stickman Game Help");
        helpDialog.setContentText("This is a stickman game. Enjoy playing!");
        helpDialog.showAndWait();
    }

    private void playVid2(Stage primaryStage) {
        Group vid2Root = new Group();
        Scene vid2Scene = new Scene(vid2Root, 1600, 1000);

        String videoPath2 = getClass().getResource("/vid2.mp4").toExternalForm();
        Media media2 = new Media(videoPath2);

        // Create a MediaPlayer to play the second video
        MediaPlayer mediaPlayer2 = new MediaPlayer(media2);
        mediaPlayer2.setAutoPlay(true);

        // Create a MediaView to display the second video
        MediaView mediaView2 = new MediaView(mediaPlayer2);
        mediaView2.setFitWidth(vid2Scene.getWidth());
        mediaView2.setFitHeight(vid2Scene.getHeight());
        vid2Root.getChildren().add(mediaView2);

        // Play vid2 only once
        mediaPlayer2.setOnEndOfMedia(() -> {
            mediaPlayer2.stop();
            vid2Root.getChildren().remove(mediaView2); // Remove video2
            showCharacterSelection(primaryStage); // After vid2, show the character selection screen
        });

        primaryStage.hide(); // Hide the primaryStage

        Stage vid2Stage = new Stage();
        vid2Stage.setScene(vid2Scene);
        vid2Stage.show();
    }
    private void showCharacterSelection(Stage primaryStage) {
        Group characterSelectionRoot = new Group();
        Scene characterSelectionScene = new Scene(characterSelectionRoot, 1600, 1000);

        // Create a VBox for character selection
        VBox characterSelectionBox = new VBox(20);
        characterSelectionBox.setAlignment(Pos.CENTER);

        // Load background image for screen 2
        Image gameImage = new Image("/screen2.png");
        ImageView gameImageView = new ImageView(gameImage);
        gameImageView.setPreserveRatio(true);
        gameImageView.setFitWidth(characterSelectionScene.getWidth());
        gameImageView.setFitHeight(characterSelectionScene.getHeight());
        characterSelectionRoot.getChildren().add(gameImageView);
        gameImageView.setX(-50);

        // Create ImageViews for character selection
        Image character1Image = new Image("/image1.png");
        Image character2Image = new Image("/image1.1.png");

        ImageView character1View = new ImageView(character1Image);
        ImageView character2View = new ImageView(character2Image);

        // Set the size of characters to half and center align them
        double characterWidth = character1Image.getWidth()*2 ;
        double characterHeight = character1Image.getHeight() *2;


        character1View.setFitWidth(characterWidth);
        character1View.setFitHeight(characterHeight);
        character1View.setX(-300); // Adjust the X position

        character2View.setFitWidth(characterWidth);
        character2View.setFitHeight(characterHeight);
        character2View.setX(400); // Adjust the X position

        // Center characters vertically
        character1View.setY(-200);
        character2View.setY(-200);

        character1View.setOnMouseClicked(e -> startGame("/image1.png"));
        character2View.setOnMouseClicked(e -> startGame("/image1.1.png"));

        characterSelectionRoot.getChildren().addAll(character1View, character2View);

        characterSelectionRoot.getChildren().add(characterSelectionBox);

        Stage characterSelectionStage = new Stage();
        characterSelectionStage.setScene(characterSelectionScene);
        characterSelectionStage.show();

        primaryStage.hide(); // Hide the primaryStage
    }



    private void startGame(String characterImagePath) {
        Group gameRoot = new Group();
        Scene gameScene = new Scene(gameRoot, 1600, 1000);

        Image characterImage = new Image(characterImagePath);
        ImageView characterView = new ImageView(characterImage);
        characterView.setX(500);
        characterView.setY(300);
        gameRoot.getChildren().add(characterView);
        characterView.setFitWidth(characterImage.getWidth() / 2);
        characterView.setFitHeight(characterImage.getHeight() / 2);

        Stage gameStage = new Stage();
        gameStage.setScene(gameScene);
        GameScreen gameScreen = new GameScreen(gameStage, characterImagePath); // Pass the gameStage to GameScreen
        gameStage.show();
        gameScreen.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
