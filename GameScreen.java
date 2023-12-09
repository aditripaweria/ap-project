package com.example.demo2;

import javafx.animation.KeyFrame;
import javafx.animation.ParallelTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
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

    private boolean isMovingRight;
    private List<ImageView> sticks = new ArrayList<>();

    private ObjectProperty<Timeline> stickUserData = new SimpleObjectProperty<>();

    private int gemCount;

    private boolean isOverGap = false;
    private boolean isGameOver = false;

    public GameScreen(Stage primaryStage, String characterImagePath) {
        isMovingRight = false;
        this.primaryStage = primaryStage;
        this.characterImagePath = characterImagePath;
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

        Label gemCountLabel = new Label("Gems Collected: 0");
        gemCountLabel.setStyle("-fx-text-fill: white");

        Rectangle scoreBox = new Rectangle(120, 40);
        scoreBox.setFill(Color.BLACK);
        scoreBox.setArcWidth(10);
        scoreBox.setArcHeight(10);

        Label scoreLabel = new Label("Score: 0");
        scoreLabel.setStyle("-fx-text-fill: white");

        javafx.scene.layout.HBox gemCountBox = new javafx.scene.layout.HBox();
        gemCountBox.setAlignment(javafx.geometry.Pos.TOP_CENTER);
        gemCountBox.setStyle("-fx-padding: 10px;");

        gemCountBox.getChildren().addAll(scoreBox, new Label("  "), scoreLabel);

        root.getChildren().add(gemCountBox);

        Timeline characterMoveTimeline = new Timeline(
                new KeyFrame(Duration.millis(16), event -> {
                    if (isMovingRight && !isGameOver) {
                        moveCharacter(5);
                    }
                })
        );
        characterMoveTimeline.setCycleCount(Timeline.INDEFINITE);
        characterMoveTimeline.play();

        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.W && !isGameOver) {
                stickHeight = stickHeight + 10;
                createStick(200, 500, stickHeight);
            } else if (event.getCode() == KeyCode.RIGHT && !isGameOver) {
                isMovingRight = true;
            }
        });

        scene.setOnKeyReleased(event -> {
            if (event.getCode() == KeyCode.W && !isGameOver) {
                stickRotate();
            } else if (event.getCode() == KeyCode.RIGHT && !isGameOver) {
                isMovingRight = false;
            }
        });

        Timeline gemCheckTimeline = new Timeline(
                new KeyFrame(Duration.seconds(1), event -> checkCharacterOnStickAndGems(gemCountLabel, scoreLabel))
        );
        gemCheckTimeline.setCycleCount(Timeline.INDEFINITE);
        gemCheckTimeline.play();

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void moveCharacter(double deltaX) {
        double newX = characterView.getTranslateX() + deltaX;

        if (newX >= 0 && newX + characterView.getFitWidth() <= gamePane.getWidth()) {
            characterView.setTranslateX(newX);

            if (newX + characterView.getFitWidth() >= gamePane.getWidth() / 2 && !isOverGap) {
                checkCharacterOverGap();
            }

            if (newX + characterView.getFitWidth() >= gamePane.getWidth()) {
                transitionToNextSetOfPillars();
            }
        } else if (!isGameOver) {
            fallDown();
        }
    }

    private void checkCharacterOverGap() {
        boolean isStickUnderCharacter = false;
        for (ImageView stick : sticks) {
            if (characterView.getBoundsInParent().intersects(stick.getBoundsInParent())) {
                isStickUnderCharacter = true;
                break;
            }
        }

        if (!isStickUnderCharacter) {
            isOverGap = true;
            fallDown();
        }
    }

    private void transitionToNextSetOfPillars() {
        double transitionDistance = gamePane.getWidth() / 2;

        ParallelTransition parallelTransition = new ParallelTransition();

        TranslateTransition characterTransition = new TranslateTransition(Duration.seconds(2), characterView);
        characterTransition.setToX(characterView.getTranslateX() - transitionDistance);
        parallelTransition.getChildren().add(characterTransition);

        for (Node pillar : gamePane.getChildren()) {
            TranslateTransition pillarTransition = new TranslateTransition(Duration.seconds(2), pillar);
            pillarTransition.setToX(pillar.getTranslateX() - transitionDistance);
            parallelTransition.getChildren().add(pillarTransition);
        }

        parallelTransition.setOnFinished(event -> {
            if (!isGameOver) {
                isOverGap = false;
                eraseSticks();
            }
        });

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

    private void createStick(double x, double y, double stickHeight) {
        double stickWidth = 10;

        if (oneImageView != null) {
            gamePane.getChildren().remove(oneImageView);
        }

        oneImageView = new ImageView(new Image("/stick.png"));
        oneImageView.setLayoutX(x);
        oneImageView.setLayoutY(y - stickHeight);
        oneImageView.setFitWidth(stickWidth);
        oneImageView.setFitHeight(stickHeight);

        gamePane.getChildren().add(oneImageView);
        sticks.add(oneImageView);

        if (!isGameOver) {
            checkCharacterOnStick();
        }
    }

    private void eraseSticks() {
        gamePane.getChildren().removeAll(sticks);
        sticks.clear();

        Timeline timeline = stickUserData.get();
        if (timeline != null) {
            timeline.stop();
        }
    }

    private void checkCharacterOnStickAndGems(Label gemCountLabel, Label scoreLabel) {
        checkCharacterOnStick();
        checkGems(gemCountLabel, scoreLabel);
    }

    private void checkGems(Label gemCountLabel, Label scoreLabel) {
        List<Node> gemsToRemove = new ArrayList<>();
        for (Node node : gamePane.getChildren()) {
            if (node instanceof ImageView && ((ImageView) node).getImage().getUrl().contains("gem.png")) {
                if (characterView.getBoundsInParent().intersects(node.getBoundsInParent())) {
                    gemsToRemove.add(node);
                    gemCount++;
                }
            }
        }

        gamePane.getChildren().removeAll(gemsToRemove);
        gemCountLabel.setText("Gems Collected: " + gemCount);
        scoreLabel.setText("Score: " + gemCount);
    }

    private void checkCharacterOnStick() {
        for (ImageView stick : sticks) {
            if (characterView.getBoundsInParent().intersects(stick.getBoundsInParent())) {
                isOverGap = false;
                eraseSticks();
                return;
            }
        }

        if (!isOverGap) {
            fallDown();
        }
    }

    private void fallDown() {
        if (!isGameOver) {
            isGameOver = true;

            // Stop all animations
            characterView.setRotate(0);
            isMovingRight = false;
            eraseSticks();
            Timeline characterMoveTimeline = new Timeline();
            characterMoveTimeline.stop();

            stopGameAndDisplayMessage("Game Over! Try Again.");
        }
    }

    private void stopGameAndDisplayMessage(String message) {
        // Add code to display the "Game Over" message
        Label gameOverLabel = new Label(message);
        gameOverLabel.setStyle("-fx-font-size: 24; -fx-text-fill: white;");
        StackPane.setAlignment(gameOverLabel, javafx.geometry.Pos.CENTER);
        StackPane root = (StackPane) primaryStage.getScene().getRoot();
        root.getChildren().add(gameOverLabel);
    }

    private List<ImageView> createPillars(double sceneWidth, double sceneHeight) {
        List<ImageView> pillars = new ArrayList<>();
        Random rand = new Random();

        double gapBetweenPillars = 3 * (rand.nextDouble() * 200 + 100);

        Image startPillarImage = new Image("/1.png");
        ImageView startPillarView = new ImageView(startPillarImage);
        double startPillarWidth = (rand.nextDouble() * 200 + 100) * 2;
        double startPillarHeight = sceneHeight / 2;
        startPillarView.setFitWidth(startPillarWidth);
        startPillarView.setFitHeight(startPillarHeight);
        startPillarView.setX(-50);
        startPillarView.setY(sceneHeight / 2);
        pillars.add(startPillarView);

        for (int i = 1; i < 8; i++) {
            Image pillarImage = new Image("/" + (i + 1) + ".png");
            ImageView pillarView = new ImageView(pillarImage);

            double pillarWidth = (rand.nextDouble() * 200 + 100) * 2;
            double pillarHeight = sceneHeight / 2;
            double pillarX = i * gapBetweenPillars;
            double pillarY = sceneHeight - pillarHeight;

            pillarView.setFitWidth(pillarWidth);
            pillarView.setFitHeight(pillarHeight);
            pillarView.setTranslateX(pillarX);
            pillarView.setTranslateY(pillarY);

            pillars.add(pillarView);
        }

        return pillars;
    }
}
