package com.cph.eattingsnake;

import java.util.ArrayList;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

public class Snake {

    int size, screenW, screenH;
    int X; // yard顶部到屏幕顶部的距离
    int Y; // yard左侧到屏幕左侧的距离
    Node head = new Node(22, 2, Dir.R);
    ArrayList<Node> snakeBody = new ArrayList<Node>();

    public Snake() {
        snakeBody.add(head);
        add();
        size = GameActivity.Yard.SIZE;
        screenW = GameActivity.screenW;
        screenH = GameActivity.screenH;
        X = (screenH - GameActivity.Yard.SIZE * 25) / 2;
        Y = (screenW - GameActivity.Yard.SIZE * 15) / 2;
    }

    public void draw(Paint p, Canvas c) {
        for (int i = 0; i < snakeBody.size(); i++) {
            snakeBody.get(i).draw(p, c);
        }
		//每次刷新都会移动
        move();
    }

    private void move() {
        switch (head.dir) {
            case L:
                head.col--;
                break;
            case R:
                head.col++;
                break;
            case U:
                head.row--;
                break;
            case D:
                head.row++;
                break;
        }
        add();
        delete();
    }

    public void add() {
        Node node = null;
        switch (head.dir) {
            case L:
                node = new Node(head.row, head.col + 1, Dir.L);
                break;
            case R:
                node = new Node(head.row, head.col - 1, Dir.R);
                break;
            case U:
                node = new Node(head.row + 1, head.col, Dir.U);
                break;
            case D:
                node = new Node(head.row - 1, head.col, Dir.D);
                break;
        }
		//在脖子上加上此节点
        snakeBody.add(1, node);
    }

    private void delete() {
        if (snakeBody.size() <= 1)
            return;
        else
            snakeBody.remove(snakeBody.size() - 1);
    }

    public Rect getRect() {
        return new Rect((head.col - 1) * size + Y, (head.row - 1) * size + X, head.col * size + Y, head.row * size + X);
    }

    public boolean checkDead() {
        for (int i = 1; i < snakeBody.size(); i++) {
            if (this.getRect().intersect(snakeBody.get(i).getRect())) {
                return true;
            }
        }
        return false;
    }

    class Node {
        int row, col;

        Dir dir;

        public Node(int row, int col, Dir d) {
            this.row = row;
            this.col = col;
            dir = d;
            size = GameActivity.Yard.SIZE;
        }

        public void draw(Paint p, Canvas c) {
            if (this == head) {
                p.setColor(Color.GREEN);
                c.drawRect((col - 1) * size + Y + 3, (row - 1) * size + X + 3, col * size + Y - 3, row * size + X - 3, p);

            } else {
                p.setColor(Color.BLACK);
                c.drawRect((col - 1) * size + Y + 3, (row - 1) * size + X + 3, col * size + Y - 3, row * size + X - 3, p);
            }
        }

        public Rect getRect() {
            return new Rect((col - 1) * size + Y, (row - 1) * size + X, col * size + Y, row * size + X);
        }

    }
}
