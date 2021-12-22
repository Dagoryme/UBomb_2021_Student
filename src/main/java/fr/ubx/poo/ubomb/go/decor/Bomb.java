package fr.ubx.poo.ubomb.go.decor;

import fr.ubx.poo.ubomb.game.Game;
import fr.ubx.poo.ubomb.game.Position;
import fr.ubx.poo.ubomb.go.character.Player;

public class Bomb extends Decor{
    private long init;
    private long time;
    private boolean hasExploded;


    public Bomb(Game game, Position position, long now){
        super(position);
        this.init = now;
        setModified(true);
    }

    public boolean isWalkable(Player player) {
        return true;
    }

    public void checkStatus(long now){
        setModified(true);
        time = (now-this.init) / 1000000;
        System.out.println(time);
        if (time > 4000){           //explose la bombe au bout de 4 secondes
            if (hasExploded == false){
                //boom();
            }
        }
    }

    public long getTime(){
        return this.time;
    }

}
