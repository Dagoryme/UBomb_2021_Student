package fr.ubx.poo.ubomb.go.decor;

import fr.ubx.poo.ubomb.game.Direction;
import fr.ubx.poo.ubomb.game.Game;
import fr.ubx.poo.ubomb.game.Position;
import fr.ubx.poo.ubomb.game.Direction;
import fr.ubx.poo.ubomb.game.Grid;
import fr.ubx.poo.ubomb.go.character.Player;

public class Bomb extends Decor{

    private long init_time;
    private int state;
    private boolean hasExploded;
    private boolean explosionEnded;
    private long hasExplodedtime;
    private boolean isExplosionDone;
    private boolean isExplosionSprite;


    public Bomb(Game game,Position position, long now){
        super(game,position);
        this.init_time = now;
        this.hasExplodedtime=0;
        this.explosionEnded=false;
    }

    public boolean isWalkable(Player player) {
        return true;
    }

    public void checkStatus(long now) {
            if (now - init_time >= 1000 && hasExplodedtime==0) {
                this.state = 3;
            }
            if (now - init_time >= 2000 && hasExplodedtime==0) {
                this.state = 2;
            }
            if (now - init_time >= 3000 && hasExplodedtime==0) {
                this.state = 1;
            }
            if (now - init_time >= 4000 && hasExplodedtime==0) {
                this.state = 0;
                this.hasExplodedtime = System.currentTimeMillis();
                this.hasExploded = true;
            }
            setModified(true);
            if (now - hasExplodedtime >=1000 && this.state==0){
                this.explosionEnded=true;
                this.remove();
                if (isExplosionSprite==false){
                    game.getPlayer().setBombs(game.getPlayer().getBombs()+1);
                }
                setModified(false);
                }
    }

    public int getState(){
        return this.state;

    }

    public void setState(int state) {
        this.state = state;
    }

    public long getInit_time() {
        return init_time;
    }

    public void setisExplosionSprite(boolean b){
        this.isExplosionSprite=b;
    }

    public boolean getisExplosionSprite(){
        return this.isExplosionSprite;
    }

    public boolean getIsExplosionDone() {
        return isExplosionDone;
    }

    public void setExplosionDone(boolean IsExplosionDone) {
        this.isExplosionDone = IsExplosionDone;
    }

    public boolean gethasExploded(){
        return this.hasExploded;
    }

    public boolean getexplosionEnded(){
        return this.explosionEnded;
    }

    public String toString() {
        return "Bomb";
    }

    public boolean isBreakable(){return false;}

    @Override
    public Position getPosition() {
        return super.getPosition();
    }
}
