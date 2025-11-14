package sprint4;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ComputerPlayer extends Player {
    private final Random rnd = new Random();

    public ComputerPlayer(String name, boolean isBlue){
        super(name, isBlue);
    }

    @Override
    public Move chooseMove(GameLogic game){
        int size = game.size;

        List<int[]> emptyCells = new ArrayList<>();

        // finding all empty cells
        for (int r = 0; r < size; r++){
            for (int c = 0; c < size; c++){
                if (game.getCell(r,c) == '\0'){
                    emptyCells.add(new int[]{r, c});
                }
            }
        }

        if (emptyCells.isEmpty()){
            return null; // no move possible
        }

        // pick random empty cell
        int[] chosen = emptyCells.get(rnd.nextInt(emptyCells.size()));
        int row = chosen[0];
        int col = chosen[1];

        // pick letter (random for now)
        char letter = rnd.nextBoolean() ? 'S' : 'O';

        return new Move(row, col, letter);
    }
}
