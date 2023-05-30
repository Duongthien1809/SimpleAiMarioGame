package control;

import model.enemy.Enemy;
import model.hero.Bullet;
import model.hero.Hero;
import model.Item;
import model.Map;
import model.block.Block;
import model.prize.Award;
import view.ImageLoader;

import java.awt.*;
import java.util.ArrayList;

public class MapController {
    private Map map;
    private final Dimension prefSize = new Dimension(960, 576);
    private double bottomBorder = 720 - 96;

    public MapController(Map map) {
        this.map = map;
    }

    public void updateLocations() {
        if (map == null)
            return;

        map.updateLocations();
    }

    public void resetCurrentMap(WindowController windowController) {
        Hero hero = map.getHero();
        hero.resetLocation();
        windowController.resetCamera();
        createMap(windowController.getImageLoader(), map.getPath(), hero);
        map.setHero(hero);
    }

    public boolean createMap(ImageLoader loader, String path, Hero hero) {
        MapCreator mapCreator = new MapCreator(loader);
        map = mapCreator.createMap("/maps/" + path, 400, hero);

        return map != null;
    }

    public void checkCollisions(WindowController windowController) {
        if (map == null) {
            return;
        }

        checkBottomCollisions(windowController);
        checkTopCollisions(windowController);
        checkHeroHorizontalCollision(windowController);
        checkEnemyCollisions();
        checkAwardContact();
        checkBulletContact();
    }
    private void checkAwardContact() {
        Hero hero = map.getHero();
        ArrayList<Award> awards = map.getAwards();
        ArrayList<Item> toBeRemoved = new ArrayList<>();

        Rectangle heroBounds = hero.getBounds();
        for(Award award : awards){
            Rectangle awardBounds = award.getBounds();
            if (awardBounds.intersects(heroBounds)) {
                award.onTouch();
                toBeRemoved.add((Item) award);
            }
        }

        removeObjects(toBeRemoved);
    }
    private void checkBulletContact() {
        ArrayList<Bullet> bullets = map.getBullets();
        ArrayList<Enemy> enemies = map.getEnemies();
        ArrayList<Block> blocks = map.getAllBlocks();
        ArrayList<Item> toBeRemoved = new ArrayList<>();

        for(Bullet bullet : bullets){
            Rectangle fireballBounds = bullet.getBounds();

            for(Enemy enemy : enemies){
                Rectangle enemyBounds = enemy.getBounds();
                if (fireballBounds.intersects(enemyBounds)) {
                    toBeRemoved.add(enemy);
                    toBeRemoved.add(bullet);
                }
            }

            for(Block block : blocks){
                Rectangle blockBounds = block.getBounds();
                if (fireballBounds.intersects(blockBounds)) {
                    toBeRemoved.add(bullet);
                }
            }

            if (bullet.getX() < 0 || bullet.getX() > prefSize.width){
                toBeRemoved.add(bullet);
            }
        }
        removeObjects(toBeRemoved);
    }
    private void checkHeroHorizontalCollision(WindowController windowController) {
        Hero hero = map.getHero();
        ArrayList<Block> blocks = map.getAllBlocks();
        ArrayList<Enemy> enemies = map.getEnemies();
        ArrayList<Item> toBeRemoved = new ArrayList<>();

        boolean herodies = false;
        boolean toRight = hero.isTowardsRight();

        Rectangle marioBounds = toRight ? hero.getRightBounds() : hero.getLeftBounds();

        for (Block block : blocks) {
            Rectangle blockBound = !toRight ? block.getRightBounds() : block.getLeftBounds();
            if (marioBounds.intersects(blockBound)) {
                hero.setVelocityX(0);
                if (toRight)
                    hero.setX(block.getX() - hero.getDimension().width);
                else
                    hero.setX(block.getX() + block.getDimension().width);
            }
        }
        //enemy
        boolean isInjured = false;
        for(Enemy enemy : enemies){
            Rectangle enemyBounds = !toRight ? enemy.getRightBounds() : enemy.getLeftBounds();
            if (marioBounds.intersects(enemyBounds)) {
                isInjured = hero.onTouchEnemy();
                toBeRemoved.add(enemy);
            }
        }
        removeObjects(toBeRemoved);

//        if (isInjured){
//            injuredHero = new Hero(hero.getX(), hero.getY() - 48, heroAnimation, imageLoader.getSpriteSubImage(3, 4, 24, 24));
//            injuredHero.setJumping(false);
//            injuredHero.setFalling(false);
//            injuredHero.setVelocityY(-3);
//        }
        //camera
        if (hero.getX() <= windowController.getCameraLocation().getX() && hero.getVelocityX() < 0) {
            hero.setVelocityX(0);
            hero.setX(windowController.getCameraLocation().getX());
        }

        if (herodies) {
            resetCurrentMap(windowController);
        }

    }
    private void checkEnemyCollisions() {
        ArrayList<Enemy> enemies = map.getEnemies();
        for (Enemy enemy : enemies) {
            boolean standsOnBlock = false;
            ArrayList<Block> blocks = map.getAllBlocks();
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

            if (enemy.getY() + enemy.getDimension().height > bottomBorder) {
                enemy.setFalling(false);
                enemy.setVelocityY(0);
                enemy.setY(bottomBorder - enemy.getDimension().height);
            }

            if (!standsOnBlock && enemy.getY() < bottomBorder) {
                enemy.setFalling(true);
            }
        }
    }
    private void checkTopCollisions(WindowController windowController) {
        Hero hero = map.getHero();
        Rectangle marioTopBounds = hero.getTopBounds();
        ArrayList<Block> blocks = map.getAllBlocks();

        for (Block block : blocks) {
            Rectangle blockBottomBounds = block.getBottomBounds();
            if (marioTopBounds.intersects(blockBottomBounds)) {
                hero.setVelocityY(0);
                hero.setY(block.getY() + block.getDimension().height);
            }
        }
    }

    private void checkBottomCollisions(WindowController windowController) {
        Hero hero = map.getHero();
        ArrayList<Block> blocks = map.getAllBlocks();
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

        if (hero.getY() + hero.getDimension().height >= prefSize.height - 48) {
            hero.setY((prefSize.height - 48) - hero.getDimension().height);
            hero.setFalling(false);
            hero.setVelocityY(0);
        }
    }
    public Hero getHero() {
        return map.getHero();
    }
    public void drawMap(Graphics2D g2){
        map.drawMap(g2);
    }
    private void removeObjects(ArrayList<Item> items){
        ArrayList<Bullet> bullets = map.getBullets();
        ArrayList<Enemy> enemies = map.getEnemies();
        ArrayList<Award> awards = map.getAwards();
        if(items == null)
            return;

        for(Item item : items){
            if(item instanceof Bullet){
                bullets.remove((Bullet)item);
            }
            else if(item instanceof Enemy){
                enemies.remove((Enemy)item);
            }
            else if(item instanceof Award){
                awards.remove((Award) item);
            }
        }
    }
}
