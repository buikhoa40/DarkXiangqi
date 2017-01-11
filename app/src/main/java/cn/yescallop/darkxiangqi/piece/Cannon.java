package cn.yescallop.darkxiangqi.piece;

import cn.yescallop.darkxiangqi.Side;
import cn.yescallop.darkxiangqi.math.Position;

public class Cannon extends Piece {

    public Cannon(Side side) {
        this(side, false);
    }

    public Cannon(Side side, boolean turned) {
        super(side, turned);
    }

    @Override
    public int getId() {
        return CANNON;
    }

    @Override
    public String getName() {
        return "cannon";
    }

    @Override
    public boolean reachable(Position pos) {
        Piece piece = board.getPiece(pos);
        if (!super.reachable(pos)) {
            return false;
        }
        if (piece == null && pos.isNextTo(this)) {
            return true;
        }
        boolean one = false;
        if (pos.x == x) {
            if (pos.y > y) {
                for (int i = y + 1; i < pos.y; i++) {
                    if (board.getPiece(x, i) != null) {
                        if (one) {
                            return false;
                        } else {
                            one = true;
                        }
                    }
                }
            } else {
                for (int i = pos.y + 1; i < y; i++) {
                    if (board.getPiece(x, i) != null) {
                        if (one) {
                            return false;
                        } else {
                            one = true;
                        }
                    }
                }
            }
        } else if (pos.y == y) {
            if (pos.x > x) {
                for (int i = x + 1; i < pos.x; i++) {
                    if (board.getPiece(i, y) != null) {
                        if (one) {
                            return false;
                        } else {
                            one = true;
                        }
                    }
                }
            } else {
                for (int i = pos.x + 1; i < x; i++) {
                    if (board.getPiece(i, y) != null) {
                        if (one) {
                            return false;
                        } else {
                            one = true;
                        }
                    }
                }
            }
        } else {
            return false;
        }
        return one;
    }
}
