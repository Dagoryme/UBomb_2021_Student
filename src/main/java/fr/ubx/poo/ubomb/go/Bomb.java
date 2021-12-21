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

    public void checkStatus(long now){
        time = (now-this.init) / 1000000;
        if (time > 4000){           //explose la bombe au bout de 4 secondes
            if (hasExploded == false){
                //boom();
                setModified(true);
            }
        }
    }

    public long getTime(){
        return this.time;
    }

    public String toString() {
        return "Bomb";
    }
}
