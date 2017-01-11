package cn.yescallop.darkxiangqi.piece;

import cn.yescallop.darkxiangqi.Side;
import cn.yescallop.darkxiangqi.math.Position;

public class General extends NormalPiece {

    public General(Side side) {
        this(side, false);
    }

    public General(Side side, boolean turned) {
        super(side, turned);
    }

    @Override
    public int getId() {
        return GENERAL;
    }

    @Override
    public String getName() {
        return "general";
    }

    @Override
    public boolean reachable(Position pos) {
        Piece piece = board.getPiece(pos);
        return super.reachable(pos) && (piece == null ||
                piece.getId() != SOLDIER);
    }
}
