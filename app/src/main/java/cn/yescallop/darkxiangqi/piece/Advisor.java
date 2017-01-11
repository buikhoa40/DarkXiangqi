package cn.yescallop.darkxiangqi.piece;

import cn.yescallop.darkxiangqi.Side;
import cn.yescallop.darkxiangqi.math.Position;

public class Advisor extends NormalPiece {

    public Advisor(Side side) {
        this(side, false);
    }

    public Advisor(Side side, boolean turned) {
        super(side, turned);
    }

    @Override
    public int getId() {
        return ADVISOR;
    }

    @Override
    public String getName() {
        return "advisor";
    }

    @Override
    public boolean reachable(Position pos) {
        Piece piece = board.getPiece(pos);
        return super.reachable(pos) && (piece == null ||
                piece.getId() != GENERAL);
    }
}
