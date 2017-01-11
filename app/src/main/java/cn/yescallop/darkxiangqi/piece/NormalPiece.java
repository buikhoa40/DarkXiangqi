package cn.yescallop.darkxiangqi.piece;

import cn.yescallop.darkxiangqi.Side;
import cn.yescallop.darkxiangqi.math.Position;

public abstract class NormalPiece extends Piece {

    protected NormalPiece(Side side, boolean turned) {
        super(side, turned);
    }

    @Override
    public boolean reachable(Position pos) {
        return super.reachable(pos) && pos.isNextTo(this);
    }
}
