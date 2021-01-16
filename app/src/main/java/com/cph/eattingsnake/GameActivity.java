package com.cph.eattingsnake;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;

public class GameActivity extends Activity implements android.view.GestureDetector.OnGestureListener {

    GestureDetector detector;
    Snake snake;
    Yard yard;
    Egg egg;
    private DisplayMetrics displayMetrics;
    static int screenW, screenH;
    GameThread gameThread;
    MediaPlayer bgmPlayer, eatPlayer;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); // 去掉标题栏
        yard = new Yard(this);
        bgmPlayer = MediaPlayer.create(this, R.raw.bgm);
        setContentView(yard);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        sharedPreferences = getSharedPreferences("record", Activity.MODE_PRIVATE); // 获取游戏数据文件
        editor = sharedPreferences.edit();
        detector = new GestureDetector(this, this); // 手势监测器
        displayMetrics = new DisplayMetrics(); // 获得屏幕尺寸
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screenW = displayMetrics.widthPixels;
        screenH = displayMetrics.heightPixels;
        snake = new Snake();
        egg = new Egg();
        bgmPlayer.setLooping(true);
        bgmPlayer.start();
    }

    @Override
    protected void onStop() {
        bgmPlayer.stop();
        super.onStop();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        bgmPlayer.start();
    }

    /* (non-Javadoc)
     * @see android.app.Activity#onStart()
     */
    @Override
    protected void onStart() {
        super.onStart();

    }

    protected void onResume() {
        super.onResume();
        bgmPlayer.start();
    }

    public boolean onTouchEvent(MotionEvent me) {
        return detector.onTouchEvent(me); // 将该activity上的触碰事件交给GestureDetector处理
    }

    public boolean onDown(MotionEvent arg0) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    // 滑屏监测
    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        Log.d("EattingSnake", "onFling");

        float minMove = 50; // 最小滑动距离
        float minVelocity = 0; // 最小滑动速度
        float beginX = e1.getX();
        float endX = e2.getX();
        float beginY = e1.getY();
        float endY = e2.getY();
        if (beginX - endX > minMove && Math.abs(velocityX) > minVelocity && Math.abs(beginX - endX) > Math.abs(beginY - endY)) { // 左滑
            if (snake.head.dir != Dir.R) {
                snake.head.dir = Dir.L;
            }
        } else if (endX - beginX > minMove && Math.abs(velocityX) > minVelocity && Math.abs(beginX - endX) > Math.abs(beginY - endY)) { // 右滑
            if (snake.head.dir != Dir.L) {
                snake.head.dir = Dir.R;
            }
        } else if (beginY - endY > minMove && Math.abs(velocityY) > minVelocity && Math.abs(beginX - endX) < Math.abs(beginY - endY)) { // 上滑
            if (snake.head.dir != Dir.D) {
                snake.head.dir = Dir.U;
            }
        } else if (endY - beginY > minMove && Math.abs(velocityY) > minVelocity && Math.abs(beginX - endX) < Math.abs(beginY - endY)) { // 下滑
            if (snake.head.dir != Dir.U) {
                snake.head.dir = Dir.D;
            }
        }
        return false;
    }

    class Yard extends SurfaceView implements SurfaceHolder.Callback {
        static final int ROWS = 25;
        static final int COLS = 15;
        public static final int SIZE = 40;
        private SurfaceHolder holder;

        public Yard(Context context) {
            super(context);
            holder = this.getHolder();
            holder.addCallback(this);
            gameThread = new GameThread(holder);
        }

        public Yard(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
        }

        public Yard(Context context, AttributeSet attrs) { // 不知道为什么写三个构造器xml才不报错
            super(context, attrs);
        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) { // surfaceView绘画入口
            gameThread.isRun = true;
            gameThread.start();

        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            gameThread.isRun = false;
        }
    }

    class GameThread extends Thread {

        public boolean isRun;
        private SurfaceHolder holder;

        int length = 2;

        int COLS = Yard.COLS;
        int ROWS = Yard.ROWS;
        int SIZE = Yard.SIZE;
        float exSize;


        public GameThread(SurfaceHolder holder) {
            this.holder = holder;
            isRun = true;
        }

        @Override
        public void run() {
            while (isRun) {
                Canvas canvas;
                Paint paint = new Paint();
                exSize = paint.getTextSize();
                synchronized (holder) {
                    canvas = holder.lockCanvas();// 锁定画布，一般在锁定后就可以通过其返回的画布对象Canvas，在其上面画图等操作了。
                    canvas.drawRGB(255, 160, 0);// 清屏：指定画布的背景色, 从而去除残影
                    paint.setStyle(Style.FILL); //画出填充图形

                    paint.setTextSize(30.0f);
                    paint.setFakeBoldText(true);
                    paint.setColor(Color.WHITE);
                    canvas.drawText("长度： " + length, 300, 200, paint);
                    if (length > sharedPreferences.getInt("rec", 2)) {
                        editor.putInt("rec", length);
                        editor.commit();
                    }
                    canvas.drawText("记录： " + sharedPreferences.getInt("rec", 2), 300, 235, paint);
                    initialisePaint(paint);

                    snake.draw(paint, canvas);//将画笔画布递给下一层的元素
                    egg.draw(paint, canvas);
                }

                if (snake.checkDead()) {
                    paint.setTextSize(80.0f);
                    paint.setFakeBoldText(true);
                    paint.setColor(Color.RED);
                    canvas.drawText("GAME OVER", 130, 700, paint);
                    bgmPlayer.stop();
                    isRun = false;
                }
                if (canvas != null) {
                    holder.unlockCanvasAndPost(canvas);// 结束锁定画图，并提交改变。
                }
                try {
                    Thread.sleep(300);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (snake.getRect().intersect(egg.getRect())) {
                    snake.add();
                    egg.reShow();
                    length++;
                }

            }
        }

        public void initialisePaint(Paint paint)   //初始化paint参数
        {
            paint.setTextSize(exSize);
            paint.setFakeBoldText(false);
        }


    }


}
