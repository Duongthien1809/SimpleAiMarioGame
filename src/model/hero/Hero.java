package model.hero;

import model.Item;
import view.Animation;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Hero extends Item {
    private Animation animation;
    private boolean towardsRight = true;

    public Hero(double x, double y, Animation animation, BufferedImage bulletStyle) {
        super(x, y, null);
        setDimension(48, 48);
        this.animation = animation;
        this.bulletStyle = bulletStyle;
        this.isArmed = true;//TODO: isArmed should be false by default
        setStyle(getCurrentStyle(towardsRight, false, false));
    }

    public BufferedImage getCurrentStyle(boolean towardsRight, boolean movingInX, boolean movingInY) {
        BufferedImage style;

        if (movingInY && towardsRight) {
            style = animation.getRightFrames()[0];
        } else if (movingInY) {
            style = animation.getLeftFrames()[0];
        } else if (movingInX) {
            style = animation.animate(5, towardsRight);
        } else {
            if (towardsRight) {
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

    public boolean isTowardsRight() {
        return towardsRight;
    }

    private boolean isArmed;
    private BufferedImage bulletStyle;

    public Bullet shoot(boolean towardsRight, double x, double y) {
        if (isArmed) {
            return new Bullet(x, y + 48, bulletStyle, towardsRight);
        }
        return null;
    }

    private int remainingLives = 3;
    public void onTouchEnemy(){
/*        if(!marioForm.isSuper() && !marioForm.isFire()){
            remainingLives--;
            engine.playMarioDies();
            return true;
        }
        else{
            engine.shakeCamera();
            marioForm = marioForm.onTouchEnemy(engine.getImageLoader());
            setDimension(48, 48);
            return false;
        }*/
        remainingLives--;
        System.out.println(remainingLives);
    }
}
