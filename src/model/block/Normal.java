package model.block;

import view.Animation;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Normal extends Block {
    private Animation animation;
    private boolean breaking;
    private int frames;
    public Normal(double x, double y, BufferedImage style){
        super(x, y, style);
        setEmpty(true);
    }

    public int getFrames() {
        return frames;
    }

    public void animate(){
        if(breaking){
            setStyle(animation.animate(3, true));
            frames--;
        }
    }
}

