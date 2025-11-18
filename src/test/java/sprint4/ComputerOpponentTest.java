package sprint4;

import org.junit.Test;

import static org.junit.Assert.*;

public class ComputerOpponentTest {

    // AC 8.1  Computer Player Selected as Player
    @Test
    public void createComputerAndHumanPlayers() {
        Player blue = new ComputerPlayer("Blue Computer", true);
        Player green = new HumanPlayer("Green Human", false);

        assertTrue("Blue player should be a ComputerPlayer",
                blue instanceof ComputerPlayer);
        assertTrue("Green player should be a HumanPlayer",
                green instanceof HumanPlayer);
    }

    // AC 8.2  Computer Makes a Valid Move
    @Test
    public void computerMakesValidMoveOnNonFullBoard() {
        GameLogic game = new SimpleMode(3);
        ComputerPlayer cpu = new ComputerPlayer("CPU", true);

        game.board[0][0] = 'S';
        game.board[1][1] = 'O';

        Move m = cpu.chooseMove(game);

        assertNotNull("Computer should choose a move when board is not full", m);

        // Row/col must be in range and cell empty *before* applying move
        assertTrue("Row must be in bounds", m.row >= 0 && m.row < 3);
        assertTrue("Col must be in bounds", m.col >= 0 && m.col < 3);
        assertEquals("Chosen cell must be empty before move",
                '\0', game.board[m.row][m.col]);

        // Letter must be S or O
        assertTrue("Computer must choose either 'S' or 'O'",
                m.letter == 'S' || m.letter == 'O');

        // After applying move, the board must update correctly
        boolean accepted = game.makeMove(m.row, m.col, m.letter);
        assertTrue("GameLogic should accept computer's move", accepted);
        assertEquals("Board cell should contain chosen letter",
                m.letter, game.board[m.row][m.col]);
    }

    // AC 8.3  Computer Plays Simple / General to the End
    @Test
    public void computerCanFinishSimpleGame() {
        GameLogic game = new SimpleMode(3);
        ComputerPlayer blue = new ComputerPlayer("Blue CPU", true);
        ComputerPlayer green = new ComputerPlayer("Green CPU", false);

        playFullComputerVsComputerGame(game, blue, green);

        assertTrue("Simple game should eventually end", game.gameOver());
        assertNotNull("Winner (or Draw) should be set", game.getWinner());
    }

    @Test
    public void computerCanFinishGeneralGame() {
        GameLogic game = new GeneralMode(3);
        ComputerPlayer blue = new ComputerPlayer("Blue CPU", true);
        ComputerPlayer green = new ComputerPlayer("Green CPU", false);

        playFullComputerVsComputerGame(game, blue, green);

        assertTrue("General game should eventually end", game.gameOver());
        assertNotNull("Winner (or Draw) should be set", game.getWinner());
    }

    // AC 8.4  Computer Follows Turn Order

    @Test
    public void turnOrderAlternatesBetweenPlayers() {
        GameLogic game = new SimpleMode(3);
        ComputerPlayer blue = new ComputerPlayer("Blue CPU", true);
        ComputerPlayer green = new ComputerPlayer("Green CPU", false);

        // First move: should be blue
        assertTrue("Blue should start", game.isBlueTurn());
        Move m1 = blue.chooseMove(game);
        boolean ok1 = game.makeMove(m1.row, m1.col, m1.letter);
        assertTrue(ok1);

        // After blue's move in SimpleMode (no SOS on empty game), turn should flip
        assertFalse("Turn should switch to green after blue's move when no SOS",
                game.isBlueTurn());

        // Second move: green
        Move m2 = green.chooseMove(game);
        boolean ok2 = game.makeMove(m2.row, m2.col, m2.letter);
        assertTrue(ok2);

        // Now it should be blue's turn again (unless game already ended)
        if (!game.gameOver()) {
            assertTrue("Turn should switch back to blue after green's move",
                    game.isBlueTurn());
        }
    }

    // AC 8.5  Computer Chooses Random Valid Move

    @Test
    public void computerDoesNotAlwaysPickSameCell() {
        GameLogic game = new SimpleMode(3);
        ComputerPlayer cpu = new ComputerPlayer("CPU", true);

        int firstRow = -1, firstCol = -1;
        boolean sawDifferent = false;

        // Call chooseMove many times on a fresh (or nearly fresh) board
        for (int i = 0; i < 30; i++) {
            Move m = cpu.chooseMove(game);
            assertNotNull(m);

            if (i == 0) {
                firstRow = m.row;
                firstCol = m.col;
            } else if (m.row != firstRow || m.col != firstCol) {
                sawDifferent = true;
                break;
            }
        }

        assertTrue("Computer should not always pick the exact same cell; " +
                "choices should vary over time", sawDifferent);
    }

    // AC 9.1  Automatic Continuous Play
    @Test
    public void computerVsComputerPlaysToEnd_SimpleMode() {
        GameLogic game = new SimpleMode(3);
        ComputerPlayer blue = new ComputerPlayer("Blue CPU", true);
        ComputerPlayer green = new ComputerPlayer("Green CPU", false);

        playFullComputerVsComputerGame(game, blue, green);

        assertTrue("Computer vs Computer simple game must reach gameOver()", game.gameOver());
    }

    // AC 9.2  computer vs computer Works in Both Modes

    @Test
    public void computerVsComputerPlaysToEnd_GeneralMode() {
        GameLogic game = new GeneralMode(3);
        ComputerPlayer blue = new ComputerPlayer("Blue CPU", true);
        ComputerPlayer green = new ComputerPlayer("Green CPU", false);

        playFullComputerVsComputerGame(game, blue, green);

        assertTrue("Computer vs Computer general game must reach gameOver()", game.gameOver());
        assertNotNull("Winner or Draw should be decided", game.getWinner());
    }

    // Helper used by several ACs: play a full computer game using GameLogic

    private void playFullComputerVsComputerGame(GameLogic game,
                                                ComputerPlayer blue,
                                                ComputerPlayer green) {
        int safetyLimit = game.size * game.size + 10; // avoid infinite loop

        int moves = 0;
        while (!game.gameOver() && moves < safetyLimit) {
            ComputerPlayer current =
                    game.isBlueTurn() ? blue : green;

            Move move = current.chooseMove(game);
            assertNotNull("Computer should always return a move while game not over", move);

            boolean accepted = game.makeMove(move.row, move.col, move.letter);
            assertTrue("Each chosen move must be accepted as valid", accepted);

            moves++;
        }

        assertTrue("Game should end in a finite number of moves", game.gameOver());
    }
}