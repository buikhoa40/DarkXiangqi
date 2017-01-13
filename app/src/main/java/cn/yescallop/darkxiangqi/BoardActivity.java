package cn.yescallop.darkxiangqi;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.graphics.Point;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.view.KeyEvent;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.List;

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
        new Thread() {
            @Override
            public void run() {
                while (!layout.isLaidOut()) ;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Snackbar.make(layout, resId, 2000).show();
                    }
                });
            }
        }.start();
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            new AlertDialog.Builder(this)
                    .setTitle(R.string.warning)
                    .setMessage(R.string.disconnect)
                    .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            onBackPressed();
                        }
                    })
                    .setNegativeButton(R.string.cancel, null)
                    .show();
        }
        return true;
    }

    public void showNotification() {
        if (this.isForeground()) return;
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Notification notification = new NotificationCompat.Builder(this)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(getString(R.string.event))
                .setContentIntent(PendingIntent.getActivity(this, 0, getIntent(), PendingIntent.FLAG_CANCEL_CURRENT))
                .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setTicker(getString(R.string.event))
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true)
                .build();
        manager.notify(0, notification);
    }

    public boolean isForeground() {
        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> processes = manager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo process : processes) {
            if (process.processName.equals(getPackageName())) {
                return process.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND;
            }
        }
        return false;
    }
}
