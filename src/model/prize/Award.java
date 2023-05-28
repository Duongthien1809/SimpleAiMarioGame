package model.prize;

import java.awt.*;

public interface Award {

    int getPoint();

    void reveal();

    Rectangle getBounds();

    void onTouch();
}
