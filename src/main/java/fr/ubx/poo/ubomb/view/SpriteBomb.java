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
        Image image = getImage(bomb.getState(),b);
        setImage(image);
    }

    public Image getImage(int i,boolean b) {
        return ImageResource.getBomb(i,b);
    }
}

