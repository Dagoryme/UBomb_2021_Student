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

    public boolean addbonus(Game game){
        if (game.getPlayer().getBombs()>1){
            game.getPlayer().setBombs(game.getPlayer().getBombs()-1);
        }
        return true;
    }

    public void takenBy(Player player) {
        player.takeKey();
    }
}
