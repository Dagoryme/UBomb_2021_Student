package fr.ubx.poo.ubomb.go.decor.bonus;
import fr.ubx.poo.ubomb.game.Game;
import fr.ubx.poo.ubomb.game.Position;
import fr.ubx.poo.ubomb.go.character.Player;

public class Heart extends Bonus {

    public Heart(Position position) {
        super(position);
    }

    @Override
    public boolean isWalkable(Player player) {
        return true;
    }

    public boolean addbonus(Game game){
        game.getPlayer().setLives(game.getPlayer().getLives()+1);
        return true;
    }

    public void takenBy(Player player) {
        player.takeKey();
    }
}
