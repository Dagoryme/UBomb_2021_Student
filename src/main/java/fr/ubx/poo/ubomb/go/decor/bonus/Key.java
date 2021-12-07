/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.ubomb.go.decor.bonus;
import fr.ubx.poo.ubomb.game.Game;
import fr.ubx.poo.ubomb.game.Position;
import fr.ubx.poo.ubomb.go.character.Player;
import fr.ubx.poo.ubomb.go.decor.Decor;

public class Key extends Bonus {
    public Key(Position position) {
        super(position);
    }

    @Override
    public boolean isWalkable(Player player) {
        return true;
    }

    public boolean addbonus(Game game){
        game.getPlayer().setKeys(game.getPlayer().getKeys()+1);
        return true;
    }

    public void takenBy(Player player) {
        player.takeKey();
    }
}
