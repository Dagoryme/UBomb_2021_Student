package fr.ubx.poo.ubomb.game;

import java.io.*;
import java.lang.reflect.Field;
import java.util.Optional;
import fr.ubx.poo.ubomb.game.Game;

import javax.swing.text.html.parser.Entity;
import static fr.ubx.poo.ubomb.game.EntityCode.Monster;
import static fr.ubx.poo.ubomb.game.EntityCode.DoorPrevOpened;

public class GridRepoFile extends GridRepo {

    public GridRepoFile(Game game){
        super(game);
    }

    public Grid load (int level,String worldPath){

        int r;
        int x = 0;
        int y = 0;

        int lines = 0;
        int characters = 0;
        int columns = 0;

        File file;
        FileReader fr;
        BufferedReader br;

        try{
            file = new File(worldPath, "level" + level + ".txt");
            fr = new FileReader(file);

            br = new BufferedReader(fr);
            while (br.readLine() != null){      //compte le nombre de lignes du fichier
                lines++;
            }
            br.close();
            fr.close();
            fr = new FileReader(file);
            while(true){
                r = fr.read();
                characters++;               //compte le nombre de caractères du fichier
                if (r==-1){
                    break;
                }
            }
            columns = characters/lines - 1;     //calcule le nombre de colonnes du fichier
            fr.close();
        }
        catch(FileNotFoundException e){
            System.err.println("File not found " + e);
        }
        catch(IOException e){
            System.err.println("File error " + e);
        }
        EntityCode[][] tab = new EntityCode[lines][columns];        //crée une tableau de WorldEntity avec lines lignes et columns colonnes

        try{

            file = new File(worldPath, "level" + level + ".txt");
            fr = new FileReader(file);
            while (x < lines){
                while (y<columns-5){
                    r = fr.read();
                    if(r != -1 && r!=10){
                        System.out.println(r);
                        System.out.println((char)r);
                        tab[x][y] = EntityCode.fromCode((char)r);     // remplie le tableau avec entity
                        y ++;
                    }
                }
                x ++;
                y = 0;
            }
            fr.close();
        }
        catch(FileNotFoundException e){
            System.err.println("File not found " + e);
        }
        catch(IOException e){
            System.err.println("File error " + e);
        }
        Grid grid = new Grid(lines, columns);
        for (int i = 0; i < lines; i++) {
            for (int j = 0; j < columns; j++) {
                Position position = new Position(i, j);
                EntityCode entityCode = tab[j][i];
                if (entityCode == Monster){
                    Position posmonster= new Position(i,j);
                    grid.addPosMonster(posmonster);
                }
                if (entityCode == DoorPrevOpened){
                    Position nextPosPlayer = new Position(i,j);
                    grid.setNextPosPlayer(nextPosPlayer);
                }
                grid.set(position, processEntityCode(entityCode, position));
            }
        }
        return grid;

    }


    private final EntityCode[][] getEntities(String name) {
        try {
            Field field = this.getClass().getDeclaredField(name);
            return (EntityCode[][]) field.get(this);
        } catch (IllegalAccessException e) {
            return null;
        } catch (NoSuchFieldException e) {
            return null;
        }
    }
}
