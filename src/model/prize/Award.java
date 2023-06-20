package model.prize;

import model.hero.Hero;

import java.awt.*;

public interface Award {

    int getPoint();

    void reveal();

    Rectangle getBounds();

    void onTouch(Hero hero);
}
