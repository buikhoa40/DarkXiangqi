package cn.yescallop.darkxiangqi;

import android.view.MotionEvent;
import android.view.View;

public class BoardOnTouchListener implements View.OnTouchListener {

    private Board board;

    public BoardOnTouchListener(Board board) {
        this.board = board;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() != MotionEvent.ACTION_DOWN) {
            return false;
        }
        int x = (int) ((event.getX() - 2) / ((v.getWidth() - 2) / 8));
        int y = (int) ((event.getY() - 2) / ((v.getHeight() - 2) / 4));
        board.onTouch(x, y);
        return true;
    }
}
