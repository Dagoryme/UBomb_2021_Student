package fr.ubx.poo.ubomb.go.decor.bonus;
import fr.ubx.poo.ubomb.game.Game;
import fr.ubx.poo.ubomb.game.Position;
import fr.ubx.poo.ubomb.go.character.Player;
import fr.ubx.poo.ubomb.go.decor.Decor;

public class Bomb_ND extends Bonus {

    public Bomb_ND(Position position) {
        super(position);
    }

    @Override
    public boolean isWalkable(Player player) {
        return true;
    }


    public void takenBy(Player player) {
        if (player.getBombs()>1){
            player.setBombs(player.getBombs()-1);
            remove();
        }
    }
}
