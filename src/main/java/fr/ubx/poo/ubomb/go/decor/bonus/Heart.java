package fr.ubx.poo.ubomb.go.decor.bonus;
import fr.ubx.poo.ubomb.game.Game;
import fr.ubx.poo.ubomb.game.Position;
import fr.ubx.poo.ubomb.go.Takeable;
import fr.ubx.poo.ubomb.go.character.Player;

public class Heart extends Bonus implements Takeable {

    public Heart(Position position) {
        super(position);
    }

    @Override
    public boolean isWalkable(Player player) {
        return true;
    }

    public boolean isBreakable(){return true;}

    public void takenBy(Player player) {
        player.setLives(player.getLives()+1);
        remove();
    }
}
