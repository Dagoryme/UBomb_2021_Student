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


public class Player extends GameObject implements Movable {

    private Direction direction;
    private boolean moveRequested = false;
    private int lives;
    private int keys;
    private int bombs;
    private int bombrange;


    public Player(Game game, Position position, int lives) {
        super(game, position);
        this.direction = Direction.DOWN;
        this.lives = lives;
        this.keys = 0;
        this.bombs = 0;
        this.bombrange = 1;
    }

    public int getLives() {
        return this.lives;
    }

    public int getKeys() {
        return this.keys;
    }

    public int getBombs() {
        return this.bombs;
    }

    public int getBombrange(){
        return this.bombrange;
    }

    public void setLives(int lives){
        this.lives = lives;
    }

    public void setKeys(int keys){
        this.keys = keys;
    }

    public void setBombs(int nb){
        this.bombs = nb;
    }

    public void setBombRange(int bombrange){
        this.bombrange = bombrange;
    }

    public Direction getDirection() {
        return direction;
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
        if (decor instanceof Box){
            Position box = direction.nextPosition(nextPos);
            if(isInMap(box) && isEmpty(box)){
                decor.setPosition(box);
                grid.remove(nextPos);
                grid.set(box,decor);
                return true;
            }
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

    public boolean isEmpty(Position pos){
        Grid grid = game.getGrid();
        Decor decor = grid.get(pos);
        if(decor == null){
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
        Grid grid = game.getGrid();
        Decor decor = grid.get(nextPos);

        if (decor instanceof Monster){
            this.lives=this.lives-1;
        }
        Player player = this.game.getPlayer();
        if (decor instanceof Bonus){
            ((Bonus) decor).takenBy(player);
        }
        setPosition(nextPos);
    }

    @Override
    public boolean isWalkable(Player player) {
        return false;
    }

    @Override
    public void explode() {
    }

    // Example of methods to define by the player



    public void openDoor(){
        Direction direction = getDirection();
        Position nextPos = direction.nextPosition(getPosition());
        Grid grid =game.getGrid();
        Decor decor = grid.get(nextPos);
        if (decor != null ){
            if (keys>0 && decor instanceof Door){
                keys=keys -1;
                ((Door) decor).setIsOpened(true);
                ((Door) decor).setModified(true);
            }
        }

    }

    public void dropBomb(Bomb bomb){
        if (bombs>=1){
            bombs=bombs-1;
            Grid grid =game.getGrid();
            grid.set(getPosition(),bomb);
        }
    }

    public void takeDoor(int gotoLevel) {}

    public void takeKey() {}



    public boolean isWinner() {
        Position pos = getPosition();
        Grid grid = game.getGrid();
        Decor decor = grid.get(pos);
        if (decor instanceof Princess ){
            return true;
        }
        return false;
    }
}
