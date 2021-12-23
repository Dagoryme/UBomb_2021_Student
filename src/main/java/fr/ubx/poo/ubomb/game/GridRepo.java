package fr.ubx.poo.ubomb.game;

import fr.ubx.poo.ubomb.go.decor.*;
import fr.ubx.poo.ubomb.go.decor.bonus.*;
import fr.ubx.poo.ubomb.go.decor.bonus.Key;


public abstract class GridRepo {

    private final Game game;

    GridRepo(Game game) {
        this.game = game;
    }

    public Game getGame() {
        return game;
    }

    public abstract Grid load(int level, String name);

    Decor processEntityCode(EntityCode entityCode, Position pos) {
        switch (entityCode) {
            case Empty:
                return null;
            case BombNumberDec:
                return new Bomb_ND(pos);
            case BombNumberInc:
                return new Bomb_NI(pos);
            case BombRangeDec:
                return new Bomb_RD(pos);
            case BombRangeInc:
                return new Bomb_RI(pos);
            case Heart:
                return new Heart(pos);
            case Key:
                return new Key(pos);
            case Princess:
                return new Princess(pos);
            case DoorNextClosed:
                return new Door(pos);
            case DoorNextOpened:
                return new Door(pos);
            case Box:
                return new Box(pos);
            case Stone:
                return new Stone(pos);
            case Tree:
                return new Tree(pos);

            default:
                return null;
                // throw new RuntimeException("EntityCode " + entityCode.name() + " not processed");
        }
    }
}
