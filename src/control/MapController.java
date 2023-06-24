package control;

import model.Item;
import model.Map;
import model.block.Block;
import model.enemy.Enemy;
import model.hero.Bullet;
import model.hero.Hero;
import model.prize.Award;
import view.Animation;
import view.ImageLoader;

import java.awt.*;
import java.util.ArrayList;

public class MapController {
    private Map map;
    private final Dimension prefSize = new Dimension(960, 576);

    public MapController() {
    }

    public void updateLocations() {
        if (map == null)
            return;

        map.updateLocations();
    }

    public void resetCurrentMap(WindowController windowController) {
        Hero hero = getHero();
        hero.resetRemainingLives();
        hero.resetLocation();
        hero.resetPrevX();
        windowController.resetCamera();
        windowController.resetCounter();
        windowController.resetScore();
        createMap(windowController.getImageLoader(), map.getPath());
        map.setHero(hero);
        MusicPlayer.resetBackground();
    }

    public boolean createMap(ImageLoader loader, String path) {
        MapCreator mapCreator = new MapCreator(loader);
        map = mapCreator.createMap("/maps/" + path, 400);
        return map != null;
    }

    public void acquirePoints(int point) {
        map.getHero().acquirePoints(point);
    }

    public Hero getHero() {
        return map.getHero();
    }

    public boolean isGameOver() {
        return map.getHero().getRemainingLives() == 0 || map.isTimeOver();
    }

    public int getScore() {
        return map.getHero().getPoints();
    }

    public int getRemainingLives() {
        return map.getHero().getRemainingLives();
    }

    public int getCoins() {
        return map.getHero().getCoins();
    }


    public void addBullet(Bullet bullet){
        map.addBullet(bullet);
    }

    public ArrayList<Bullet> getBullets(){
        return map.getBullets();
    }

    public void drawMap(Graphics2D g2) {
        map.drawMap(g2);
    }

    public int passMission() {
        if (map.getHero().getX() >= map.getEndPoint().getX() && !map.getEndPoint().isTouched()) {
            map.getEndPoint().setTouched(true);
            int height = (int) map.getHero().getY();
            return height * 2;
        } else
            return -1;
    }

    public boolean endLevel() {
        return map.getHero().getX() >= map.getEndPoint().getX() + 320;
    }

    public void checkCollisions(WindowController windowController) {
        checkBottomCollisions();
        checkTopCollisions();
        checkHeroHorizontalCollision(windowController);
        checkEnemyCollisions();
        checkBulletContact();
        checkAwardContact();
    }

    private void checkBottomCollisions() {
        Hero hero = getHero();
        ArrayList<Block> blocks = map.getAllBlocks();
        ArrayList<Enemy> enemies = map.getEnemies();
        ArrayList<Item> toBeRemoved = new ArrayList<>();
        Rectangle heroBottomBounds = hero.getBottomBounds();

        if (!hero.isJumping())
            hero.setFalling(true);

        for (Block block : blocks) {
            Rectangle brickTopBounds = block.getTopBounds();
            if (heroBottomBounds.intersects(brickTopBounds)) {
                hero.setY(block.getY() - hero.getDimension().height + 1);
                hero.setFalling(false);
                hero.setVelocityY(0);
            }
        }

        //Enemy
        for (Enemy enemy : enemies) {
            Rectangle enemyTopBounds = enemy.getTopBounds();
            if (heroBottomBounds.intersects(enemyTopBounds)) {
                MusicPlayer.playStomp();
                acquirePoints(50);
                toBeRemoved.add(enemy);
            }
        }

        if (hero.getY() + hero.getDimension().height >= map.getBottomBorder()) {
            hero.setY(map.getBottomBorder() - hero.getDimension().height);
            hero.setFalling(false);
            hero.setVelocityY(0);
        }

        removeObjects(toBeRemoved);
    }

    private void checkTopCollisions() {
        Hero hero = getHero();
        ArrayList<Block> blocks = map.getAllBlocks();
        Rectangle marioTopBounds = hero.getTopBounds();

        for (Block block : blocks) {
            Rectangle brickBottomBounds = block.getBottomBounds();
            if (marioTopBounds.intersects(brickBottomBounds)) {
                hero.setVelocityY(0);
                hero.setY(block.getY() + block.getDimension().height);
                Award award = block.reveal();
                if (award != null) {
                    acquirePoints(50);
                    map.addAward(award);
                    MusicPlayer.playAcquireX();
                }
            }
        }
    }

