/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.ubomb.engine;

import fr.ubx.poo.ubomb.game.Direction;
import fr.ubx.poo.ubomb.game.Game;
import fr.ubx.poo.ubomb.game.Grid;
import fr.ubx.poo.ubomb.game.Position;
import fr.ubx.poo.ubomb.go.character.Monster;
import fr.ubx.poo.ubomb.go.character.Player;
import fr.ubx.poo.ubomb.go.decor.*;
import fr.ubx.poo.ubomb.view.*;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.util.*;


public final class GameEngine {

    private static AnimationTimer gameLoop;
    private final String windowTitle;
    private final Game game;
    private final Player player;
    private final List<Sprite> sprites = new LinkedList<>();
    private final Set<Sprite> cleanUpSprites = new HashSet<>();
    private final Stage stage;
    private StatusBar statusBar;
    private Pane layer;
    private Input input;
    private List <Bomb> bombs = new LinkedList<>();


    public GameEngine(final String windowTitle, Game game, final Stage stage) {
        this.stage = stage;
        this.windowTitle = windowTitle;
        this.game = game;
        this.player = game.getPlayer();
        initialize();
        buildAndSetGameLoop();
    }

    private void initialize() {
        Group root = new Group();
        layer = new Pane();

        int height = game.getGrid().getHeight();
        int width = game.getGrid().getWidth();
        int sceneWidth = width * Sprite.size;
        int sceneHeight = height * Sprite.size;
        Scene scene = new Scene(root, sceneWidth, sceneHeight + StatusBar.height);
        scene.getStylesheets().add(getClass().getResource("/css/application.css").toExternalForm());

        stage.setTitle(windowTitle);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.sizeToScene();
        stage.show();

        input = new Input(scene);
        root.getChildren().add(layer);
        statusBar = new StatusBar(root, sceneWidth, sceneHeight, game);

        // Create sprites
        for (Decor decor : game.getGrid().values()) {
            if (decor instanceof Door){
                sprites.add(new SpriteDoor(layer,decor));
                decor.setModified(true);
            } else {
                sprites.add(SpriteFactory.create(layer, decor));
                decor.setModified(true);
            }
        }
        sprites.add(new SpritePlayer(layer, player));
    }

    void buildAndSetGameLoop() {
        gameLoop = new AnimationTimer() {
            public void handle(long now) {
                // Check keyboard actions
                processInput(now);

                // Do actions
                update(now);
                createNewBombs(now);
                checkCollision(now);
                checkExplosions();

                // Graphic update
                cleanupSprites();
                render();
                statusBar.update(game);
            }
        };
    }

    private void checkExplosions() {
        for (int i=0;i<bombs.size();i++){
            bombs.get(i).checkStatus(System.currentTimeMillis());
            if (bombs.get(i).gethasExploded() && bombs.get(i).getisExplosionSprite()==false && bombs.get(i).getIsExplosionDone()==false){
                bombs.get(i).setExplosionDone(true);
                Grid grid = game.getGrid();
                int range = game.getPlayer().getBombrange();
                Decor decor;
                for (int j=0;j<4;j++){
                    Direction direction = Direction.values()[j];
                    Position nextPos = direction.nextPosition(bombs.get(i).getPosition());
                    for (int k = 0; k<range;k++){
                        decor = grid.get(nextPos);
                        if (decor!=null){
                            if (decor.isBreakable()==false){
                                break;
                            }
                            else if (decor.isBreakable()){
                                Bomb bomb = new Bomb(game,nextPos,bombs.get(i).getInit_time());
                                bombs.add(bomb);
                                bomb.setisExplosionSprite(true);
                                sprites.add(new SpriteBomb(layer,bomb));
                                decor.remove();
                                decor.setModified(true);
                                break;
                            }
                        }
                        if (decor==null && player.isInMap(nextPos)){
                            Bomb bomb = new Bomb(game,nextPos,bombs.get(i).getInit_time());
                            bombs.add(bomb);
                            bomb.setisExplosionSprite(true);
                            sprites.add(new SpriteBomb(layer,bomb));
                        }
                        if (nextPos.getX() == player.getPosition().getX() &&nextPos.getY() == player.getPosition().getY()){
                            player.getHit(System.currentTimeMillis());
                        }
                        nextPos=direction.nextPosition(nextPos);
                    }
                }
            }
            if (bombs.get(i).gethasExploded() && bombs.get(i).getexplosionEnded()){
                Grid grid = game.getGrid();
                grid.remove(bombs.get(i).getPosition());
                bombs.remove(i);
            }
        }
    }

    private void createNewBombs(long now) {
    }

    private void checkCollision(long now) {
    }

    private void processInput(long now) {
        if (input.isExit()) {
            gameLoop.stop();
            Platform.exit();
            System.exit(0);
        } else if (input.isMoveDown()) {
            player.requestMove(Direction.DOWN);
        } else if (input.isMoveLeft()) {
            player.requestMove(Direction.LEFT);
        } else if (input.isMoveRight()) {
            player.requestMove(Direction.RIGHT);
        } else if (input.isMoveUp()) {
            player.requestMove(Direction.UP);
        } else if (input.isKey()){
            player.openDoor();
        } else if (input.isBomb()){
            if (player.getBombs()>=1){
                Bomb bomb = new Bomb(game,player.getPosition(),System.currentTimeMillis());
                bomb.setisExplosionSprite(false);
                bomb.setState(3);
                sprites.add(new SpriteBomb(layer,bomb));
                bombs.add(bomb);
                player.dropBomb(bomb);
            }
        }
        input.clear();
    }

    private void showMessage(String msg, Color color) {
        Text waitingForKey = new Text(msg);
        waitingForKey.setTextAlignment(TextAlignment.CENTER);
        waitingForKey.setFont(new Font(60));
        waitingForKey.setFill(color);
        StackPane root = new StackPane();
        root.getChildren().add(waitingForKey);
        Scene scene = new Scene(root, 400, 200, Color.WHITE);
        stage.setTitle(windowTitle);
        stage.setScene(scene);
        input = new Input(scene);
        stage.show();
        new AnimationTimer() {
            public void handle(long now) {
                processInput(now);
            }
        }.start();
    }


    private void update(long now) {
        player.update(now);

        if (player.getLives() == 0) {
            gameLoop.stop();
            showMessage("Perdu!", Color.RED);
        }

        if (player.isWinner()) {
            gameLoop.stop();
            showMessage("Gagné", Color.BLUE);
        }
    }

    public void cleanupSprites() {
        sprites.forEach(sprite -> {
            if (sprite.getGameObject().isDeleted()) {
                game.getGrid().remove(sprite.getPosition());
                cleanUpSprites.add(sprite);
            }
        });
        cleanUpSprites.forEach(Sprite::remove);
        sprites.removeAll(cleanUpSprites);
        cleanUpSprites.clear();
    }

    private void render() {
        sprites.forEach(Sprite::render);
    }

    public void start() {
        gameLoop.start();
    }
}
