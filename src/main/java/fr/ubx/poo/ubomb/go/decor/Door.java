package fr.ubx.poo.ubomb.go.decor;

import fr.ubx.poo.ubomb.game.Direction;
import fr.ubx.poo.ubomb.game.Grid;
import fr.ubx.poo.ubomb.game.Position;
import fr.ubx.poo.ubomb.go.character.Player;

public class Door extends Decor {

    private boolean isPrevious = false;
    private boolean isOpened = false;

    public Door(Position position) {
        super(position);
    }

    public boolean canBeOpened() {
        return true;
    }

    public boolean isBreakable() {
        return false;
    }

    public void setIsOpened(boolean bool) {
        this.isOpened = bool;
        setModified(true);
    }

    public boolean getIsOpened() {
        if (isPrevious) {
            return true;
        }
        return this.isOpened;
    }

    public boolean isWalkable(Player player) {
        if (this.isOpened == true) {
            return true;
        }
        return false;
    }

    public void WorldChange(Player player){
        System.out.println("erwan le suce bite");
    }

    public void OpenDoor(Player player){
        player.setKeys(player.getKeys()-1);
        this.setIsOpened(true);
        this.setModified(true);
    }

    @Override
    public String toString() {
        return "Door";
    }
}