package model;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.Serializable;

public abstract class Item {
    private double x, y;

    private double velocityX, velocityY;

    private Dimension dimension;

    private BufferedImage style;

    private double gravityAcc;

    private boolean falling, jumping;

    public Item(double x, double y, BufferedImage style) {
        setLocation(x, y);
        setStyle(style);

        if (style != null) {
            setDimension(style.getWidth(), style.getHeight());
        }

        setVelocityX(0);
        setVelocityY(0);
        setGravityAcc(0.38);
        jumping = false;
        falling = true;
    }

    public void draw(Graphics g) {
        BufferedImage style = getStyle();

        if (style != null) {
            g.drawImage(style, (int) x, (int) y, null);
        }
    }

    public void updateLocation() {
        if (jumping && velocityY <= 0) {
            jumping = false;
            falling = true;
        } else if (jumping) {
            velocityY = velocityY - gravityAcc;
            y = y - velocityY;
        }

        if (falling) {
            y = y + velocityY;
            velocityY = velocityY + gravityAcc;
        }

        x = x + velocityX;
    }

    public void setLocation(double x, double y) {
        setX(x);
        setY(y);
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public Dimension getDimension() {
        return dimension;
    }

    public void setDimension(Dimension dimension) {
        this.dimension = dimension;
    }

    public void setDimension(int width, int height) {
        this.dimension = new Dimension(width, height);
    }

    public BufferedImage getStyle() {
        return style;
    }

    public void setStyle(BufferedImage style) {
        this.style = style;
    }

    public double getVelocityX() {
        return velocityX;
    }

    public void setVelocityX(double velocityX) {
        this.velocityX = velocityX;
    }

    public double getVelocityY() {
        return velocityY;
    }

    public void setVelocityY(double velocityY) {
        this.velocityY = velocityY;
    }

    public double getGravityAcc() {
        return gravityAcc;
    }

    public void setGravityAcc(double gravityAcc) {
        this.gravityAcc = gravityAcc;
    }

    public Rectangle getTopBounds() {
        return new Rectangle((int) x + dimension.width / 6, (int) y, 2 * dimension.width / 3, dimension.height / 2);
    }

    public Rectangle getBottomBounds() {
        return new Rectangle((int) x + dimension.width / 6, (int) y + dimension.height / 2, 2 * dimension.width / 3, dimension.height / 2);
    }

    public Rectangle getLeftBounds() {
        return new Rectangle((int) x, (int) y + dimension.height / 4, dimension.width / 4, dimension.height / 2);
    }

    public Rectangle getRightBounds() {
        return new Rectangle((int) x + 3 * dimension.width / 4, (int) y + dimension.height / 4, dimension.width / 4, dimension.height / 2);
    }

    public Rectangle getBounds() {
        return new Rectangle((int) x, (int) y, dimension.width, dimension.height);
    }

    public boolean isFalling() {
        return falling;
    }

    public void setFalling(boolean falling) {
        this.falling = falling;
    }

    public boolean isJumping() {
        return jumping;
    }

    public void setJumping(boolean jumping) {
        this.jumping = jumping;
    }
}
