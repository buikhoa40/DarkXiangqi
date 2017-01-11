package cn.yescallop.darkxiangqi.piece;

import cn.yescallop.darkxiangqi.Side;
import cn.yescallop.darkxiangqi.math.Position;

public class Horse extends NormalPiece {

    public Horse(Side side) {
        this(side, false);
    }

    public Horse(Side side, boolean turned) {
        super(side, turned);
    }

    @Override
    public int getId() {
        return HORSE;
    }

    @Override
    public String getName() {
        return "horse";
    }

    @Override
    public boolean reachable(Position pos) {
        Piece piece = board.getPiece(pos);
        return super.reachable(pos) && (piece == null || (
                piece.getId() == HORSE ||
                        piece.getId() == CANNON ||
                        piece.getId() == SOLDIER));
    }
}
