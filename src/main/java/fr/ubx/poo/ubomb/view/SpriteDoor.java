package fr.ubx.poo.ubomb.view;

import fr.ubx.poo.ubomb.go.Bomb;
import fr.ubx.poo.ubomb.go.decor.Door;
import fr.ubx.poo.ubomb.go.decor.*;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;

public class SpriteDoor extends Sprite{
    public SpriteDoor(Pane layer, Decor decor) {
        super(layer, null, decor);
        updateImage();
    }

    @Override
    public void updateImage() {
        Door door = (Door) getGameObject();
        boolean b = door.getIsOpened();
        Image image = getImage(b);
        setImage(image);
    }

    public Image getImage(boolean b) {
        return ImageResource.getDoor(b);
    }

}
