package com.cph.eattingsnake;

import java.util.Random;

import com.cph.eattingsnake.GameActivity.Yard;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;

public class Egg {

    int size, row, col, screenW, screenH, X, Y;
    Random r;
    Rect box;
    RectF Box;

    public Egg() {
        r = new Random();
        size = Yard.SIZE;
        this.row = r.nextInt(Yard.ROWS);
        this.col = r.nextInt(Yard.COLS);
        screenW = GameActivity.screenW;
        screenH = GameActivity.screenH;
        X = (screenH - GameActivity.Yard.SIZE * 25) / 2;
        Y = (screenW - GameActivity.Yard.SIZE * 15) / 2;

    }

    public void reShow() {
        this.row = r.nextInt(Yard.ROWS);
        this.col = r.nextInt(Yard.COLS);
    }

    public Rect getRect() {
        return new Rect((col - 1) * size + Y, (row - 1) * size + X, col * size + Y, row * size + X);
    }

    public void draw(Paint p, Canvas c) {
        p.setColor(Color.WHITE);
        box = new Rect((col - 1) * size + Y + 5, (row - 1) * size + X + 5, col * size + Y - 5, row * size + X - 5);
        Box = new RectF(box);
        c.drawOval(Box, p);
    }


}
