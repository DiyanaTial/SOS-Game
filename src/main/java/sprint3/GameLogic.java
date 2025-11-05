package sprint3;

import java.util.ArrayList;
import java.util.List;

public abstract class GameLogic {
    protected char[][] board;
    protected int size;
    protected boolean isBlueTurn;  // true = Blue, false = Green
    protected String winner;       // winner : blue green or draw
    protected String gameMode;

    // holds all SOS segments formed by the most recent move (each as 3 coords)
    protected List<int[][]> lastSOSes = new ArrayList<>();

    public GameLogic(int size, String gamemode) {
        this.size = size;
        this.board = new char[size][size];
        this.isBlueTurn = true; // Blue starts
        this.winner = null;
        this.gameMode = gamemode;
    }

    //abstract methods that will be implemented differently in SimpleMode and GeneralMode

    public abstract boolean makeMove(int row, int col, char letter);
    public abstract boolean gameOver();

    // utility
    // returns whatever char is stored at specific board position
    public char getCell(int row, int col) { return board[row][col]; }
    // used in GUI to know whose turn it is
    public boolean isBlueTurn() { return isBlueTurn; }
    public String getTurnText() { return isBlueTurn ? "Blue" : "Green"; }
    public String getWinner() { return winner; }

    // flips turn from Blue -> Green or Green -> Blue
    public void turnSwitch() { isBlueTurn = !isBlueTurn; }

    // quick helper that checks if a cell is inside the board
    protected boolean inRange(int row, int col){
        return row >= 0 && col >= 0 && row < size && col < size;
    }

    // checks if all board cells are filled
    protected boolean boardFull() {
        for (char[] row : board){
            for (char cell : row){
                if (cell == '\0') return false;
            }
        }
        return true;
    }

    // returns all SOS segments created from recent moves
    public List<int[][]> getLastSOSes() { return lastSOSes; }

   // resets the SOS list (starting new game)
    public void clearLastSOSes() { lastSOSes.clear(); }

    protected List<int[][]> detectAllSOS(int row, int col) {
        List<int[][]> found = new ArrayList<>();
        char placed = board[row][col];
        int[][] dirs = {{0,1},{1,0},{1,1},{1,-1}};

        for (int[] d : dirs) {
            int dr = d[0], dc = d[1];

            if (placed == 'O') {
                // Case 1 the letter placed was 'O' (middle of S-O-S)
                int r1 = row - dr, c1 = col - dc;
                int r2 = row + dr, c2 = col + dc;
                if (inRange(r1,c1) && inRange(r2,c2) &&
                        board[r1][c1] == 'S' && board[r2][c2] == 'S') {
                    found.add(new int[][]{{r1,c1},{row,col},{r2,c2}});
                }
            } else if (placed == 'S') {
                // Case 2 the letter placed was 'S'
                int r1 = row + dr, c1 = col + dc;
                int r2 = row + 2*dr, c2 = col + 2*dc;
                if (inRange(r1,c1) && inRange(r2,c2) &&
                        board[r1][c1] == 'O' && board[r2][c2] == 'S') {
                    found.add(new int[][]{{row,col},{r1,c1},{r2,c2}});
                }
                // backward: S, O, S <- this
                int rb1 = row - dr, cb1 = col - dc;
                int rb2 = row - 2*dr, cb2 = col - 2*dc;
                if (inRange(rb1,cb1) && inRange(rb2,cb2) &&
                        board[rb1][cb1] == 'O' && board[rb2][cb2] == 'S') {
                    found.add(new int[][]{{rb2,cb2},{rb1,cb1},{row,col}});
                }
            }
        }
        return found;
    }
}
