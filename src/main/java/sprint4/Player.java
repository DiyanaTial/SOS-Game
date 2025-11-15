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
    /** used by computer player */
    public Move chooseMove(GameLogic game) {
        return null;
    }
}
