package sprint3;

import java.util.List;

public class SimpleMode extends GameLogic {

    public SimpleMode(int size) {
        super(size, "Simple");
    }

    @Override
    public boolean makeMove (int row, int col, char letter){
        if (!inRange(row, col) || board[row][col] != '\0') return false;

        // capture current player BEFORE state changes
        String currentPlayer = getTurnText();

        board[row][col] = letter;

        // find all SOS
        lastSOSes = detectAllSOS(row, col);

        if (!lastSOSes.isEmpty()){
            // current player wins immediately
            winner = currentPlayer;
        } else if (boardFull()) {
            winner = "Draw";
        } else {
            turnSwitch();
        }
        return true;
    }

    @Override
    public boolean gameOver(){
        return winner != null;
    }
}
