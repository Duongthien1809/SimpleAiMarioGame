package control;

import model.Item;
import model.block.Magic;
import model.enemy.Enemy;
import model.enemy.Mushroom;
import model.hero.Bullet;
import model.hero.Hero;
import model.block.Block;
import model.block.Ground;
import model.block.Normal;
import model.prize.Award;
import model.prize.X;
import view.Animation;
import view.ImageLoader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.ArrayList;

public class WindowController extends JPanel {
    private GameX gameX;
    private Hero hero;
    private Animation heroAnimation;
    private Hero injuredHero;

    private ArrayList<Block> blocks = new ArrayList<>();
    private ArrayList<Enemy> enemies = new ArrayList<>();

    private ArrayList<Award> awards = new ArrayList<>();

    private ImageLoader imageLoader = new ImageLoader();

    private KeyController keyController;

    private BufferedImage normalBlock, magicBlock, emptyBlock, groundBlock;
    private BufferedImage x;
    private BufferedImage mushroomLeft, mushroomRight;



    public WindowController() {
        BufferedImage[] leftFrames = imageLoader.getHeroLeftFrames();
        BufferedImage[] rightFrames = imageLoader.getHeroRightFrames();
        this.heroAnimation = new Animation(leftFrames, rightFrames);

        this.hero = new Hero(0, prefSize.height - 48 * 2, heroAnimation, imageLoader.getSpriteSubImage(3, 4, 24, 24));
        this.gameX = new GameX(hero);
        this.gameX.setWindowController(this);

        this.keyController = new KeyController(gameX);

        this.groundBlock = imageLoader.getSpriteSubImage(2, 2, 48, 48);
        for (int i = 0; i <= prefSize.width; i += 48) {
            blocks.add(new Ground(i, bottomBorder, this.groundBlock));
        }

        //two normal blocks for testing jumping
        this.normalBlock = imageLoader.getSpriteSubImage(1, 1, 48, 48);
        blocks.add(new Normal(48 * 2, prefSize.height - 48 * 3, this.normalBlock));

        this.magicBlock = imageLoader.getSpriteSubImage(2, 1, 48, 48);
        this.emptyBlock = imageLoader.getSpriteSubImage(1, 2, 48, 48);
        this.x = imageLoader.getSpriteSubImage(1, 5, 48, 48);
        Award award = generateAward(48 * 6, prefSize.height - 48 * 5);
        awards.add(award);
        blocks.add(new Magic(48 * 6, prefSize.height - 48 * 4, this.magicBlock, this.emptyBlock, award));

        this.mushroomLeft = imageLoader.getSpriteSubImage(2, 4, 48, 48);
        this.mushroomRight = imageLoader.getSpriteSubImage(5, 4, 48, 48);
        //two normal blocks for testing enemy interaction
        blocks.add(new Normal(48 * 9, prefSize.height - 48 * 2, this.normalBlock));
        enemies.add(new Mushroom(48 * 10, prefSize.height - 48 * 2, this.mushroomLeft, this.mushroomRight));
        blocks.add(new Normal(48 * 15, prefSize.height - 48 * 2, this.normalBlock));
//        setFocusable(true);
        setPreferredSize(prefSize);

        initGame();
        startGame();
    }

    public void setWindowController(WindowController windowController){
        setGameX(windowController.getGameX());
        setHero(windowController.getHero());
        setBlocks(windowController.getBlocks());
        setImageLoader(windowController.getImageLoader());
        setKeyController(windowController.getKeyController());

        initGame();
        startGame();
    }

    public GameX getGameX(){
        return this.gameX;
    }

    public Hero getHero(){
        return this.hero;
    }

    public ArrayList<Block> getBlocks(){
        return this.blocks;
    }

    public ImageLoader getImageLoader(){
        return this.imageLoader;
    }

    public void setBlocks(final ArrayList<Block> blocks){
        this.blocks = blocks;
    }

    public void setGameX(final GameX gameX){
        this.gameX = gameX;
    }

    public void setHero(final Hero hero){
        this.hero = hero;
    }

    public void setImageLoader(final ImageLoader imageLoader){
        this.imageLoader = imageLoader;
    }

    public void setKeyController(final KeyController keyController){
        this.keyController = keyController;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g.create();
        Status gameStatus = gameX.getStatus();
        switch (gameStatus) {

        }

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (backgroundImage != null) {
            backgroundImage.paintIcon(this, g, 0, 0);
        }
        // backgroundImage.paintIcon(null, g, 0, 0);

        g.setFont(new Font(Font.MONOSPACED, Font.BOLD, 19));
        g.setColor(Color.BLACK);
        g.drawString("Time:" + counter, prefSize.width - 100, 20);

        for (Block block : blocks) {
            block.draw(g2);
        }

        for (Award award : awards){
            if (award instanceof X){
                ((X) award).draw(g2);
            }
        }

        for (Enemy enemy : enemies) {
            enemy.draw(g2);
        }

        for (Bullet bullet : bullets) {
            bullet.draw(g2);
        }

        hero.draw(g2);
        if (injuredHero != null){
            injuredHero.draw(g2);
            injuredHero.setY(injuredHero.getY() - 2);
            if (injuredHero.getY() < 0){
                injuredHero = null;
            }
        }
        g2.dispose();
    }

