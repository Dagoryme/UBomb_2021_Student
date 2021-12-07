package fr.ubx.poo.ubomb.go.decor;

import fr.ubx.poo.ubomb.game.Game;
import fr.ubx.poo.ubomb.game.Position;
import fr.ubx.poo.ubomb.go.character.Player;

public class Monster extends Decor{

    public Monster (Position position) {
        super(position);
    }

    public boolean isBreakable() {
        return true;
    }

    public boolean canBeOpened(){
        return false;
    }

    @Override
    public String toString() {
        return "Box";
    }

    public void setIsOpened(boolean bool){

    }

    public boolean getEffect(Game game){
        return false;
    }

    public boolean isWalkable(Player player) {
        return true;
    }
}
