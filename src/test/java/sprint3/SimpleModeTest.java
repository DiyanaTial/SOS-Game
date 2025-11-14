package sprint3;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SimpleModeTest {

    private void playExpectTrue(GameLogic g, int r, int c, char ch) {
        assertTrue(g.makeMove(r, c, ch), "Expected move to succeed at ("+r+","+c+")");
    }

    @Test
    void startsWithBlueAndEmptyBoard() {
        SimpleMode g = new SimpleMode(3);
        assertTrue(g.isBlueTurn(), "Blue should start");
        for (int r = 0; r < 3; r++)
            for (int c = 0; c < 3; c++)
                assertEquals('\0', g.getCell(r, c));
        assertFalse(g.gameOver());
    }

    @Test
    void invalidMoveRejected_andTurnUnchanged() {
        SimpleMode g = new SimpleMode(3);
        playExpectTrue(g, 0, 0, 'S');         // Blue
        assertFalse(g.makeMove(0, 0, 'O'));   // Green tries to overwrite
        // Turn should still be Green (since the invalid move should not toggle)
        assertFalse(g.isBlueTurn());
        assertEquals('S', g.getCell(0, 0));
        assertFalse(g.gameOver());
    }

    @Test
    void blueWinsByRow_SOS() {
        SimpleMode g = new SimpleMode(3);
        // Row 0: S O S with Blue scoring on last move
        playExpectTrue(g, 0, 0, 'S'); // Blue
        playExpectTrue(g, 1, 1, 'S'); // Green (irrelevant filler)
        playExpectTrue(g, 0, 1, 'O'); // Blue
        playExpectTrue(g, 2, 2, 'S'); // Green (irrelevant filler)
        playExpectTrue(g, 0, 2, 'S'); // Blue completes S-O-S

        assertTrue(g.gameOver());
        assertEquals("Blue", g.getWinner());
        assertFalse(g.getLastSOSes().isEmpty(), "Should record the winning SOS segment");
    }

    @Test
    // TODO: Fix this (should be green)
    void blueWinsByDiagonal_withLastMoveS() {
        SimpleMode g = new SimpleMode(3);
        // Build diag: place O at (1,1), S at (2,2), then S at (0,0) to complete S-O-S
        playExpectTrue(g, 1, 1, 'O'); // Blue
        playExpectTrue(g, 0, 2, 'S'); // Green filler
        playExpectTrue(g, 2, 2, 'S'); // Blue
        playExpectTrue(g, 2, 0, 'S'); // Green filler
        playExpectTrue(g, 0, 0, 'S'); // Blue completes diag S(0,0)-O(1,1)-S(2,2)

        assertTrue(g.gameOver());
        assertEquals("Green", g.getWinner());
    }

    @Test
    void simpleDraw_noSOSAnywhere() {
        SimpleMode g = new SimpleMode(3);
        // Fill with O everywhere; never forms SOS
        playExpectTrue(g, 0,0,'O'); // B
        playExpectTrue(g, 0,1,'O'); // G
        playExpectTrue(g, 0,2,'O'); // B
        playExpectTrue(g, 1,0,'O'); // G
        playExpectTrue(g, 1,1,'O'); // B
        playExpectTrue(g, 1,2,'O'); // G
        playExpectTrue(g, 2,0,'O'); // B
        playExpectTrue(g, 2,1,'O'); // G
        playExpectTrue(g, 2,2,'O'); // B

        assertTrue(g.gameOver());
        assertEquals("Draw", g.getWinner());
    }

    @Test
    void turnAlternatesWhenNoWin() {
        SimpleMode g = new SimpleMode(3);
        playExpectTrue(g, 0,0,'S');             // Blue -> Green
        assertFalse(g.isBlueTurn());
        playExpectTrue(g, 0,1,'S');             // Green -> Blue
        assertTrue(g.isBlueTurn());
        assertFalse(g.gameOver());
    }
}

