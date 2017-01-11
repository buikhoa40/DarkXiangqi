package cn.yescallop.darkxiangqi;

import android.content.res.Resources;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cn.yescallop.darkxiangqi.math.Position;
import cn.yescallop.darkxiangqi.piece.Piece;

public class Board {

    private final Resources resources;
    private final ImageView view;
    private Piece[][] pieces = new Piece[8][4];
    private Side side = null;
    private Piece selected = null;

    public Board(Resources resources, ImageView view) {
        this.resources = resources;
        this.view = view;
        view.setOnTouchListener(new BoardOnTouchListener(this));
        this.reset();
    }

    public Piece getPiece(int x, int y) {
        return pieces[x][y];
    }

    public Piece getPiece(Position pos) {
        return pieces[pos.x][pos.y];
    }

    public void setPiece(Position pos, Piece piece) {
        pieces[pos.x][pos.y] = piece;
        if (piece != null) {
            piece.x = pos.x;
            piece.y = pos.y;
            piece.board = this;
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
        selected = piece;
    }

    public void unselect() {
        selected = null;
    }

    public boolean isStarted() {
        return side != null;
    }

    public void setSide(Side side) {
        this.side = side;
    }

    public void changeSide() {
        this.side = side.getAgainstSide();
        this.unselect();
    }

    public Side getSide() {
        return side;
    }

    public void onTouch(int x, int y) {
        if (this.isSelected() ? this.onProcess(x, y) : this.onSelect(x, y)) {
            this.updateImage();
        }
    }

    public boolean onSelect(int x, int y) {
        Piece piece = this.getPiece(x, y);
        if (piece != null && (!piece.isTurned() || piece.getSide() == this.side)) {
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
        if (piece != null && (!piece.isTurned() || piece.getSide() == this.side)) {
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

    public void reset() {
        List<Position> positions = new ArrayList<>(32);
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 4; y++) {
                positions.add(new Position(x, y));
            }
        }
        Random random = new Random();
        this.setPiece(positions.remove(random.nextInt(positions.size() - 1)), Piece.get(Piece.GENERAL, Side.Black, false));
        this.setPiece(positions.remove(random.nextInt(positions.size() - 1)), Piece.get(Piece.GENERAL, Side.Red, false));
        for (int i = 0; i < 2; i++) {
            for (int id = 1; id < 6; id++) {
                this.setPiece(positions.remove(random.nextInt(positions.size() - 1)), Piece.get(id, Side.Black, false));
                this.setPiece(positions.remove(random.nextInt(positions.size() - 1)), Piece.get(id, Side.Red, false));
            }
        }
        for (int i = 0; i < 4; i++) {
            this.setPiece(positions.remove(random.nextInt(positions.size() - 1)), Piece.get(Piece.SOLDIER, Side.Black, false));
            this.setPiece(positions.remove(random.nextInt(positions.size() - 1)), Piece.get(Piece.SOLDIER, Side.Red, false));
        }
        this.setPiece(positions.remove(random.nextInt(1)), Piece.get(Piece.SOLDIER, Side.Black, false));
        this.setPiece(positions.remove(0), Piece.get(Piece.SOLDIER, Side.Red, false));
        this.unselect();
    }
}
