package fr.ubx.poo.ubomb.view;

import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import fr.ubx.poo.ubomb.go.decor.*;


public class SpriteBomb extends Sprite {
    public SpriteBomb(Pane layer, Decor decor) {
        super(layer, null, decor);
    }

    public void updateImage() {
        Bomb bomb = (Bomb) getGameObject();
        bomb.checkStatus(System.currentTimeMillis());
        boolean b = bomb.gethasExploded();
        System.out.println(bomb.getState());
        if (b){
            Image image = ImageResource.getExplosion();
            setImage(image);
        }else {
            Image image = getImage(bomb.getState());
            setImage(image);
        }
    }

    public Image getImage(int i) {
        return ImageResource.getBomb(i);
    }
}

