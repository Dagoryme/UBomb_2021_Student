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
    //charge le niveau voulu
    public Grid load (int level,String worldPath){

        int r;
        int x = 0;
        int y = 0;
        int lines = 0;
        int nb = 0;
        int columns = 0;

        File file;
        FileReader fr;
        BufferedReader br;

        try{
            file = new File(worldPath, "level" + level + ".txt");
            fr = new FileReader(file);

            br = new BufferedReader(fr);
            //nombre de lignes du fichier txt
            while (br.readLine() != null){
                lines++;
            }
            br.close();
            fr.close();
            fr = new FileReader(file);
            //nombre de caractères du fichier
            while(true){
                r = fr.read();
                nb++;
                if (r==-1){
                    break;
                }
            }
            //calcule les colonnes
            columns = nb/lines - 1;
            fr.close();
        }
        catch(FileNotFoundException e){
            System.err.println("File not found " + e);
        }
        catch(IOException e){
            System.err.println("File error " + e);
        }
        EntityCode[][] tab = new EntityCode[lines][columns];
        // création du tableau pour le niveau
        try{

            file = new File(worldPath, "level" + level + ".txt");
            fr = new FileReader(file);
            while (x < lines){
                while (y<columns){
                    r = fr.read();
                    if(r != -1 && r!=10){
                        //remplissage du tableau
                        tab[x][y] = EntityCode.fromCode((char)r);
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
        Grid grid = new Grid(columns, lines);
        for (int i = 0; i < columns; i++) {
            for (int j = 0; j < lines; j++) {
                Position position = new Position(i, j);
                EntityCode entityCode = tab[j][i];
                //placement des monstre dans la liste
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
