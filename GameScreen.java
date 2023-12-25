package com.example.demo2;

import javafx.animation.KeyFrame;
import javafx.animation.ParallelTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameScreen {

    private final Stage primaryStage;
    private final String characterImagePath;
    private Pane gamePane;
    private ImageView characterView;
    private ImageView oneImageView;
    private double stickHeight;
    private boolean characterStatus;
    private List<ImageView> sticks = new ArrayList<>();
    private ObjectProperty<Timeline> stickUserData = new SimpleObjectProperty<>();
    private boolean stickVisible;
    private int gemCount;
    private boolean isGameOver;
    private ImageView stick;

    private boolean isPaused=false;
    public GameScreen(Stage primaryStage, String characterImagePath) {
        stickVisible = false;
        this.primaryStage = primaryStage;
        this.characterImagePath = characterImagePath;
        isGameOver = false;
    }

    public void show() {
        StackPane root = new StackPane();
        gamePane = new Pane();
        Scene scene = new Scene(root, 1600, 1000);

        ImageView backgroundView = new ImageView(new Image("/screen3.jpg"));
        backgroundView.setFitWidth(scene.getWidth());
        backgroundView.setFitHeight(scene.getHeight());
        root.getChildren().add(backgroundView);

        List<ImageView> pillars = createPillars(scene.getWidth(), scene.getHeight());
        double pillarWidth = 400;
        double pillarHeight = 600;

        for (ImageView pillar : pillars) {
            pillar.setTranslateY(scene.getHeight() - pillarHeight);
        }

        double firstPillarX = scene.getWidth() / 2 - pillarWidth / 2;
        pillars.get(0).setTranslateX(firstPillarX);

        characterView = new ImageView(new Image(characterImagePath));
        characterView.setFitWidth(400);
        characterView.setFitHeight(500);
        double characterInitialY = scene.getHeight() - pillarHeight - characterView.getFitHeight() / 2;
        characterView.setTranslateX(firstPillarX + (pillarWidth - 100) / 2);
        characterView.setTranslateY(characterInitialY);
        characterView.setLayoutX(-900);
        characterView.setLayoutY(-150);

        root.getChildren().add(gamePane);
        gamePane.getChildren().addAll(pillars);
        gamePane.getChildren().add(characterView);

        // Add a label to display gem count
        javafx.scene.control.Label gemCountLabel = new javafx.scene.control.Label("Gems Collected: 0");
        gemCountLabel.setStyle("-fx-text-fill: white");

        // Create a box to display gem count at the center top
        javafx.scene.layout.HBox gemCountBox = new javafx.scene.layout.HBox();
        gemCountBox.setAlignment(javafx.geometry.Pos.TOP_CENTER);
        gemCountBox.setStyle("-fx-background-color: rgba(0, 0, 0, 0.5); -fx-padding: 10px;");

        gemCountBox.getChildren().add(gemCountLabel);
        root.getChildren().add(gemCountBox);

        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.LEFT) {
                moveCharacter(-10);
            } else if (event.getCode() == KeyCode.RIGHT) {
                moveCharacter(10);
            } else if (event.getCode() == KeyCode.W) {
                createStick();
            } else if (event.getCode() == KeyCode.A) {
                fallDown();
            } else if (event.getCode() == KeyCode.T) {
                stopGameAndDisplayMessage("Game Over. Press 'Play Again' to restart.");
            }
            else if (event.getCode() == KeyCode.P && !isGameOver && !isPaused) {
                // Pause the game
                pauseGame();
            } else if (event.getCode() == KeyCode.R && isPaused) {
                // Resume the game
                resumeGame();
            } else if (event.getCode() == KeyCode.N && isPaused) {
                // Start a new game
                resetGame();
            }
        });

        scene.setOnKeyReleased(event -> {
            if (event.getCode() == KeyCode.W) {
                stopStickGrowth();
            }
        });

        // Add a timeline to check for crossed gems periodically
        Timeline gemCheckTimeline = new Timeline(
                new KeyFrame(Duration.seconds(1), event -> checkCharacterOnStickAndGems(gemCountLabel))
        );
        gemCheckTimeline.setCycleCount(Timeline.INDEFINITE);
        gemCheckTimeline.play();

        primaryStage.setScene(scene);
        primaryStage.show();
    }
    public void pauseGame() {
        isPaused = true;

        // Add UI elements or overlay indicating the game is paused
        // ...

        // Add UI elements for resuming and restarting
        Button resumeButton = new Button("Resume");
        resumeButton.setOnAction(event -> resumeGame());
        resumeButton.setLayoutX(primaryStage.getWidth() / 2 - 50);
        resumeButton.setLayoutY(primaryStage.getHeight() / 2 - 30);

        Button restartButton = new Button("Restart");
        restartButton.setOnAction(event -> resetGame());
        restartButton.setLayoutX(primaryStage.getWidth() / 2 - 50);
        restartButton.setLayoutY(primaryStage.getHeight() / 2 + 30);

        gamePane.getChildren().addAll(resumeButton, restartButton);
    }

    public void resumeGame() {
        isPaused = false;

        // Remove UI elements or overlay indicating the game is paused
        // ...

        // Remove UI elements for resuming and restarting
        gamePane.getChildren().removeIf(node ->
                node instanceof Button && ("Resume".equals(((Button) node).getText())
                        || "Restart".equals(((Button) node).getText())));
    }

    private void moveCharacter(double deltaX) {
        double newX = characterView.getTranslateX() + deltaX;

        if (newX >= 0 && newX + characterView.getFitWidth() <= gamePane.getWidth()) {
            characterView.setTranslateX(newX);

            // Check if character is reaching the right edge
            if (newX + characterView.getFitWidth() >= gamePane.getWidth()) {
                // Transition to the left side and generate new pillars
                transitionToNextSetOfPillars();
            }
        }
    }

    private void transitionToNextSetOfPillars() {
        double transitionDistance = gamePane.getWidth() /4;

        // Move character and pillars simultaneously
        ParallelTransition parallelTransition = new ParallelTransition();

        // Move character to the left side with a smooth transition
        TranslateTransition characterTransition = new TranslateTransition(Duration.seconds(2), characterView);
        characterTransition.setToX(characterView.getTranslateX() - transitionDistance);
        parallelTransition.getChildren().add(characterTransition);

        // Move existing set of pillars to the left
        for (Node pillar : gamePane.getChildren()) {
            TranslateTransition pillarTransition = new TranslateTransition(Duration.seconds(2), pillar);
            pillarTransition.setToX(pillar.getTranslateX() - transitionDistance);
            parallelTransition.getChildren().add(pillarTransition);
        }

        // Play the parallel transition
        parallelTransition.play();
    }

    public void stickRotate() {
        double x = oneImageView.getLayoutX();
        double y = oneImageView.getLayoutY();

        oneImageView.setTranslateX(x - oneImageView.getFitWidth() / 2);
        oneImageView.setTranslateY(y);

        oneImageView.setRotate(-90);

        oneImageView.setLayoutX(x);
        oneImageView.setLayoutY(y);
    }

    private void createStick() {
        if (stick == null) {
            double stickHeight = 10;
            stick = new ImageView(new Image("/stick.png"));
            stick.setFitWidth(characterView.getFitWidth() / 8); // One-fourth of character's width
            stick.setFitHeight(stickHeight);
            stick.setTranslateX(characterView.getTranslateX() + characterView.getFitWidth() / 2 - stick.getFitWidth() / 2);
            stick.setTranslateY(characterView.getTranslateY() - stickHeight);
            gamePane.getChildren().add(stick);
            stick.setLayoutX(-900);
            stick.setLayoutY(80);

            // Use a Timeline for continuous growth
            Timeline timeline = new Timeline(new KeyFrame(Duration.millis(100), event -> {
                double newHeight = stick.getFitHeight() + 10; // Adjust the rate at which the stick grows
                stick.setFitHeight(newHeight);
                stick.setTranslateY(characterView.getTranslateY() - newHeight);
            }));
            timeline.setCycleCount(Timeline.INDEFINITE); // Repeat indefinitely
            timeline.play();
            stickUserData.set(timeline);
        }
    }

    private void stopStickGrowth() {
        if (stick != null) {
            // Stop further growth
            Timeline timeline = stickUserData.get();
            if (timeline != null) {
                timeline.stop();
            }

            // Rotate the stick by 90 degrees
            stick.setRotate(90);

            // Adjust stick position to stay within the screen
            double stickX = characterView.getTranslateX() + characterView.getFitWidth();
            double stickY = characterView.getTranslateY() + characterView.getFitHeight() / 2 - stick.getFitHeight() / 2;

            double maxY = gamePane.getHeight() - stick.getFitHeight();


            stick.setTranslateX(stickX);
            stick.setTranslateY(Math.min(stickY, maxY));
            stick.setLayoutY(80);
            gamePane.getChildren().add(stick);

            // Check if the character reaches the other side of the stick
            checkCharacterOnStick();
        }
    }

    private void checkCharacterOnStick() {
        // Iterate through sticks and check if the character is on any of them
        for (ImageView stick : sticks) {
            if (characterView.getBoundsInParent().intersects(stick.getBoundsInParent())) {
                // The character is on the stick
                stopGameAndDisplayMessage("Game Over. Character on the stick. Press 'Play Again' to restart.");
            }
        }
    }

    private void stopGameAndDisplayMessage(String message) {
        // Stop the game and display a message
        isGameOver = true;

        // Add any specific actions or logic here, such as showing a message or stopping animations

        // Simulate falling animation for the character
        TranslateTransition fallTransition = new TranslateTransition(Duration.seconds(1), characterView);
        fallTransition.setToY(primaryStage.getHeight()); // Move character to the bottom of the screen
        fallTransition.setOnFinished(event -> showPlayAgainButton());
        fallTransition.play();
    }

    private void eraseSticks() {
        gamePane.getChildren().removeAll(sticks);
        sticks.clear();

        Timeline timeline = stickUserData.get();
        if (timeline != null) {
            timeline.stop();
        }
    }

    private void checkCharacterOnStickAndGems(javafx.scene.control.Label gemCountLabel) {
        checkCharacterOnStick();
        checkGems(gemCountLabel);
    }

    private void checkGems(javafx.scene.control.Label gemCountLabel) {
        // Check if character crossed a gem
        List<Node> gemsToRemove = new ArrayList<>();
        for (Node node : gamePane.getChildren()) {
            if (node instanceof ImageView && ((ImageView) node).getImage().getUrl().contains("gem.png")) {
                if (characterView.getBoundsInParent().intersects(node.getBoundsInParent())) {
                    gemsToRemove.add(node);
                    gemCount++;
                }
            }
        }

        // Remove gems and update gem count label
        gamePane.getChildren().removeAll(gemsToRemove);
        gemCountLabel.setText("Gems Collected: " + gemCount);
    }

    private void fallDown() {
        if (!isGameOver) {
            isGameOver = true;

            // Stop the game
            stopGameAndDisplayMessage("Game Over. Press 'Play Again' to restart.");

            // Simulate falling animation for the character
            TranslateTransition fallTransition = new TranslateTransition(Duration.seconds(1), characterView);
            fallTransition.setToY(primaryStage.getHeight()); // Move character to the bottom of the screen
            fallTransition.setOnFinished(event -> showPlayAgainButton());
            fallTransition.play();
        }
    }


    private void showPlayAgainButton() {
        // Add a play again button after the falling animation completes
        Button playAgainButton = new Button("Play Again");
        playAgainButton.setOnAction(event -> resetGame());
        playAgainButton.setLayoutX(primaryStage.getWidth() / 2 - 50);
        playAgainButton.setLayoutY(primaryStage.getHeight() / 2);

        gamePane.getChildren().add(playAgainButton);
    }

    private void resetGame() {
        isGameOver = false;

        // Remove the play again button
        gamePane.getChildren().clear();

        // Reset game elements
        sticks.clear();
        gemCount = 0;

        // Call the show method to start a new game
        show();
    }

    private List<ImageView> createPillars(double sceneWidth, double sceneHeight) {
        List<ImageView> pillars = new ArrayList<>();
        Random rand = new Random();

        double gapBetweenPillars = 3 * (rand.nextDouble() * 200 + 100); // Triple the gap between pillars

        for (int i = 0; i < 8; i++) {
            Image pillarImage = new Image("/" + (i + 1) + ".png");
            ImageView pillarView = new ImageView(pillarImage);

            double pillarWidth = (rand.nextDouble() * 200 + 100) * 2;
            double pillarHeight = sceneHeight / 2;
            double pillarX = i * gapBetweenPillars; // Adjust the pillarX to create spacing
            double pillarY = sceneHeight - pillarHeight;

            pillarView.setFitWidth(pillarWidth);
            pillarView.setFitHeight(pillarHeight);
            pillarView.setTranslateX(pillarX);
            pillarView.setTranslateY(pillarY);

            // Insert a gem with a certain probability
            if (rand.nextDouble() < 0.2) { // Adjust the probability as needed
                Image gemImage = new Image("/gem.png"); // Change the path accordingly
                ImageView gemView = new ImageView(gemImage);

                // Set gem dimensions and position it within the pillar's height
                double gemWidth = 30;
                double gemHeight = 30;
                double gemX = pillarX + (pillarWidth - gemWidth) / 2;
                double gemY = pillarY - gemHeight - rand.nextDouble() * 100; // Adjust the height range

                gemView.setFitWidth(gemWidth);
                gemView.setFitHeight(gemHeight);
                gemView.setTranslateX(gemX);
                gemView.setTranslateY(gemY);

                gamePane.getChildren().add(gemView);
            }

            pillars.add(pillarView);
        }

        Image pill1Image = new Image("/pill1.png"); // Change the path accordingly
        ImageView pill1View = new ImageView(pill1Image);
        double pill1Width = 400; // Adjust the width as needed
        double pill1Height = 400; // Adjust the height as needed
        double pill1X = sceneWidth - pill1Width; // Place at the extreme right
        double pill1Y = sceneHeight - pill1Height; // Adjust the Y position as needed

        pill1View.setFitWidth(pill1Width);
        pill1View.setFitHeight(pill1Height);
        pill1View.setX(-30);
        pill1View.setY(410);

        gamePane.getChildren().add(pill1View);

        return pillars;
    }
}
