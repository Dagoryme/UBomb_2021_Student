package fr.ubx.poo.ubomb.go.decor;

import fr.ubx.poo.ubomb.game.Direction;
import fr.ubx.poo.ubomb.game.Game;
import fr.ubx.poo.ubomb.game.Position;
import fr.ubx.poo.ubomb.go.Movable;
import fr.ubx.poo.ubomb.go.character.Player;

public class Box extends Decor {

    public Box(Position position) {
        super(position);
    }

    @Override
    public String toString() {
        return "Box";
    }

    public boolean isWalkable(Player player) {
        return false;
    }

    public boolean isBreakable(){return true;}

}
