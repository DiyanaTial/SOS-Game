package sprint3;

import java.util.List;

public class GeneralMode extends GameLogic {
    private int blueScore = 0;
    private int greenScore = 0;

    public GeneralMode(int size){
        super(size, "General");
    }

    @Override
    public boolean makeMove(int row, int col, char letter){
        if (!inRange(row, col) || board[row][col] != '\0') return false;

        board[row][col] = letter;

        // detect all sos segments formed by this move
        lastSOSes = detectAllSOS(row, col);
        int made = lastSOSes.size();

        if (made > 0){
            if (isBlueTurn) blueScore += made;
            else greenScore += made;
            // General rules same player continues
            // do not switch
        } else {
            turnSwitch();
        }

        // end if full
        if (boardFull()) {
            if (blueScore > greenScore) winner = "Blue";
            else if (greenScore > blueScore) winner = "Green";
            else winner = "Draw";
        }
        return true;
    }

    @Override
    public boolean gameOver(){
        return winner != null;
    }

    public int getBlueScore() { return blueScore; }
    public int getGreenScore() { return greenScore; }
}
