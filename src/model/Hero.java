package model;

import control.GameX;
import view.Animation;
import view.ImageLoader;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Hero extends Movable {
    private Animation animation;
    private boolean towardsRight = true;

    public Hero(double x, double y) {
        super(x, y, null);
        setDimension(48, 48);

        ImageLoader imageLoader = new ImageLoader();
        BufferedImage[] leftFrames = imageLoader.getLeftFrames();
        BufferedImage[] rightFrames = imageLoader.getRightFrames();

        Animation animation = new Animation(leftFrames, rightFrames);
        this.animation = animation;
        setStyle(getCurrentStyle(towardsRight, false, false));
    }

    public BufferedImage getCurrentStyle(boolean toRight, boolean movingInX, boolean movingInY) {
        BufferedImage style;

        if (movingInY && toRight) {
            style = animation.getRightFrames()[0];
        } else if (movingInY) {
            style = animation.getLeftFrames()[0];
        } else if (movingInX) {
            style = animation.animate(5, toRight);
        } else {
            if (toRight) {
                style = animation.getRightFrames()[1];
            } else
                style = animation.getLeftFrames()[1];
        }

        return style;
    }

    @Override
    public void draw(Graphics g) {
        boolean movingInX = (getVelocityX() != 0);
        boolean movingInY = (getVelocityY() != 0);

        setStyle(getCurrentStyle(towardsRight, movingInX, movingInY));

        super.draw(g);
    }

    public void jump() {
        if (!isJumping() && !isFalling()) {
            setJumping(true);
            setVelocityY(10);
        }
    }

    //    public void move(boolean towardsRight, Camera camera) {
    public void move(boolean towardsRight) {
        if (towardsRight) {
            setVelocityX(5);
        }
//        else if(camera.getX() < getX()){
//            setVelocityX(-5);
//        }
        else {
            setVelocityX(-5);
        }

        this.towardsRight = towardsRight;
    }
}
