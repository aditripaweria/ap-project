import javafx.embed.swing.JFXPanel;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GameScreenTest {

    @BeforeAll
    public static void init() {
        // Initialize JavaFX toolkit, required for testing JavaFX components
        new JFXPanel();
    }

    @Test
    public void testCharacterMovement() {
        // Create a new GameScreen instance
        GameScreen gameScreen = new GameScreen(new Stage(), "/character.png");

        // Ensure that the character starts at the correct position
        assertEquals(-900, gameScreen.getCharacterView().getLayoutX());
        assertEquals(-140, gameScreen.getCharacterView().getLayoutY());

        // Move the character to the right
        gameScreen.moveCharacter(10);

        // Check if the character has moved to the expected position
        assertEquals(-890, gameScreen.getCharacterView().getLayoutX());
        assertEquals(-140, gameScreen.getCharacterView().getLayoutY());
    }

    @Test
    public void testStickCreation() {
        // Create a new GameScreen instance
        GameScreen gameScreen = new GameScreen(new Stage(), "/character.png");

        // Initially, there should be no stick
        assertNull(gameScreen.getOneImageView());

        // Create a stick
        gameScreen.createStick(200, 500, 20);

        // Check if the stick has been created
        assertNotNull(gameScreen.getOneImageView());
        assertEquals(200 - 10 / 2, gameScreen.getOneImageView().getLayoutX());
        assertEquals(500 - 20, gameScreen.getOneImageView().getLayoutY());
    }


}