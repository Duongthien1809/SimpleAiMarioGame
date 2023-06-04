package model.hero;

import control.Camera;
import model.Item;
import view.Animation;
import view.ImageLoader;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Hero extends Item {
    private boolean towardsRight = true;
    private Animation animation;
    private int remainingLives = 3;
    private boolean isArmed;
    private BufferedImage bulletStyle;
    private int points;
    private int coins;

    public Hero(double x, double y) {
        super(x, y, null);

        //init point
        remainingLives = 3;
        coins = 0;
        points = 0;
        setDimension(48, 48);
        this.bulletStyle = bulletStyle;
        this.isArmed = true;//TODO: isArmed should be false by default
        ImageLoader imageLoader = new ImageLoader();
        BufferedImage[] leftFrames = imageLoader.getHeroLeftFrames();
        BufferedImage[] rightFrames = imageLoader.getHeroRightFrames();

        this.animation = new Animation(leftFrames, rightFrames);
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
    public void move(boolean towardsRight, Camera camera) {
        if (towardsRight) {
            setVelocityX(5);
        }
        else if(camera.getX() < getX()){
            setVelocityX(-5);
        }
        else {
            setVelocityX(-5);
        }

        this.towardsRight = towardsRight;
    }

    public boolean isTowardsRight() {
        return towardsRight;
    }



    public Bullet shoot(boolean towardsRight, double x, double y) {
        if (isArmed) {
            return new Bullet(x, y + 48, bulletStyle, towardsRight);
        }
        return null;
    }
    public void acquirePoints(int point){
        points = points + point;
    }

    public boolean onTouchEnemy(){
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
        return remainingLives > 0;
    }
    public int getRemainingLives() {
        return remainingLives;
    }

    public void setRemainingLives(int remainingLives) {
        this.remainingLives = remainingLives;
    }

    public int getPoints() {
        return points;
    }

    public int getCoins() {
        return coins;
    }
    public void resetLocation() {
        setVelocityX(0);
        setVelocityY(0);
        setX(50);
        setJumping(false);
        setFalling(true);
    }
}
