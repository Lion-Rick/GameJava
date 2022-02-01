package com.example.test_background;

import static com.example.test_background.GameView.screenRatioX;
import static com.example.test_background.GameView.screenRatioY;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

public class Chibi {

    int toShoot = 0;

    boolean isGoingUp = false;
    int x, y, width, height, wingCounter = 0, shootCounter = 1;
    Bitmap chibi1, chibi2, chibi0, shoot1, shoot2, shoot3, shoot4, shoot5, dead;
    private GameView gameView;
    Chibi (GameView gameView, int screenY, Resources res) {

        this.gameView = gameView;
        chibi1 = BitmapFactory.decodeResource(res, R.drawable.chibi_1);
        chibi0 = BitmapFactory.decodeResource(res, R.drawable.chibi_wait);
        chibi2 = BitmapFactory.decodeResource(res, R.drawable.chibi_2);

        width = chibi1.getWidth();
        height = chibi1.getHeight();

        width /= 5;
        height /= 5;

        width = (int) (width * screenRatioX);
        height = (int) (height * screenRatioY);

        chibi1 = Bitmap.createScaledBitmap(chibi1, width, height, false);
        chibi2 = Bitmap.createScaledBitmap(chibi2, width, height, false);
        chibi0 = Bitmap.createScaledBitmap(chibi0, width, height, false);

        //shoot magic flame

        shoot1 = BitmapFactory.decodeResource(res, R.drawable.shoot1);
        shoot2 = BitmapFactory.decodeResource(res, R.drawable.shoot2);
        shoot3 = BitmapFactory.decodeResource(res, R.drawable.shoot3);
        shoot4 = BitmapFactory.decodeResource(res, R.drawable.shoot4);
        shoot5 = BitmapFactory.decodeResource(res, R.drawable.shoot5);

        shoot1 = Bitmap.createScaledBitmap(shoot1, width, height, false);
        shoot2 = Bitmap.createScaledBitmap(shoot2, width, height, false);
        shoot3 = Bitmap.createScaledBitmap(shoot3, width, height, false);
        shoot4 = Bitmap.createScaledBitmap(shoot4, width, height, false);
        shoot5 = Bitmap.createScaledBitmap(shoot5, width, height, false);

        // dead
        dead = BitmapFactory.decodeResource(res, R.drawable.dead);

        dead = Bitmap.createScaledBitmap(dead, width, height, false);

        y = screenY / 2;
        x = (int) (64 + screenRatioX);

    }

    Bitmap getChibi () {

        if (toShoot != 0) {
            if(shootCounter == 1) {
                shootCounter++;
                return shoot1;
            }
            if (shootCounter == 2) {
                shootCounter++;
                return shoot2;
            }
            if (shootCounter == 3) {
                shootCounter++;
                return shoot3;
            }
            if (shootCounter == 4) {
                shootCounter++;
                return shoot4;
            }
            shootCounter = 1;
            toShoot--;
            gameView.newBulletFlame();
            return shoot5;
        }

        if (wingCounter == 0) {
            wingCounter++;
            return chibi1;

        }if (wingCounter == 1){
            wingCounter++;
            return  chibi0;
        }
        wingCounter = 0;

        return chibi2;
    }

    Rect getCollisionShape() {
        return new Rect(x, y, x + width, y + height);
    }

    Bitmap getDead () {
        return dead;
    }
}
