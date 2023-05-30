package model;

import model.block.Block;
import model.block.Normal;
import model.enemy.Enemy;
import model.hero.Bullet;
import model.hero.Hero;
import model.prize.Award;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;

public class Map {
    //init the game object
    private Hero hero;
    private ArrayList<Block> grounds = new ArrayList<>();
    private ArrayList<Block> normalBlocks = new ArrayList<>();
    private ArrayList<Block> revealedBlocks = new ArrayList<>();
    private ArrayList<Enemy> enemies = new ArrayList<>();
    private ArrayList<Bullet> bullets = new ArrayList<>();
    private ArrayList<Award> awards = new ArrayList<>();
    private double remainTime;
    private BufferedImage backgroundImage;
    private String path;
    private EndPoint endPoint;

    public Map(double remainTime, BufferedImage backgroundImage) {
        this.remainTime = remainTime;
        this.backgroundImage = backgroundImage;
    }

    public Map(Hero hero) {
        this.hero = hero;
    }

    public void setHero(Hero hero) {
        this.hero = hero;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setEndPoint(EndPoint endPoint) {
        this.endPoint = endPoint;
    }

    public void setRemainTime(double remainTime) {
        this.remainTime = remainTime;
    }


    public Hero getHero() {
        return hero;
    }

    public EndPoint getEndPoint() {
        return endPoint;
    }

    public String getPath() {
        return path;
    }

    public double getRemainTime() {
        return remainTime;
    }

    public void addBlock(Block block) {
        this.normalBlocks.add(block);
    }

    public void addGround(Block block) {
        this.grounds.add(block);
    }
    public void addEnemy(Enemy enemy){this.enemies.add(enemy);}

    public void addRevealedBlock(Normal normalBlock) {
        revealedBlocks.add(normalBlock);
    }

    public ArrayList<Block> getAllBlocks() {
        ArrayList<Block> allBlocks = new ArrayList<>();
        allBlocks.addAll(this.normalBlocks);
        allBlocks.addAll(this.grounds);
        return allBlocks;
    }
    public ArrayList<Enemy> getEnemies() {
        return enemies;
    }

    public ArrayList<Bullet> getBullets() {
        return bullets;
    }

    public ArrayList<Award> getAwards() {
        return awards;
    }

    public void drawMap(Graphics2D g2) {
        drawBackground(g2);
        drawBlock(g2);
        drawHero(g2);
        endPoint.draw(g2);
    }

    private void drawHero(Graphics2D g2) {
        hero.draw(g2);
    }

    private void drawBlock(Graphics2D g2) {
        for (Block block : normalBlocks) {
            if (block != null) block.draw(g2);
        }
        for (Block block : grounds) {
            block.draw(g2);
        }
    }

    private void drawBackground(Graphics2D g2) {
        g2.drawImage(backgroundImage, 0, 0, null);
    }

    public void updateLocations() {
        hero.updateLocation();
        for (Iterator<Block> blockIterator = revealedBlocks.iterator(); blockIterator.hasNext(); ) {
            Normal normalBlock = (Normal) blockIterator.next();
            normalBlock.animate();
            if (normalBlock.getFrames() < 0) {
                this.normalBlocks.remove(normalBlock);
                blockIterator.remove();
            }
        }

//        endPoint.updateLocation();
    }

}
