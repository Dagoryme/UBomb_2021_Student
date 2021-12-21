/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.ubomb.go.decor;
import fr.ubx.poo.ubomb.game.Position;
import fr.ubx.poo.ubomb.go.character.Player;

public class Tree extends Decor {
    public Tree(Position position) {
        super(position);
    }

    public boolean isWalkable(Player player) {
        return false;
    }

}
