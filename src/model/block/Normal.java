package model.block;

import view.Animation;
import view.ImageLoader;

import java.awt.image.BufferedImage;

public class Normal extends Block {

    private Animation animation;
    private boolean breaking;
    private int frames;

    public Normal(double x, double y, BufferedImage style){
        super(x, y, style);
        setBreakable(true);
        setEmpty(true);

        setAnimation();
        breaking = false;
        frames = animation.getLeftFrames().length;
    }

    private void setAnimation(){
        ImageLoader imageLoader = new ImageLoader();
        BufferedImage[] leftFrames = imageLoader.getBlockFrames();

        animation = new Animation(leftFrames, leftFrames);
    }
}

