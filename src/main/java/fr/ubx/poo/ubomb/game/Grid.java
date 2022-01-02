package fr.ubx.poo.ubomb.game;

import fr.ubx.poo.ubomb.go.character.Monster;
import fr.ubx.poo.ubomb.go.decor.Bomb;
import fr.ubx.poo.ubomb.go.decor.Decor;
import fr.ubx.poo.ubomb.game.Position;
import fr.ubx.poo.ubomb.go.decor.Door;
import javafx.geometry.Pos;
import static fr.ubx.poo.ubomb.game.EntityCode.DoorPrevOpened;

import java.util.*;

public class Grid {

    private final int width;
    private final int height;
    private List<Position> PosMonster = new ArrayList<>();
    private Position nextPosPlayer;
    private final Map<Position, Decor> elements;
    private List <Bomb> bombs = new ArrayList<>();
    private List <Monster> monsters = new ArrayList<>();


    public Grid(int width, int height) {
        this.width = width;
        this.height = height;
        this.elements = new Hashtable<>();
    }
    //ajoute la position d'un monstre a la liste
    public void addPosMonster(Position pos){
        PosMonster.add(pos);
    }

    //retourne la liste de position
    public List<Position> getPosMonster() {
        return PosMonster;
    }

    public void cleanPosMonster(){
        PosMonster.clear();
    }

    public int getWidth() {
        return width;
    }

    public List<Bomb> getBombs(){
        return this.bombs;
    }

    public List<Monster> getMonster(){
        return this.monsters;
    }

    public int getHeight() {
        return height;
    }

    public void setNextPosPlayer(Position nextPosPlayer) {
        this.nextPosPlayer = nextPosPlayer;
    }

    public Position getNextPosPlayer(boolean isPrev) {
        if (isPrev) {
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    Position position = new Position(i, j);
                    if (get(position) instanceof Door){
                        return position;
                    }
                }
            }
        }
        return nextPosPlayer;
    }

    public Decor get(Position position) {
        return elements.get(position);
    }

    public void set(Position position, Decor decor) {
        if (decor != null)
            elements.put(position, decor);
    }

    public void remove(Position position) {
        elements.remove(position);
    }

    public Collection<Decor> values() {
        return elements.values();
    }

}
