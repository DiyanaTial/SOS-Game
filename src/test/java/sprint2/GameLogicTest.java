package sprint2;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GameLogicTest {
    @Test
    public void testMoves(){
        GameLogic logic = new GameLogic(3, "Simple Game");
        assertTrue(logic.makeMove(0,0, 'S'));
        assertEquals('S', logic.getCell(0, 0));
    }

    @Test
    public void testCannotOverwriteCell() {
        GameLogic logic = new GameLogic(3, "Simple Game");
        logic.makeMove(0, 0, 'S');
        assertFalse(logic.makeMove(0, 0, 'O'));
    }

    @Test
    public void testTurnSwitch() {
        GameLogic logic = new GameLogic(3, "Simple");
        logic.makeMove(0, 0, 'S');
        assertFalse(logic.isBlueTurn());
    }
}
