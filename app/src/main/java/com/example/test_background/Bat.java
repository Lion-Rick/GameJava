package com.example.test_background;

import static com.example.test_background.GameView.screenRatioX;
import static com.example.test_background.GameView.screenRatioY;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

public class Bat {

    public int speed = 20;
    public boolean wasShot = true;

    int x, y, width, height, batCounter = 1;

    Bitmap bat1, bat2, bat3, bat4, bat5;

    Bat (Resources res) {

        bat1 = BitmapFactory.decodeResource(res, R.drawable.bat3);
        bat2 = BitmapFactory.decodeResource(res, R.drawable.bat2);
        bat3 = BitmapFactory.decodeResource(res, R.drawable.bat3);
        bat4 = BitmapFactory.decodeResource(res, R.drawable.bat4);
        bat5 = BitmapFactory.decodeResource(res, R.drawable.bat5);

        width = bat1.getWidth();
        height = bat2.getHeight();

        width /= 2;
        height /= 2;

        width = (int) (screenRatioX * width);
        height = (int) (screenRatioY * height);

        bat1 = Bitmap.createScaledBitmap(bat1, width, height, false);
        bat2 = Bitmap.createScaledBitmap(bat2, width, height, false);
        bat3 = Bitmap.createScaledBitmap(bat3, width, height, false);
        bat4 = Bitmap.createScaledBitmap(bat4, width, height, false);
        bat5 = Bitmap.createScaledBitmap(bat5, width, height, false);

        y = -height;
    }

    Bitmap getBat() {

        if (batCounter == 1){
            batCounter++;
            return bat1;

        }
        if (batCounter == 2) {
            batCounter++;
            return bat2;
        }
        if (batCounter == 3) {
            batCounter++;
            return bat3;
        }
        if (batCounter == 4) {
            batCounter++;
            return bat4;
        }
        batCounter = 1;
        return bat5;
    }

    Rect getCollisionShape() {
        return new Rect(x, y, x + width, y + height);
    }
}
