package fr.ubx.poo.ubomb.go;
import fr.ubx.poo.ubomb.game.Game;
import fr.ubx.poo.ubomb.game.Position;
import fr.ubx.poo.ubomb.go.GameObject;
import fr.ubx.poo.ubomb.go.character.Player;

public class Bomb extends GameObject{
    private long init;
    private long time;
    private boolean hasExploded;


    public Bomb(Game game, Position position, long now){
        super(game, position);
        this.init = now;
        setModified(true);
    }

    public boolean isWalkable(Player player) {
        return true;
    }

    public long getTime(){
        return this.time;
    }

    public String toString() {
        return "Bomb";
    }
}
