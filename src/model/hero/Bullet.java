package model.hero;

import model.Item;

import java.awt.image.BufferedImage;

public class Bullet extends Item {

    public Bullet(double x, double y, BufferedImage style, boolean toRight) {
        super(x, y, style);
        setDimension(24, 24);
        setFalling(false);
        setJumping(false);
        setVelocityX(10);

        if(!toRight){
            setVelocityX(-10);
        }
    }
}