    public void updateLocations() {
        hero.updateLocation();
        for (Enemy enemy : enemies) {
            enemy.updateLocation();
        }
        for (Bullet bullet : bullets) {
            bullet.updateLocation();
        }
        checkCollisions();
    }

    public void render() {
        repaint();
    }

    public KeyController getKeyController() {
        return keyController;
    }

    private void removeObjects(ArrayList<Item> items){
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

    public void checkCollisions() {
        checkBottomCollisions();
        checkTopCollisions();
        checkHeroHorizontalCollision();
        checkEnemyCollisions();
        checkBulletContact();
        checkAwardContact();
    }

    private void checkBottomCollisions() {
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
                toBeRemoved.add(enemy);
            }
        }

        if (hero.getY() + hero.getDimension().height >= bottomBorder) {
            hero.setY(bottomBorder - hero.getDimension().height);
            hero.setFalling(false);
            hero.setVelocityY(0);
        }

        removeObjects(toBeRemoved);
    }

    private void checkTopCollisions() {
        Rectangle marioTopBounds = hero.getTopBounds();

        for (Block block : blocks) {
            Rectangle brickBottomBounds = block.getBottomBounds();
            if (marioTopBounds.intersects(brickBottomBounds)) {
                hero.setVelocityY(0);
                hero.setY(block.getY() + block.getDimension().height);
                Award award = block.reveal();
                if (award != null){
                    awards.add(award);
                }
            }
        }
    }

    private void checkHeroHorizontalCollision() {
        ArrayList<Item> toBeRemoved = new ArrayList<>();
        boolean towardsRight = hero.isTowardsRight();

        Rectangle marioBounds = towardsRight ? hero.getRightBounds() : hero.getLeftBounds();

        for (Block block : blocks) {
            Rectangle brickBounds = !towardsRight ? block.getRightBounds() : block.getLeftBounds();
            if (marioBounds.intersects(brickBounds)) {
                hero.setVelocityX(0);
                if (towardsRight)
                    hero.setX(block.getX() - hero.getDimension().width);
                else
                    hero.setX(block.getX() + block.getDimension().width);
            }
        }

        //enemy
        boolean isInjured = false;
        for(Enemy enemy : enemies){
            Rectangle enemyBounds = !towardsRight ? enemy.getRightBounds() : enemy.getLeftBounds();
            if (marioBounds.intersects(enemyBounds)) {
                isInjured = hero.onTouchEnemy();
                toBeRemoved.add(enemy);
            }
        }
        removeObjects(toBeRemoved);

        if (isInjured){
            injuredHero = new Hero(hero.getX(), hero.getY() - 48, heroAnimation, imageLoader.getSpriteSubImage(3, 4, 24, 24));
            injuredHero.setJumping(false);
            injuredHero.setFalling(false);
            injuredHero.setVelocityY(-3);
        }
        //camera
    }

    private void checkEnemyCollisions() {
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

    private void checkBulletContact() {
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

    private Award generateAward(double x, double y){
        return new X(x, y, this.x, 0);
    }

    private void checkAwardContact() {
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

    private boolean isRunning;

    public void play() {
        long lastTime = System.nanoTime();
        double amountOfTicks = 60.0;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;
        long timer = System.currentTimeMillis();

        while (isRunning) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            while (delta >= 1) {
                if (gameX.getStatus() == Status.RUNNING) {
                    updateLocations();
                }
                delta--;
            }
            render();

            if (gameX.getStatus() != Status.RUNNING) {
                timer = System.currentTimeMillis();
            }

            if (System.currentTimeMillis() - timer > 1000) {
                timer += 1000;
//                mapManager.updateTime();
            }
        }
    }

    private ArrayList<Bullet> bullets = new ArrayList<>();

    public void shoot() {
        Bullet bullet = hero.shoot(hero.isTowardsRight(), hero.getX(), hero.getY() - 36);
        if (bullet != null) {
            bullets.add(bullet);
        }
    }

    public static final String IMAGE_DIR = "../images/";
    private final Dimension prefSize = new Dimension(960, 576);
    private final int bottomBorder = prefSize.height - 48;

    private ImageIcon backgroundImage;
    private final String background = "sky.jpg";

    private boolean gameOver = false;
    private int counter = 3600;
    private Timer t;


    private void createGameObjects() {
        // hier werden wir später die Spielobjekte erzeugen
    }

    public void setBackgroundImage() {
        String imagePath = IMAGE_DIR + background;
        URL imageURL = getClass().getResource(imagePath);
        backgroundImage = new ImageIcon(imageURL);
    }

    private void initGame() {
        setBackgroundImage();
        repaint();
        t = new Timer(60, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                countDown();
            }
        });
        //repaint();
        //createGameObjects();
    }

    private void startGame() {
        if (isRunning)
            return;

        isRunning = true;

        t.start();
    }

    public void pauseGame() {
        t.stop();
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    public void continueGame() {
        if (!isGameOver()) t.start();
    }

    public void restartGame() {
        counter = 0;
        setGameOver(false);
        //createGameObjects();
        startGame();
    }

    private void endGame() {
        setGameOver(true);
        pauseGame();
    }

    public void countDown() {
        counter--;
        if (counter == 0)
            endGame();
        repaint();
    }
}
