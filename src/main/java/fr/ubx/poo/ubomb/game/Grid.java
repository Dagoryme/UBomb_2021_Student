package fr.ubx.poo.ubomb.game;

import fr.ubx.poo.ubomb.go.decor.Decor;
import fr.ubx.poo.ubomb.game.Position;
import javafx.geometry.Pos;

import java.util.*;

public class Grid {

    private final int width;
    private final int height;
    private List<Position> PosMonster = new ArrayList<>();
    private Position nextPosPlayer;
    private final Map<Position, Decor> elements;

    public Grid(int width, int height) {
        this.width = width;
        this.height = height;
        this.elements = new Hashtable<>();
    }

    public void addPosMonster(Position posmonster){
        PosMonster.add(posmonster);
    }

    public List<Position> getPosMonster() {
        return this.PosMonster;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void setNextPosPlayer(Position nextPosPlayer) {
        this.nextPosPlayer = nextPosPlayer;
    }

    public Position getNextPosPlayer() {
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
