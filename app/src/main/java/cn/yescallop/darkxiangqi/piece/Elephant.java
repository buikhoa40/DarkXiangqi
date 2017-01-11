package cn.yescallop.darkxiangqi.piece;

import cn.yescallop.darkxiangqi.Side;
import cn.yescallop.darkxiangqi.math.Position;

public class Elephant extends NormalPiece {

    public Elephant(Side side) {
        this(side, false);
    }

    public Elephant(Side side, boolean turned) {
        super(side, turned);
    }

    @Override
    public int getId() {
        return ELEPHANT;
    }

    @Override
    public String getName() {
        return "elephant";
    }

    @Override
    public boolean reachable(Position pos) {
        Piece piece = board.getPiece(pos);
        return super.reachable(pos) && (piece == null || (
                piece.getId() != ADVISOR &&
                        piece.getId() != GENERAL));
    }
}
