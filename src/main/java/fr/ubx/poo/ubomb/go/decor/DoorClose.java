package fr.ubx.poo.ubomb.go.decor;

import fr.ubx.poo.ubomb.game.Position;
import fr.ubx.poo.ubomb.go.character.Player;

public class DoorClose extends Decor  {

        private boolean isPrevious = false;
        private boolean isOpened = false;

        public DoorClose(Position position) {
            super(position);
        }


        @Override
        public String toString() {
            return "DoorClose";
        }

        public boolean canBeOpened(){
            return true;
        }

        public boolean isBreakable(){
            return false;
        }

        public void setIsOpened(boolean bool){
            this.isOpened = bool;
        }

        public boolean getIsOpened(){
            if (isPrevious){
                return true;
            }
            return this.isOpened;
        }

/*    public boolean getEffect(Game game){
        if (isPrevious){
            game.setLevel(game.getLevel()-1);
            game.loadPreviousLevel(game.getWorldPath(), game.getLevel());
        }
        else if (isOpened){
            game.setLevel(game.getLevel()+1);
            game.loadNextLevel(game.getWorldPath(), game.getLevel());
        }
        return false;

    }
 */

        public boolean isWalkable(Player player) {
            if (this.isOpened == true){
                return true;
            }
            return false;
        }

    }

