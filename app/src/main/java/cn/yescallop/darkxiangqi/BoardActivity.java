package cn.yescallop.darkxiangqi;

import android.graphics.Point;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import cn.yescallop.darkxiangqi.drawable.BoardDrawable;
import cn.yescallop.darkxiangqi.network.Client;

public class BoardActivity extends AppCompatActivity {

    private static BoardActivity instance;
    private ImageView boardView;
    private Board board;
    private BoardDrawable boardDrawable;
    private RelativeLayout layout;

    public static BoardActivity getInstance() {
        return instance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);

        boardView = (ImageView) findViewById(R.id.board);

        Point size = new Point();
        layout = (RelativeLayout) findViewById(R.id.activity_main);
        this.getWindowManager().getDefaultDisplay().getSize(size);
        int width = size.x - layout.getPaddingLeft() - layout.getPaddingRight();
        int height = size.y - layout.getPaddingTop() - layout.getPaddingBottom();
        board = new Board(getResources(), boardView);
        boardDrawable = new BoardDrawable(width, height, board);
        boardView.setImageDrawable(boardDrawable);

        instance = this;
        new Client().connect("yescallop.cn", 14144);
    }

    public RelativeLayout getLayout() {
        return layout;
    }

    public void showSnackbar(final int resId) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Snackbar.make(layout, resId, 2000).show();
            }
        });
    }

    public void showSnackbar(final String text) {
        new Thread() {
            @Override
            public void run() {
                while (!layout.isLaidOut()) ;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Snackbar.make(layout, text, 2000).show();
                    }
                });
            }
        }.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Client.getInstance().close();
    }
}
