package fr.ubx.poo.ubomb.view;

import fr.ubx.poo.ubomb.go.Bomb;
import fr.ubx.poo.ubomb.go.decor.Door;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;

public class SpriteDoor extends Sprite{
    public SpriteDoor(Pane layer, Door door) {
        super(layer, null, door);
    }

    public void updateImage() {
        Door door = (Door) getGameObject();
        Image image = getImage(door);
        setImage(image);
    }

    public Image getImage(Door door) {
        return ImageResource.getDoor(door.getIsOpened());
    }
}
