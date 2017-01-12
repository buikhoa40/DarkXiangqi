package cn.yescallop.darkxiangqi;

import android.content.res.Resources;
import android.widget.ImageView;

import cn.yescallop.darkxiangqi.math.Position;
import cn.yescallop.darkxiangqi.network.Client;
import cn.yescallop.darkxiangqi.network.packet.SelectPiecePacket;
import cn.yescallop.darkxiangqi.piece.Piece;

public class Board {

    private static Board instance;
    private final Resources resources;
    private final ImageView view;
    private Piece[][] pieces = new Piece[8][4];
    private Side side = null;
    private boolean holding;
    private Piece selected = null;

    public Board(Resources resources, ImageView view) {
        instance = this;
        this.resources = resources;
        this.view = view;
        view.setOnTouchListener(new BoardOnTouchListener(this));
    }

    public static Board getInstance() {
        return instance;
    }

    public Piece getPiece(int x, int y) {
        return pieces[x][y];
    }

    public Piece getPiece(Position pos) {
        return pieces[pos.x][pos.y];
    }

    public void setPiece(int x, int y, Piece piece) {
        pieces[x][y] = piece;
        if (piece != null) {
            piece.x = x;
            piece.y = y;
        }
    }

    public void setPiece(Position pos, Piece piece) {
        pieces[pos.x][pos.y] = piece;
        if (piece != null) {
            piece.x = pos.x;
            piece.y = pos.y;
        }
    }

    public Piece[][] getPieces() {
        return pieces;
    }

    public Piece getSelectedPiece() {
        return selected;
    }

    public boolean isSelected() {
        return selected != null;
    }

    public void select(Piece piece) {
        this.select(piece, true);
    }

    public void select(Piece piece, boolean own) {
        if (selected == piece) return;
        selected = piece;
        if (!this.isStarted()) {
            this.setSide(own ? piece.getSide() : piece.getSide().getAgainstSide());
        }
        if (own) {
            SelectPiecePacket packet = new SelectPiecePacket();
            packet.x = piece.x;
            packet.y = piece.y;
            Client.getInstance().sendPacket(packet);
        }
    }

    public void unselect() {
        selected = null;
    }

    public boolean isStarted() {
        return side != null;
    }

    public void changeSide() {
        this.setHolding(!this.isHolding());
        this.unselect();
    }

    public Side getSide() {
        return side;
    }

    public void setSide(Side side) {
        this.side = side;
    }

    public void onTouch(int x, int y) {
        if (this.isHolding() && (this.isSelected() ? this.onProcess(x, y) : this.onSelect(x, y))) {
            this.updateImage();
        }
    }

    public boolean onSelect(int x, int y) {
        Piece piece = this.getPiece(x, y);
        if (piece != null && (!piece.isTurned() || piece.getSide() == this.getSide())) {
            piece.select();
        }
        return true;
    }

    public boolean onProcess(int x, int y) {
        Piece piece = this.getPiece(x, y);
        if (piece == this.selected && !piece.isTurned()) {
            piece.turn();
            return true;
        }
        if (piece != null && (!piece.isTurned() || piece.getSide() == this.getSide())) {
            piece.select();
            return true;
        }
        if (this.selected.move(x, y)) {
            return true;
        }
        return false;
    }

    public Resources getResources() {
        return resources;
    }

    public void updateImage() {
        view.invalidate();
    }

    public boolean isHolding() {
        return holding;
    }

    public void setHolding(boolean holding) {
        this.holding = holding;
    }

    public void startGame(boolean first, byte[][] data) {
        this.setHolding(first);
        this.setSide(null);
        int i = 0;
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 4; y++) {
                this.setPiece(x, y, Piece.get(data[i][0], data[i][1] == 0 ? Side.Black : Side.Red));
                i++;
            }
        }
        BoardActivity.getInstance().showSnackbar(first ? R.string.first_you : R.string.first_enemy);
    }
}
