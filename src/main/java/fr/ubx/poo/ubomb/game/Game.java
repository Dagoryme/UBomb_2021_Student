/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.ubomb.game;


import fr.ubx.poo.ubomb.go.GameObject;
import fr.ubx.poo.ubomb.go.character.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

public class Game {

    public final int bombBagCapacity;
    public final int monsterVelocity;
    public int playerLives;
    public int levels;
    public final long playerInvisibilityTime;
    public final long monsterInvisibilityTime;
    public final String worldPath;
    private Grid grid;
    private Player player;
    private List<Grid> grids = new LinkedList<>();
    public boolean GridChange;

    public Game(String worldPath) {
        try (InputStream input = new FileInputStream(new File(worldPath, "config.properties"))) {
            this.worldPath= worldPath;
            Properties prop = new Properties();
            // load the configuration file
            prop.load(input);
            bombBagCapacity = Integer.parseInt(prop.getProperty("bombBagCapacity", "3"));
            monsterVelocity = Integer.parseInt(prop.getProperty("monsterVelocity", "10"));
            levels = Integer.parseInt(prop.getProperty("levels", "1"));
            playerLives = Integer.parseInt(prop.getProperty("playerLives", "3"));
            playerInvisibilityTime = Long.parseLong(prop.getProperty("playerInvisibilityTime", "4000"));
            monsterInvisibilityTime = Long.parseLong(prop.getProperty("monsterInvisibilityTime", "1000"));

            // Load the world
            String prefix = prop.getProperty("prefix");
            GridRepo gridRepo = new GridRepoSample(this);
            Grid grid = gridRepo.load(1, prefix + 1);
            this.grid = grid;
            grids.add(grid);
            // Create the player
            String[] tokens = prop.getProperty("player").split("[ :x]+");
            if (tokens.length != 2)
                throw new RuntimeException("Invalid configuration format");
            Position playerPosition = new Position(Integer.parseInt(tokens[0]), Integer.parseInt(tokens[1]));
            player = new Player(this, playerPosition, playerLives);

        } catch (IOException ex) {
            System.err.println("Error loading configuration");
            throw new RuntimeException("Invalid configuration format");
        }
    }

    public Grid getGrid() {
        return grid;
    }

    public int getLevels() {
        return this.levels;
    }

    public void setLevels(int levels){
        this.levels=levels;
    }

    public String getWorldPath() {
        return worldPath;
    }

    public void loadNext(){
        if (this.GridChange==false){
            this.levels=this.levels+1;
            System.out.println("levels fait");
            GridRepoFile fromfile= new GridRepoFile(this.getPlayer().game);
            System.out.println("File fait");
            Grid nextgrid = fromfile.load(levels,this.worldPath);
            this.grid=nextgrid;
            player.setPosition(grid.getNextPosPlayer());
            grids.add(nextgrid);
            this.GridChange=true;
        }
    }

    public void loadPrev(String path){
        this.grid = grids.get(levels-2);
    }



    // Returns the player, monsters and bombs at a given position
    public List<GameObject> getGameObjects(Position position) {
        List<GameObject> gos = new LinkedList<>();
        if (getPlayer().getPosition().equals(position))
            gos.add(player);
        return gos;
    }

    public Player getPlayer() {
        return this.player;
    }

    public boolean inside(Position position) {
        return true;
    }

}
