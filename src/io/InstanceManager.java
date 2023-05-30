package io;

import control.GameX;
import control.KeyController;
import control.WindowController;
import model.block.Block;
import model.hero.Hero;
import view.ImageLoader;

import java.util.ArrayList;

public class InstanceManager {
    private WindowController windowController;
    private GameX gameX;
    private Hero hero;
    private ArrayList<Block> blocks;
    private ImageLoader imageLoader;
    private KeyController keyController;

    public void setWindowController(WindowController windowController){
        this.windowController = windowController;
        this.hero = windowController.getHero();
        this.blocks = windowController.getBlocks();
        this.imageLoader = windowController.getImageLoader();
        this.keyController = windowController.getKeyController();
    }

    public WindowController getWindowController() {

        return windowController;
    }
}
