package model.enemy;



import model.Item;

import java.awt.image.BufferedImage;


public abstract class Enemy extends Item {
    public Enemy(double x, double y, BufferedImage style) {
        super(x, y, style);
        setFalling(false);
        setJumping(false);
    }
}

