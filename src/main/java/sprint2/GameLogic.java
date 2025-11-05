package sprint2;

public class GameLogic {
    private char[][] board;
    private int size;
    private boolean isBlueTurn;  // true = Blue, false = Green
    private String gameMode;     // "Simple" or "General"

    public GameLogic(int size, String mode) {
        this.size = size;
        this.gameMode = mode;
        this.board = new char[size][size];
        this.isBlueTurn = true; // Blue starts
    }

    public boolean makeMove(int row, int col, char letter) {
        if (row < 0 || col < 0 || row >= size || col >= size) return false;
        if (board[row][col] != '\0') return false; // occupied

        board[row][col] = letter;
        isBlueTurn = !isBlueTurn;
        return true;
    }

    public char getCell(int row, int col) {
        return board[row][col];
    }

    public boolean isBlueTurn() {
        return isBlueTurn;
    }

    public String getTurnText() {
        return isBlueTurn ? "Blue" : "Green";
    }
}