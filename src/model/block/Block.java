package model.block;

import model.Item;

import java.awt.image.BufferedImage;

public abstract class Block extends Item {

    private boolean breakable;

    private boolean empty;

    public Block(double x, double y, BufferedImage style){
        super(x, y, style);
        setDimension(48, 48);
    }

    public boolean isBreakable() {
        return breakable;
    }

    public void setBreakable(boolean breakable) {
        this.breakable = breakable;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void setEmpty(boolean empty) {
        this.empty = empty;
    }
}

