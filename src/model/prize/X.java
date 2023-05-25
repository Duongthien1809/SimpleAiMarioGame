package model.prize;

import model.Item;

import java.awt.*;
import java.awt.image.BufferedImage;


public class X extends Item implements Award{

    private int point;
    private boolean revealed, acquired = false;

    public X(double x, double y, BufferedImage style, int point){
        super(x, y, style);
        this.point = point;
        revealed = false;
        setDimension(30, 42);
    }

    @Override
    public int getPoint() {
        return point;
    }

    @Override
    public void reveal() {
        revealed = true;
    }

    @Override
    public void onTouch() {
        if(!acquired){
            acquired = true;
//            mario.acquirePoints(point);
//            mario.acquireCoin();
        }
    }

    @Override
    public void updateLocation(){
        if(revealed){
            setY(getY() - 5);
        }
    }

    @Override
    public void draw(Graphics g){
        if(revealed){
            g.drawImage(getStyle(), (int)getX(), (int)getY(), null);
        }
    }
}

