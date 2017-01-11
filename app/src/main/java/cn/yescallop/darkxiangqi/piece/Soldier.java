package cn.yescallop.darkxiangqi.piece;

import cn.yescallop.darkxiangqi.Side;
import cn.yescallop.darkxiangqi.math.Position;

public class Soldier extends NormalPiece {

    public Soldier(Side side) {
        this(side, false);
    }

    public Soldier(Side side, boolean turned) {
        super(side, turned);
    }

    @Override
    public int getId() {
        return SOLDIER;
    }

    @Override
    public String getName() {
        return "soldier";
    }

    @Override
    public boolean reachable(Position pos) {
        Piece piece = board.getPiece(pos);
        return super.reachable(pos) && (piece == null || (
                piece.getId() == GENERAL ||
                        piece.getId() == SOLDIER));
    }
}