    private void checkHeroHorizontalCollision(WindowController windowController) {
        Hero hero = getHero();
        ArrayList<Block> blocks = map.getAllBlocks();
        ArrayList<Enemy> enemies = map.getEnemies();
        ArrayList<Item> toBeRemoved = new ArrayList<>();
        boolean towardsRight = hero.isTowardsRight();

        Rectangle marioBounds = towardsRight ? hero.getRightBounds() : hero.getLeftBounds();

        for (Block block : blocks) {
            Rectangle blockBounds = !towardsRight ? block.getRightBounds() : block.getLeftBounds();
            if (marioBounds.intersects(blockBounds)) {
                hero.setVelocityX(0);
                if (towardsRight)
                    hero.setX(block.getX() - hero.getDimension().width);
                else
                    hero.setX(block.getX() + block.getDimension().width);
            }
        }

        //enemy
        boolean isInjured = false;
        for (Enemy enemy : enemies) {
            Rectangle enemyBounds = !towardsRight ? enemy.getRightBounds() : enemy.getLeftBounds();
            if (marioBounds.intersects(enemyBounds)) {
                hero.onTouchEnemy();
                isInjured = true;
                toBeRemoved.add(enemy);
            }
        }

        removeObjects(toBeRemoved);

        if (isInjured) {
            Hero deadHero = new Hero(getHero().getX(), getHero().getY() - 48, null);
            deadHero.setDead(true);
            deadHero.setJumping(false);
            deadHero.setFalling(false);
            map.setDeadHero(deadHero);
        }

        if (getRemainingLives() <= 0) {
            resetCurrentMap(windowController);
        }

        //camera
        if (hero.getX() <= windowController.getCameraLocation().getX() && hero.getVelocityX() < 0) {
            hero.setVelocityX(0);
            hero.setX(windowController.getCameraLocation().getX());
        }
    }

    private void checkEnemyCollisions() {
        ArrayList<Block> blocks = map.getAllBlocks();
        ArrayList<Enemy> enemies = map.getEnemies();
        for (Enemy enemy : enemies) {
            boolean standsOnBlock = false;

            for (Block block : blocks) {
                Rectangle enemyBounds = enemy.getLeftBounds();
                Rectangle brickBounds = block.getRightBounds();

                Rectangle enemyBottomBounds = enemy.getBottomBounds();
                Rectangle brickTopBounds = block.getTopBounds();

                if (enemy.getVelocityX() > 0) {
                    enemyBounds = enemy.getRightBounds();
                    brickBounds = block.getLeftBounds();
                }

                if (enemyBounds.intersects(brickBounds)) {
                    enemy.setVelocityX(-enemy.getVelocityX());
                }

                if (enemyBottomBounds.intersects(brickTopBounds)) {
                    enemy.setFalling(false);
                    enemy.setVelocityY(0);
                    enemy.setY(block.getY() - enemy.getDimension().height);
                    standsOnBlock = true;
                }
            }

            if (enemy.getY() + enemy.getDimension().height > map.getBottomBorder()) {
                enemy.setFalling(false);
                enemy.setVelocityY(0);
                enemy.setY(map.getBottomBorder() - enemy.getDimension().height);
            }

            if (!standsOnBlock && enemy.getY() < map.getBottomBorder()) {
                enemy.setFalling(true);
            }
        }
    }

    private void checkBulletContact() {
        ArrayList<Item> toBeRemoved = new ArrayList<>();

        for (Bullet bullet : map.getBullets()) {
            Rectangle bulletBounds = bullet.getBounds();

            for (Enemy enemy : map.getEnemies()) {
                Rectangle enemyBounds = enemy.getBounds();
                if (bulletBounds.intersects(enemyBounds)) {
                    toBeRemoved.add(enemy);
                    MusicPlayer.playStomp();
                    acquirePoints(50);
                    toBeRemoved.add(bullet);
                }
            }

            for (Block block : map.getAllBlocks()) {
                Rectangle blockBounds = block.getBounds();
                if (bulletBounds.intersects(blockBounds)) {
                    toBeRemoved.add(bullet);
                }
            }
        }
        removeObjects(toBeRemoved);
    }

    private void checkAwardContact() {
        ArrayList<Item> toBeRemoved = new ArrayList<>();
        Hero hero = map.getHero();
        ArrayList<Award> awards = new ArrayList<>();
        Rectangle heroBounds = hero.getBounds();
        for (Award award : awards) {
            Rectangle awardBounds = award.getBounds();
            if (awardBounds.intersects(heroBounds)) {
                award.onTouch(hero);
                toBeRemoved.add((Item) award);
            }
        }
        removeObjects(toBeRemoved);
    }

    private void removeObjects(ArrayList<Item> items) {

        if (items == null)
            return;

        for (Item item : items) {
            if (item instanceof Bullet) {
                map.removeBullet((Bullet) item);
            } else if (item instanceof Enemy) {
                map.removeEnemy((Enemy) item);
            } else if (item instanceof Award) {
                map.removeAward((Award) item);
            }
        }
    }
    public void updateTime(){
        if(map != null)
            map.updateTime(1);
    }

    public int getRemainingTime() {
        return (int)map.getRemainingTime();
    }
}
