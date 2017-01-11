package cn.yescallop.darkxiangqi;

import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import cn.yescallop.darkxiangqi.drawable.BoardDrawable;

public class MainActivity extends AppCompatActivity {

    private ImageView boardView;
    private Board board;
    private BoardDrawable boardDrawable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        boardView = (ImageView) findViewById(R.id.board);

        Point size = new Point();
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.activity_main);
        this.getWindowManager().getDefaultDisplay().getSize(size);
        int width = size.x - layout.getPaddingLeft() - layout.getPaddingRight();
        int height = size.y - layout.getPaddingTop() - layout.getPaddingBottom();
        board = new Board(getResources(), boardView);
        boardDrawable = new BoardDrawable(width, height, board);
        boardView.setImageDrawable(boardDrawable);
    }
}
