package cn.yescallop.darkxiangqi.drawable;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

import cn.yescallop.darkxiangqi.Board;
import cn.yescallop.darkxiangqi.R;
import cn.yescallop.darkxiangqi.piece.Piece;

public class BoardDrawable extends Drawable {

    private int width;
    private int height;
    private int gridSize;
    private Board board;
    private Resources resources;

    private Paint mPaint;

    public BoardDrawable(int width, int height, Board board) {
        if ((gridSize = (int) Math.floor((height - 2) / 4d)) * 8 + 2 > width) {
            gridSize = (int) Math.floor((width - 2) / 8d);
        }
        this.width = gridSize * 8 + 2;
        this.height = gridSize * 4 + 2;

        this.board = board;
        this.resources = board.getResources();

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
    }

    @Override
    public void draw(Canvas canvas) {
        mPaint.setColor(Color.BLACK);
        mPaint.setStrokeWidth(2);
        for (int x = 1; x < width; x += gridSize) {
            for (int y = 1; y < height; y += gridSize) {
                canvas.drawLine(x, 0, x, height, mPaint);
                canvas.drawLine(0, y, width, y, mPaint);
            }
        }


        Piece[][] pieces = board.getPieces();
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 4; y++) {
                Piece piece = pieces[x][y];
                if (piece == null) continue;
                Bitmap bitmap;
                if (piece.isTurned()) {
                    bitmap = BitmapFactory.decodeResource(resources, resources.getIdentifier(pieces[x][y].getResourceName(), "drawable", "cn.yescallop.darkxiangqi"));
                } else {
                    bitmap = BitmapFactory.decodeResource(resources, R.drawable.unturned);
                }
                canvas.drawBitmap(bitmap, null, new Rect(x * gridSize + 2, y * gridSize + 2, (x + 1) * gridSize, (y + 1) * gridSize), null);
            }
        }

        Piece selected = board.getSelectedPiece();
        if (selected != null) {
            int x = selected.x;
            int y = selected.y;
            canvas.drawBitmap(BitmapFactory.decodeResource(resources, board.isHolding() ? R.drawable.selected : R.drawable.selected_2), null, new Rect(x * gridSize + 2, y * gridSize + 2, (x + 1) * gridSize, (y + 1) * gridSize), null);
        }
    }

    @Override
    public int getIntrinsicWidth() {
        return width;
    }

    @Override
    public int getIntrinsicHeight() {
        return height;
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSPARENT;
    }

    @Override
    public void setAlpha(int alpha) {
        mPaint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        mPaint.setColorFilter(cf);
    }
}
