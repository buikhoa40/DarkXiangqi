package cn.yescallop.darkxiangqi.piece;

import cn.yescallop.darkxiangqi.Board;
import cn.yescallop.darkxiangqi.Side;
import cn.yescallop.darkxiangqi.math.Position;

public abstract class Piece extends Position {

    public static final int GENERAL = 0;
    public static final int ADVISOR = 1;
    public static final int ELEPHANT = 2;
    public static final int HORSE = 3;
    public static final int CHARIOT = 4;
    public static final int CANNON = 5;
    public static final int SOLDIER = 6;

    private final Side side;
    public Board board;
    private boolean turned;

    protected Piece(Side side, boolean turned) {
        this.side = side;
        this.turned = turned;
    }

    public static Piece get(int id, Side side) {
        return get(id, side, false);
    }

    public static Piece get(int id, Side side, boolean turned) {
        return get(id, side, turned, null);
    }

    public static Piece get(int id, Side side, boolean turned, Position pos) {
        Piece piece;
        switch (id) {
            case GENERAL:
                piece = new General(side);
                break;
            case ADVISOR:
                piece = new Advisor(side);
                break;
            case ELEPHANT:
                piece = new Elephant(side);
                break;
            case HORSE:
                piece = new Horse(side);
                break;
            case CHARIOT:
                piece = new Chariot(side);
                break;
            case CANNON:
                piece = new Cannon(side);
                break;
            case SOLDIER:
                piece = new Soldier(side);
                break;
            default:
                return null;
        }
        piece.turned = turned;
        if (pos != null) {
            piece.x = pos.x;
            piece.y = pos.y;
        }
        return piece;
    }

    public abstract int getId();

    public abstract String getName();

    public String getResourceName() {
        return this.getName() + "_" + side.getName();
    }

    public Side getSide() {
        return side;
    }

    public boolean isTurned() {
        return turned;
    }

    public void turn() {
        turned = true;
        if (board.isStarted()) {
            board.changeSide();
        } else {
            board.setSide(this.side.getAgainstSide());
        }
        board.unselect();
    }

    public void select() {
        board.select(this);
    }

    public boolean isSelected() {
        return this == board.getSelectedPiece();
    }

    public boolean reachable(Position pos) {
        if (!this.turned) {
            return false;
        }
        Piece piece = board.getPiece(pos);
        return piece == null || piece.getSide() != this.getSide();
    }

    public boolean move(int x, int y) {
        return this.move(new Position(x, y));
    }

    public boolean move(Position pos) {
        if (!this.reachable(pos)) {
            return false;
        }
        board.setPiece(this, null);
        Piece piece = board.getPiece(pos);
        if (piece != null && piece.getId() == this.getId()) {
            board.setPiece(pos, null);
        } else {
            board.setPiece(pos, this);
        }
        board.changeSide();
        return true;
    }
}
