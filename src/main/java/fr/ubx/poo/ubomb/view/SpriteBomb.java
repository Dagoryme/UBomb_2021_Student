package fr.ubx.poo.ubomb.view;

import fr.ubx.poo.ubomb.game.Direction;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import fr.ubx.poo.ubomb.go.Bomb;


public class SpriteBomb extends Sprite {
    public SpriteBomb(Pane layer, Bomb bomb) {
        super(layer, null, bomb);
    }

    public void updateImage() {
        Bomb bomb = (Bomb) getGameObject();
        System.out.println("labomb");
        int i = (int) bomb.getTime();
        Image image = getImage(i);
        setImage(image);
    }

    public Image getImage(int i) {
        return ImageResource.getBomb(i);
    }
}

