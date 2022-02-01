package com.example.test_background;

import static com.example.test_background.GameView.screenRatioX;
import static com.example.test_background.GameView.screenRatioY;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

public class BulletFlame {

    int x, y, width, height;
    Bitmap bulletFlame;

    BulletFlame (Resources res) {

         bulletFlame = BitmapFactory.decodeResource(res, R.drawable.bullet);

         width = bulletFlame.getWidth();
         height = bulletFlame.getHeight();

         width /= 4;
         height /= 4;

         width = (int) (width * screenRatioX);
         height = (int) (height * screenRatioY);

         bulletFlame = Bitmap.createScaledBitmap(bulletFlame, width, height, false);

    }

    Rect getCollisionShape() {
        return new Rect(x, y, x + width, y + height);
    }
}
