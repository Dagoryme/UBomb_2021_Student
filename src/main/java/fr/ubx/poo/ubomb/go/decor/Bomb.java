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


    public Bomb(Game game,Position position, long now){
        super(game,position);
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
                this.state=5;
                remove();
                setModified(false);
            }
    }

    public void explosion(){
        Grid grid = game.getGrid();
        int range = game.getPlayer().getBombrange();
        Decor decor;
        for (int i=0;i<4;i++){
            Direction direction = Direction.values()[i];
            Position nextPos = direction.nextPosition(getPosition());
            for (int j = 0; j<range;j++){
                decor = grid.get(nextPos);
                if (decor!=null){
                    if (decor.isBreakable()==false){
                        break;
                    }
                    else if (decor.isBreakable()){
                        decor.remove();
                    }
                }
                if (nextPos == game.getPlayer().getPosition()){
                    game.getPlayer().setLives(game.getPlayer().getLives()-1);
                }
            }


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

    public boolean isBreakable(){return false;}

    @Override
    public Position getPosition() {
        return super.getPosition();
    }
}
