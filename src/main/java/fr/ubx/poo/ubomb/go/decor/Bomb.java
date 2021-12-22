package fr.ubx.poo.ubomb.go.decor;

import fr.ubx.poo.ubomb.game.Game;
import fr.ubx.poo.ubomb.game.Position;
import fr.ubx.poo.ubomb.go.character.Player;

public class Bomb extends Decor{

    private long init_time;
    private int state;
    private boolean hasExploded;


    public Bomb(Position position, long now){
        super(position);
        this.init_time = now;
    }

    public boolean isWalkable(Player player) {
        return true;
    }

    public void checkStatus(long now) {
            if (now - init_time >= 1000) {
                this.state = 3;
            }

            if (now - init_time >= 2000) {
                this.state = 2;
            }
            if (now - init_time >= 3000) {
                this.state = 1;
            }
            setModified(true);
            if (now - init_time >= 4000) {
                this.state = 0;
                this.hasExploded=true;
            }
            if (now - init_time >=4500){
                remove();
                setModified(false);
            }

        }

    public int getState(){
        return this.state;

    }

    public void setState(int state) {
        this.state = state;
    }

    public boolean gethasExploded(){
        return this.hasExploded;
    }

    public String toString() {
        return "Bomb";
    }

}
