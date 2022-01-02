/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.ubomb.engine;

import fr.ubx.poo.ubomb.game.Direction;
import fr.ubx.poo.ubomb.game.Game;
import fr.ubx.poo.ubomb.game.Grid;
import fr.ubx.poo.ubomb.game.Position;
import fr.ubx.poo.ubomb.go.GameObject;
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

import javax.sound.midi.Soundbank;
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
        for (Decor decor : game.getGrid().values()) {//initialise tous les sprites du niveau
            if (decor instanceof Door){
                sprites.add(new SpriteDoor(layer,decor));
                decor.setModified(true);
            } else {
                sprites.add(SpriteFactory.create(layer, decor));
                decor.setModified(true);
            }
        }
        for (int i=0;i<game.getGrid().getPosMonster().size();i++){
            Monster monster = new Monster(game,game.getGrid().getPosMonster().get(i),1);
            game.getGrid().getMonster().add(monster);
            sprites.add(new SpriteMonster(layer,monster));
        }
        game.getGrid().getPosMonster().clear();
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
                monstersMove();

                // Graphic update
                cleanupSprites();
                render();
                statusBar.update(game);
            }
        };
    }

    private void monstersMove(){
        for (int i = 0 ; i < game.getGrid().getMonster().size() ; i ++){
            if (System.currentTimeMillis()-game.getGrid().getMonster().get(i).getTimesincemove()>(10000/game.monsterVelocity)){ //Attend 1 seconde avant de refaire un move pour le monstre
                game.getGrid().getMonster().get(i).setTimesincemove(System.currentTimeMillis());
                game.getGrid().getMonster().get(i).requestMove(Direction.random());
                game.getGrid().getMonster().get(i).update(System.currentTimeMillis());
            }
        }
    }


    private void checkExplosions() {

        for (int i=0;i<game.getGrid().getBombs().size();i++){
            game.getGrid().getBombs().get(i).checkStatus(System.currentTimeMillis()); //verifie le status de la bombe
            if (game.getGrid().getBombs().get(i).gethasExploded() && game.getGrid().getBombs().get(i).getisExplosionSprite()==false && game.getGrid().getBombs().get(i).getIsExplosionDone()==false){
                game.getGrid().getBombs().get(i).setExplosionDone(true);
                Grid grid = game.getGrid();
                int range = game.getPlayer().getBombrange();
                Decor decor;
                if (game.getGrid().getBombs().get(i).getPosition()==player.getPosition()){ //verifie si  le joueur est touché par la bombe bombe initiale
                    player.getHit(System.currentTimeMillis());
                }
                for (int m=0;m<game.getGrid().getMonster().size();m++) { //verifie si un monstre est touché par la bombe bombe initiale
                    if (game.getGrid().getMonster().get(m).getPosition().getX() == game.getGrid().getBombs().get(i).getPosition().getX() && game.getGrid().getMonster().get(m).getPosition().getY() == game.getGrid().getBombs().get(i).getPosition().getY()) {
                        if (game.getGrid().getMonster().get(m).getHit()) {
                            game.getGrid().getMonster().remove(m);
                        }
                    }
                }
                for (int j=0;j<4;j++){ //Verifie les 4 direction possible pour l'explosion
                    Direction direction = Direction.values()[j];
                    Position nextPos = direction.nextPosition(game.getGrid().getBombs().get(i).getPosition());
                    for (int k = 0; k<range;k++){
                        decor = grid.get(nextPos);
                        if (decor!=null){
                            if (decor.isBreakable()==false){ //verifie si le decor est cassable ou non
                                break;
                            }
                            else if (decor.isBreakable()){ // si le décor est cassable, le detruit et crée un sprite d'explosion
                                Bomb bomb = new Bomb(game,nextPos,game.getGrid().getBombs().get(i).getInit_time());
                                game.getGrid().getBombs().add(bomb);
                                bomb.setisExplosionSprite(true);
                                sprites.add(new SpriteBomb(layer,bomb));
                                decor.remove();
                                decor.setModified(true);
                                break;
                            }
                        }
                        if (decor==null && player.isInMap(nextPos)){ //verifie si la bombe est dans la map
                            Bomb bomb = new Bomb(game,nextPos,game.getGrid().getBombs().get(i).getInit_time());
                            game.getGrid().getBombs().add(bomb);
                            bomb.setisExplosionSprite(true);
                            sprites.add(new SpriteBomb(layer,bomb)); // crée un sprite d'explosion de bombe
                        }
                        if (nextPos.getX() == player.getPosition().getX() &&nextPos.getY() == player.getPosition().getY()){ //verifie si  le joueur est touché par l'explosion de la bombe
                            player.getHit(System.currentTimeMillis());
                        }
                        for (int m=0;m<game.getGrid().getMonster().size();m++) { //verifie si  un monstre est touché par l'explosion de la bombe
                            if (game.getGrid().getMonster().get(m).getPosition().getX() == nextPos.getX() && game.getGrid().getMonster().get(m).getPosition().getY() == nextPos.getY()) {
                                if (game.getGrid().getMonster().get(m).getHit()) {
                                    game.getGrid().getMonster().remove(m);
                                    break;
                                }
                                break;
                            }
                        }
                        nextPos=direction.nextPosition(nextPos);
                    }
                }
            }
            if (game.getGrid().getBombs().get(i).gethasExploded() && game.getGrid().getBombs().get(i).getexplosionEnded()){ //supprime tous les sprites d'explosion
                Grid grid = game.getGrid();
                grid.remove(game.getGrid().getBombs().get(i).getPosition());
                game.getGrid().getBombs().remove(i);
            }
        }
    }

    private void createNewBombs(long now) {
    }

    private void checkCollision(long now) {
        for (int i=0;i<game.getGrid().getMonster().size();i++){
            if (player.getPosition().getX()==game.getGrid().getMonster().get(i).getPosition().getX() && player.getPosition().getY()==game.getGrid().getMonster().get(i).getPosition().getY()){ //verifie si le joueur est touché par un monstre
                player.getHit(System.currentTimeMillis());
            }
        }
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
                game.getGrid().getBombs().add(bomb);
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
        if (game.GridChange){ //vérifie si il y a eu un changement de monde
            sprites.forEach(Sprite::remove);
            int sceneWidth = (game.getGrid().getWidth()+1) * Sprite.size;
            stage.setWidth(sceneWidth);
            int sceneHeight = ((game.getGrid().getHeight()+1) * Sprite.size);
            stage.setHeight(sceneHeight + StatusBar.height); //modifie la taille de la fenetre
            statusBar.updateStatusBar((game.getGrid().getWidth()) * Sprite.size,(game.getGrid().getHeight()) * Sprite.size); // adapte la barre a la taille de la fenetre
            for (Decor decor : game.getGrid().values()) { //initialise tous les sprites du nouveau niveau
                if (decor instanceof Door){
                    sprites.add(new SpriteDoor(layer,decor));
                    decor.setModified(true);
                } else if (decor instanceof Bomb==false){
                    sprites.add(SpriteFactory.create(layer, decor));
                    decor.setModified(true);
                }
            }
            if (game.getGrid().isNew()) { //verifie si la grille viens d'être load ou si elle existait avant
                for (int i = 0; i < game.getGrid().getPosMonster().size(); i++) {
                    Monster monster = new Monster(game, game.getGrid().getPosMonster().get(i), 1); // crée les monstres si la grille est nouvelle
                    game.getGrid().getMonster().add(monster);
                    sprites.add(new SpriteMonster(layer, monster));
                }
            }else{
                for (int i=0; i<game.getGrid().getMonster().size();i++){
                    sprites.add(new SpriteMonster(layer,game.getGrid().getMonster().get(i))); //recrée les sprites des monstres si ils éxistent deja
                }
            }
            game.getGrid().getPosMonster().clear(); //Vide la liste des positions des monstre afin d'éviter les duplications
            sprites.add(new SpritePlayer(layer, player));
            render();
            for (int i=0;i<game.getGrid().getBombs().size();i++){
                sprites.add(new SpriteBomb(layer,game.getGrid().getBombs().get(i)));
            }
            if (game.getGrid().isNew()){
                game.getGrid().setNew(false);
            }
            game.GridChange=false;
        }
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
