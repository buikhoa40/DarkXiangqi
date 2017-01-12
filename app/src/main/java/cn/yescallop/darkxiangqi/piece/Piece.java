package cn.yescallop.darkxiangqi.piece;

import cn.yescallop.darkxiangqi.Board;
import cn.yescallop.darkxiangqi.Side;
import cn.yescallop.darkxiangqi.math.Position;
import cn.yescallop.darkxiangqi.network.Client;
import cn.yescallop.darkxiangqi.network.packet.MovePiecePacket;
import cn.yescallop.darkxiangqi.network.packet.TurnPiecePacket;

public abstract class Piece extends Position {

    public static final int GENERAL = 0;
    public static final int ADVISOR = 1;
    public static final int ELEPHANT = 2;
    public static final int HORSE = 3;
    public static final int CHARIOT = 4;
    public static final int CANNON = 5;
    public static final int SOLDIER = 6;

    private final Side side;
    protected Board board;
    private boolean turned;

    protected Piece(Side side, boolean turned) {
        this.side = side;
        this.turned = turned;
        this.board = Board.getInstance();
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
        this.turn(true);
    }

    public void turn(boolean own) {
        turned = true;
        if (board.isStarted()) {
            board.changeSide();
        } else {
            board.setSide(this.side.getAgainstSide());
        }
        board.unselect();
        if (own) {
            TurnPiecePacket packet = new TurnPiecePacket();
            packet.x = this.x;
            packet.y = this.y;
            Client.getInstance().sendPacket(packet);
        }
    }

    public void select() {
        this.select(true);
    }

    public void select(boolean own) {
        board.select(this, own);
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
        return this.move(x, y, true);
    }

    public boolean move(int x, int y, boolean own) {
        return this.move(new Position(x, y), own);
    }

    public boolean move(Position pos) {
        return this.move(pos, true);
    }

    public boolean move(Position pos, boolean own) {
        if (own && !this.reachable(pos)) {
            return false;
        }
        board.setPiece(this, null);
        Piece piece = board.getPiece(pos);
        if (own) {
            MovePiecePacket packet = new MovePiecePacket();
            packet.fromX = this.x;
            packet.fromY = this.y;
            packet.toX = pos.x;
            packet.toY = pos.y;
            Client.getInstance().sendPacket(packet);
        }
        if (piece != null && piece.getId() == this.getId()) {
            board.setPiece(pos, null);
        } else {
            board.setPiece(pos, this);
        }
        board.changeSide();
        return true;
    }
}
