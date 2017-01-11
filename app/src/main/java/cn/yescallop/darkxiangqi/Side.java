package cn.yescallop.darkxiangqi;

public enum Side {
    Black, Red;

    public String getName() {
        return this == Black ? "black" : "red";
    }

    public Side getAgainstSide() {
        return this == Black ? Red : Black;
    }
}
