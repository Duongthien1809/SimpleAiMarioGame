package model.enemy;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Mushroom extends Enemy{

    private BufferedImage leftStyle;
    private BufferedImage rightStyle;

    public Mushroom(double x, double y, BufferedImage leftStyle, BufferedImage rightStyle) {
        super(x, y, rightStyle);
        this.leftStyle = leftStyle;
        this.rightStyle = rightStyle;
        setVelocityX(3);
    }

    @Override
    public void draw(Graphics g){
        if(getVelocityX() > 0){
            g.drawImage(rightStyle, (int)getX(), (int)getY(), null);
        } else {
            g.drawImage(leftStyle, (int)getX(), (int)getY(), null);
        }
    }
}

