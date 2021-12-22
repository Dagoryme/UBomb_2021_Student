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
        int i = (int) bomb.getTime();
        Image image = getImage(i);
        setImage(image);
    }

    public Image getImage(int i) {
        return ImageResource.getBomb(i);
    }
}

