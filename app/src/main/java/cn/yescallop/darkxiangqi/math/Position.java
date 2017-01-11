package cn.yescallop.darkxiangqi.math;

public class Position {

    public int x;
    public int y;

    public Position() {
        this(0, 0);
    }

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Position add(int x, int y) {
        return new Position(this.x + x, this.y + y);
    }

    public boolean isNextTo(Position pos) {
        return (Math.abs(pos.x - x) == 1 && Math.abs(pos.y - y) == 0) || (Math.abs(pos.x - x) == 0 && Math.abs(pos.y - y) == 1);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Position && ((Position) obj).x == x && ((Position) obj).y == y;
    }
}
