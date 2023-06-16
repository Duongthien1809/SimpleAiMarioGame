package model;

import model.block.Block;
import model.enemy.Enemy;
import model.hero.Bullet;
import model.hero.Hero;
import model.prize.Award;
import model.prize.X;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Map {

    private double remainingTime;
    private Hero hero;
    private Hero deadHero;
    private ArrayList<Block> blocks = new ArrayList<>();
    private ArrayList<Enemy> enemies = new ArrayList<>();
    private ArrayList<Block> groundBlocks = new ArrayList<>();
    private ArrayList<Award> awards = new ArrayList<>();
    private ArrayList<Bullet> bullets = new ArrayList<>();
    private Endpoint endPoint;
    private BufferedImage backgroundImage;
    private double bottomBorder = 720 - 96;
    private String path;

    public Map(double remainingTime, BufferedImage backgroundImage) {
        this.backgroundImage = backgroundImage;
        this.remainingTime = remainingTime;
    }


    public Hero getHero() {
        return hero;
    }

    public void setHero(Hero hero) {
        this.hero = hero;
    }

    public Hero getDeadHero() {
        return deadHero;
    }

    public void setDeadHero(Hero deadHero) {
        this.deadHero = deadHero;
    }

    public ArrayList<Enemy> getEnemies() {
        return enemies;
    }

    public ArrayList<Award> getAwards() {
        return awards;
    }

    public ArrayList<Bullet> getBullets() {
        return bullets;
    }

    public ArrayList<Block> getAllBlocks() {
        ArrayList<Block> allBricks = new ArrayList<>();

        allBricks.addAll(blocks);
        allBricks.addAll(groundBlocks);

        return allBricks;
    }

    public void addBrick(Block block) {
        this.blocks.add(block);
    }

    public void addGroundBrick(Block block) {
        this.groundBlocks.add(block);
    }

    public void addEnemy(Enemy enemy) {
        this.enemies.add(enemy);
    }


    public synchronized void drawMap(Graphics2D g2) {
        drawBackground(g2);
        drawAwards(g2);
        drawBlocks(g2);
        drawEnemies(g2);
//        drawBullets(g2);
        drawHero(g2);
        if (deadHero != null){
            deadHero.draw(g2);
            deadHero.setY(deadHero.getY() - 4);
            if (deadHero.getY() < 0){
                deadHero = null;
            }
        }
        endPoint.draw(g2);
    }

    private synchronized void drawAwards(Graphics2D g2) {
        for (Award award : awards) {
            if (award instanceof X) {
                ((X) award).draw(g2);
            }
            //kann weitere gift hier erzeugt werden
        }
    }

    private void drawBackground(Graphics2D g2) {
        g2.drawImage(backgroundImage, 0, 0, null);
    }

    private void drawBlocks(Graphics2D g2) {
        for (Block block : blocks) {
            if (block != null)
                block.draw(g2);
        }

        for (Block block : groundBlocks) {
            block.draw(g2);
        }
    }

    private void drawEnemies(Graphics2D g2) {
        for (Enemy enemy : enemies) {
            if (enemy != null)
                enemy.draw(g2);
        }
    }

    private void drawHero(Graphics2D g2) {
        hero.draw(g2);
    }

    public synchronized void updateLocations() {
        hero.updateLocation();

        for (Enemy enemy : enemies) {
            enemy.updateLocation();
        }

/*        for (Iterator<Award> scoreIterator = awards.iterator(); scoreIterator.hasNext(); ) {
            Award score = scoreIterator.next();
            if (score instanceof X) {
                ((X) score).updateLocation();
                if (((X) score).getRevealBoundary() > ((X) score).getY()) {
                    scoreIterator.remove();
                }
            }
        }*/
        ArrayList<Award> toBeRemovedAwards = new ArrayList<>();
        for (Award award : awards){
            if (award instanceof X){
                ((X) award).updateLocation();
                //TODO:
/*                if (((X) award).getRevealBoundary() > ((X) award).getY()){
                    toBeRemovedAwards.add(award);
                }*/
                if (((X) award).getY() < 0){
                    toBeRemovedAwards.add(award);
                }
            }
        }
        awards.removeAll(toBeRemovedAwards);

        ArrayList<Bullet> toBeRemovedBullets = new ArrayList<>();
        for (Bullet bullet : bullets){
            if (bullet.getDistance() > 300){
                toBeRemovedBullets.add(bullet);
            }
            bullet.updateLocation();
        }
        bullets.removeAll(toBeRemovedBullets);

        endPoint.updateLocation();
    }

    public double getBottomBorder() {
        return bottomBorder;
    }

    public synchronized void addAward(Award score) {
        awards.add(score);
    }

    public void addBullet(Bullet bullet) {
        bullets.add(bullet);
    }

    public void setEndPoint(Endpoint endPoint) {
        this.endPoint = endPoint;
    }

    public Endpoint getEndPoint() {
        return endPoint;
    }

    public void removeBullet(Bullet object) {
        bullets.remove(object);
    }

    public void removeEnemy(Enemy object) {
        enemies.remove(object);
    }

    public void removeAward(Award object) {
        awards.remove(object);
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void updateTime(double passed) {
        remainingTime = remainingTime - passed;
    }

    public boolean isTimeOver() {
        return remainingTime <= 0;
    }

    public double getRemainingTime() {
        return remainingTime;
    }
}
