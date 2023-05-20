package model.block;

import view.Animation;
import view.ImageLoader;

import java.awt.image.BufferedImage;

public class Ground extends Block {
    public Ground(double x, double y, BufferedImage style){
        super(x, y, style);
        setBreakable(false);
        setEmpty(true);
    }
}
