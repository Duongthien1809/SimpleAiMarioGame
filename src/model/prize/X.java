package model.prize;

import control.MusicPlayer;
import model.Item;
import model.hero.Hero;

import java.awt.*;
import java.awt.image.BufferedImage;


public class X extends Item implements Award{

    private int point;
    private boolean revealed, acquired = false;
    private int revealBoundary;

    public X(double x, double y, BufferedImage style, int point){
        super(x, y, style);
        this.point = point;
        revealed = false;
        setDimension(30, 42);
        this.revealBoundary = (int)getY() - getDimension().height;
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
    public void onTouch(Hero hero) {
        if(!acquired){
            acquired = true;
            hero.acquirePoints(point);
//            mario.acquireCoin();
//            MusicPlayer.playAcquireX();
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

    public int getRevealBoundary() {
        return revealBoundary;
    }
}

