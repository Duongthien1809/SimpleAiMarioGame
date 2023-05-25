package model.block;

import java.awt.image.BufferedImage;

public class Normal extends Block {
    public Normal(double x, double y, BufferedImage style){
        super(x, y, style);
        setEmpty(true);
    }
}

