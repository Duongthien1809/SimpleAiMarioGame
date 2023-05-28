package model.block;

import model.Item;
import model.prize.Award;

import java.awt.image.BufferedImage;

public abstract class Block extends Item {
    private boolean empty;

    public Block(double x, double y, BufferedImage style){
        super(x, y, style);
        setDimension(48, 48);
    }

    public boolean isEmpty() {
        return empty;
    }

    public void setEmpty(boolean empty) {
        this.empty = empty;
    }

    public Award reveal(){ return null;}

    public Award getPrize() {
        return null;
    }
}

