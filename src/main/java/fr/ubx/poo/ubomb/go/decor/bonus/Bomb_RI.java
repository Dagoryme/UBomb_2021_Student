package fr.ubx.poo.ubomb.go.decor.bonus;
import fr.ubx.poo.ubomb.game.Game;
import fr.ubx.poo.ubomb.game.Position;
import fr.ubx.poo.ubomb.go.character.Player;

public class Bomb_RI extends Bonus {

    public Bomb_RI(Position position) {
        super(position);
    }

    @Override
    public boolean isWalkable(Player player) {
        return true;
    }

    public boolean addbonus(Game game){
        game.getPlayer().setBombRange(game.getPlayer().getBombrange()+1);
        return true;
    }

    public void takenBy(Player player) {
        player.takeKey();
    }
}
