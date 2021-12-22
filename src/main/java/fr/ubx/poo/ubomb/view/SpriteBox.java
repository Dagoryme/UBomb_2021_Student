package fr.ubx.poo.ubomb.view;

import fr.ubx.poo.ubomb.go.decor.Door;
import fr.ubx.poo.ubomb.go.decor.*;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;

public class SpriteBox extends Sprite{
    public SpriteBox(Pane layer, Decor decor) {
        super(layer, null, decor);
        updateImage();
    }
    @Override
    public void updateImage() {
        Box box = (Box) getGameObject();
        Image image = getImage();
        setImage(image);
    }

    public Image getImage() {
        return ImageResource.getBox();
    }

}
