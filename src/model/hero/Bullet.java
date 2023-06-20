package model.hero;

import model.Item;

import java.awt.image.BufferedImage;

public class Bullet extends Item {
    private double originalX;

    public Bullet(double x, double y, BufferedImage style, boolean toRight) {
        super(x, y, style);
        originalX = x;
        setDimension(24, 24);
        setFalling(false);
        setJumping(false);
        setVelocityX(10);

        if(!toRight){
            setVelocityX(-10);
        }
    }

    public double getDistance() {
        return Math.abs(originalX - getX());
    }
}

