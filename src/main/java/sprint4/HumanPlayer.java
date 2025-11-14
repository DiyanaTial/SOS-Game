package sprint4;

public class HumanPlayer extends Player{
    public HumanPlayer(String name, boolean isBlue){
        super(name, isBlue);
    }

    @Override
    public Move chooseMove(GameLogic game){
        // human move will come from button clicks in gui
        // so we don't actually generate move here
        return null;
    }
}
