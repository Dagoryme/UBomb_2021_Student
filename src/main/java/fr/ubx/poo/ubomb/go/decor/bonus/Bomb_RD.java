package fr.ubx.poo.ubomb.go.decor.bonus;
import fr.ubx.poo.ubomb.game.Game;
import fr.ubx.poo.ubomb.game.Position;
import fr.ubx.poo.ubomb.go.character.Player;

public class Bomb_RD extends Bonus {

    public Bomb_RD(Position position) {
        super(position);
    }

    @Override
    public boolean isWalkable(Player player) {
        return true;
    }

    public boolean isBreakable(){return true;}

    public void takenBy(Player player) { //applique l'effet du bonus
        if (player.getBombrange()>1){
            player.setBombRange(player.getBombrange()-1);
            remove();
        }
        else {
            remove();
        }
    }
}
