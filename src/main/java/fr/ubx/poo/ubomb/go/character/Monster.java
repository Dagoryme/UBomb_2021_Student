/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.ubomb.go.character;
import fr.ubx.poo.ubomb.game.Grid;
import fr.ubx.poo.ubomb.game.Direction;
import fr.ubx.poo.ubomb.game.Game;
import fr.ubx.poo.ubomb.game.Position;
import fr.ubx.poo.ubomb.go.GameObject;
import fr.ubx.poo.ubomb.go.Movable;
import fr.ubx.poo.ubomb.go.decor.*;
import fr.ubx.poo.ubomb.go.decor.bonus.*;


public class Monster extends GameObject implements Movable {

    private Direction direction;
    private boolean moveRequested = false;
    private int lives;


    public Monster(Game game, Position position, int lives) {
        super(game, position);
        this.direction = Direction.DOWN;
        this.lives = 0;
    }

    public int getLives() {
        return this.lives;
    }

    public void setLives(int lives){
        this.lives = lives;
    }

    public Direction getDirection() {
        return Direction.random();
    }

    public void requestMove(Direction direction) {
        if (direction != this.direction) {
            this.direction = direction;
            setModified(true);
        }
        moveRequested = true;
    }

    public boolean canMove(Direction direction) {
        Position nextPos = direction.nextPosition(getPosition());
        Grid grid = game.getGrid();
        Decor decor = grid.get(nextPos);
        if (isInMap(nextPos) && isObject(nextPos)) {
            return true;
        }
        return false;
    }


    public boolean isInMap(Position pos){
        Grid grid=game.getGrid();
        if(pos.getY()>=grid.getHeight()){
            return false;
        }
        if (pos.getX()>=grid.getWidth()){
            return false;
        }
        if (pos.getX() < 0){
            return false;
        }
        if(pos.getY() <0){
            return false;
        }
        return true;
    }

    public boolean getHit(){
        this.lives=this.lives-1;
        if (this.lives <= 0){
            remove();
            setModified(true);
            return true;
        }
        return false;
    }


    public boolean isObject(Position pos){
        Grid grid = game.getGrid();
        Decor decor = grid.get(pos);
        if (decor == null){
            return true;
        }
        return decor.isWalkable(game.getPlayer());
    }


    public void update(long now) {
        if (moveRequested) {
            if (canMove(direction)) {
                doMove(direction);
            }
        }
        moveRequested = false;
    }

    public void doMove(Direction direction) {
        // Check if we need to pick something up
        Position nextPos = direction.nextPosition(getPosition());
        setPosition(nextPos);
    }

    @Override
    public boolean isWalkable(Player player) {
        return true;
    }
}
