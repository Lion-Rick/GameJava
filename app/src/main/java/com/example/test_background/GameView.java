package com.example.test_background;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.view.MotionEvent;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameView extends SurfaceView implements Runnable {

    private Thread thread;
    private boolean isPlaying,isGameOver = false;
    public static float screenRatioX, screenRatioY;
    private int screenX, screenY, score = 0;
    private Paint paint;
    private Bat [] bats;
    private SharedPreferences prefs;
    private Random random;
    private SoundPool soundPool;
    private List<BulletFlame> bulletFlames;
    private int sound;
    private Chibi chibi;
    private TestGameActivity activity;
    private  Background background1, background2;

    public  GameView(TestGameActivity activity, int screenX, int screenY){
        super(activity);

        this.activity = activity;

        prefs = activity.getSharedPreferences("game", Context.MODE_PRIVATE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .build();

            soundPool = new SoundPool.Builder()
                    .setAudioAttributes(audioAttributes)
                    .build();
        } else
            soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);

        sound = soundPool.load(activity, R.raw.shoot, 1);
        this.screenX = screenX;
        this.screenY = screenY;

        screenRatioX =(float)  (1920 / screenX);
        screenRatioY =(float) (1080 / screenY);

        background1 = new Background(screenX, screenY, getResources());
        background2 = new Background(screenX, screenY, getResources());

        chibi = new Chibi(this,screenY, getResources());

        bulletFlames = new ArrayList<>();

        background2.x = screenX;

        paint = new Paint();
        paint.setTextSize(128);
        paint.setColor(Color.WHITE);



        bats = new Bat[4];

        for (int i = 0; i < 4; i++ ){
            Bat bat = new Bat(getResources());
            bats[i] = bat;
        }

        random = new Random();

    }

    @Override
    public void run() {

        while (isPlaying) {
            update ();
            draw ();
            sleep ();
        }
    }

    private void update() {

        background1.x -= 10 * screenRatioX;
        background2.x -= 10 * screenRatioX;

        if (background1.x + background1.background.getWidth() < 0) {
            background1.x = screenX;
        }

        if (background2.x + background2.background.getWidth() < 0) {
            background2.x = screenX;
        }

        if (chibi.isGoingUp)
            chibi.y -= 30 * screenRatioY;
        else
            chibi.y += 30 * screenRatioY;

        if (chibi.y < 0)
            chibi.y = 0;

        if (chibi.y > screenY - chibi.height)
            chibi.y = screenY - chibi.height;

        List<BulletFlame> trash = new ArrayList<>();

        for (BulletFlame bulletFlame : bulletFlames) {

            if (bulletFlame.x > screenX)
                trash.add(bulletFlame);

            bulletFlame.x += (int) (50 * screenRatioX);

            for (Bat bat : bats) {
                if (Rect.intersects(bat.getCollisionShape(),
                        bulletFlame.getCollisionShape())){

                    score++;
                    bat.x = -500;
                    bulletFlame.x = screenX + 500;
                    bat.wasShot = true;


                }
            }
        }
        for (BulletFlame bulletFlame : trash)
            bulletFlames.remove(bulletFlame);


        for (Bat bat : bats) {
            bat.x -= bat.speed;
            if(bat.x + bat.width < 0) {

                if (!bat.wasShot){
                    isGameOver = true;
                    return;
                }
                int bound = (int) (30 * screenRatioX);
                bat.speed = random.nextInt(bound);
                
                if (bat.speed < 10 * screenRatioX) 
                    bat.speed = (int) (10 * screenRatioX);
                
                bat.x = screenX;
                bat.y = random.nextInt(screenY - bat.height);

                bat.wasShot = false;
            }
            
            if (Rect.intersects(bat.getCollisionShape(), chibi.getCollisionShape())) {
                isGameOver = true;
                return;
            }
        }

    }

    private void draw() {

        if (getHolder().getSurface().isValid()) {

            Canvas canvas = getHolder().lockCanvas();

            canvas.drawBitmap(background1.background, background1.x, background1.y, paint);
            canvas.drawBitmap(background2.background, background2.x, background2.y, paint);

            for (Bat bat : bats) {
                canvas.drawBitmap(bat.getBat(), bat.x, bat.y, paint);
            }
            
            canvas.drawText(score + "", screenX / 2f, 164, paint);


            if (isGameOver) {
                isPlaying = false;
                canvas.drawBitmap(chibi.getDead(), chibi.x, chibi.y, paint);
                getHolder().unlockCanvasAndPost(canvas);
                saveIfHighScore();
                waitBeforeExiting ();
                getHolder().unlockCanvasAndPost(canvas);
                return;
            }

            canvas.drawBitmap(chibi.getChibi(), chibi.x, chibi.y, paint);

            for (BulletFlame bulletFlame : bulletFlames) {
                canvas.drawBitmap(bulletFlame.bulletFlame, bulletFlame.x, bulletFlame.y, paint);
            }

            getHolder().unlockCanvasAndPost(canvas);
        }


    }

    private void waitBeforeExiting() {

        try {
            Thread.sleep(3000);
            activity.startActivity(new Intent(activity, MainActivity.class));
            activity.finish();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private void saveIfHighScore() {

        if (prefs.getInt("highscore", 0) < score) {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("highscore", score);
            editor.apply();
        }

    }

    private void sleep() {

        try {
            Thread.sleep(25);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }


    public void resume () {

        isPlaying = true;
        thread = new Thread(this);
        thread.start();
    }

    public void pause () {

        try {
            isPlaying = false;
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:
                if (event.getX() < screenX / 2){
                    chibi.isGoingUp = true;
                    chibi.toShoot++;
                }
                break;
            case MotionEvent.ACTION_UP:
                chibi.isGoingUp = false;
                if (event.getY() > screenX / 2)
                    chibi.toShoot++;
                break;
        }
        return true;
    }

    public void newBulletFlame() {

        if (prefs.getBoolean("isMute", false))
            soundPool.play(sound, 1,1,0,0,1);

        BulletFlame bulletFlame = new BulletFlame(getResources());
        bulletFlame.x = chibi.x + chibi.width ;
        bulletFlame.y = chibi.y + (chibi.height / 2);
        bulletFlames.add(bulletFlame);

    }
}
