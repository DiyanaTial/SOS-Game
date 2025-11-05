package sprint2;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GameLogicTest {
    // ✅ Test 1: Valid move should update the board
    @Test
    public void testMoves(){
        GameLogic logic = new GameLogic(3, "Simple Game");
        assertTrue(logic.makeMove(0,0, 'S'));
        assertEquals('S', logic.getCell(0, 0));
    }

    // ✅ Test 2: Cannot overwrite an occupied cell
    @Test
    public void testCannotOverwriteCell() {
        GameLogic logic = new GameLogic(3, "Simple Game");
        logic.makeMove(0, 0, 'S');
        assertFalse(logic.makeMove(0, 0, 'O'));
    }

    // ✅ Test 3 (optional extra): Turn should switch after each valid move
    @Test
    public void testTurnSwitch() {
        GameLogic logic = new GameLogic(3, "Simple");
        logic.makeMove(0, 0, 'S');
        assertFalse(logic.isBlueTurn());
    }
}
