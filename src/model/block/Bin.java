package model.block;

import model.Item;

import java.awt.image.BufferedImage;

public class Bin extends Block {
    public Bin(double x, double y, BufferedImage style){
        super(x, y, style);
        setEmpty(true);
        setDimension(96, 96);
    }
}
