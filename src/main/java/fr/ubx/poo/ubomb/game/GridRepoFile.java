package fr.ubx.poo.ubomb.game;

import java.io.*;
import java.lang.reflect.Field;
import java.util.Optional;

public class GridRepoFile {

    public EntityCode[][] load(int level, String path){

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
        EntityCode[][] grid = new EntityCode[lines][columns];        //crée une tableau de WorldEntity avec lines lignes et columns colonnes

        try{
            file = new File(worldPath, "level" + level + ".txt");
            fr = new FileReader(file);
            while (x < lines){
                while (y<columns){
                    r = fr.read();
                    if(r != -1){
                        Optional<EntityCode> entity = WorldEntity.fromCode((char)r);   //transforme ce qui a été lu en caractère
                        if (entity.isPresent()){                                        //si entity n'est pas vide
                            grid[x][y] = entity.get();                             //remplie le tableau avec entity
                            y ++;
                        }
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
