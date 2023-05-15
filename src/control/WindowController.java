package control;

import model.Hero;

import javax.swing.*;
import java.awt.*;

public class WindowController extends JPanel {
    private GameX gameX;
    private Hero hero;

    public WindowController(GameX gameX, Hero hero, int width, int height) {
        this.gameX = gameX;
        this.hero = hero;
        setPreferredSize(new Dimension(width,height));
        setMaximumSize(new Dimension(width,height));
        setMinimumSize(new Dimension(width,height));
    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g.create();
        Status gameStatus = gameX.getStatus();
        switch (gameStatus){

        }
        hero.draw(g2);
        g2.dispose();
    }

    public void updateLocations(){
        hero.updateLocation();
    }

    public void render(){
        repaint();
    }
}
