package model;

import java.awt.image.BufferedImage;

public class Endpoint extends Item{
    private boolean touched = false;


    public Endpoint(double x, double y, BufferedImage style) {
        super(x, y, style);
    }

    @Override
    public void updateLocation() {
        if(touched){
            if(getY() + getDimension().getHeight() >= 576){
                setFalling(false);
                setVelocityY(0);
                setY(576 - getDimension().getHeight());
            }
            super.updateLocation();
        }
    }

    public boolean isTouched() {
        return touched;
    }

    public void setTouched(boolean touched) {
        this.touched = touched;
    }
}
