package sprint4;

public abstract class Player {
    protected final String name;
    protected final boolean isBlue;

    public Player(String name, boolean isBlue){
        this.name = name;
        this.isBlue = isBlue;
    }

    public String getName(){
        return name;
    }

    public boolean isBlue(){
        return isBlue;
    }

    /**
     * TODO:
     *  decide what move to make given current game state
     *  for human player, GUI-driven so return null
     *  for computer player, will pick a random row/col/letter
     **/

    /** used by computer player */
    public Move chooseMove(GameLogic game) {
        return null;
    }
}
