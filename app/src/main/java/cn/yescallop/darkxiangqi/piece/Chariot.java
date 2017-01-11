package cn.yescallop.darkxiangqi.piece;

import cn.yescallop.darkxiangqi.Side;
import cn.yescallop.darkxiangqi.math.Position;

public class Chariot extends NormalPiece {

    public Chariot(Side side) {
        this(side, false);
    }

    public Chariot(Side side, boolean turned) {
        super(side, turned);
    }

    @Override
    public int getId() {
        return CHARIOT;
    }

    @Override
    public String getName() {
        return "chariot";
    }

    @Override
    public boolean reachable(Position pos) {
        Piece piece = board.getPiece(pos);
        return super.reachable(pos) && (piece == null || (
                piece.getId() != ELEPHANT &&
                        piece.getId() != ADVISOR &&
                        piece.getId() != GENERAL));
    }
}
