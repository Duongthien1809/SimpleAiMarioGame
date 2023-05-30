package control;

import model.block.Magic;
import model.enemy.Enemy;
import model.enemy.Mushroom;
import model.hero.Bullet;
import model.hero.Hero;
import model.Map;
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
    private ArrayList<Enemy> enemies = new ArrayList<>();
    private ArrayList<Award> awards = new ArrayList<>();

    private BufferedImage normalBlock, magicBlock, emptyBlock, groundBlock;
    private BufferedImage x;
    private BufferedImage mushroomLeft, mushroomRight;
    private Camera camera;
    private ArrayList<Block> blocks;

    private ImageLoader imageLoader;
    private Map map;
    private KeyController keyController;
    private MapController mapController;
    private SoundController soundController;

    public WindowController() {
        this.imageLoader = new ImageLoader();
        this.blocks = new ArrayList<>();
        BufferedImage[] leftFrames = imageLoader.getHeroLeftFrames();
        BufferedImage[] rightFrames = imageLoader.getHeroRightFrames();
        Animation heroAnimation = new Animation(leftFrames, rightFrames);
        this.hero = new Hero(0, prefSize.height - 48 * 2, heroAnimation, imageLoader.getSpriteSubImage(3, 4, 24, 24));
        this.gameX = new GameX(hero);
        this.gameX.setWindowController(this);

        this.map = new Map(hero);

        this.camera = new Camera();
        this.keyController = new KeyController(gameX);
        this.mapController = new MapController(map);
        this.soundController = new SoundController();
        //createGameObjects();
//        setFocusable(true);
        setPreferredSize(prefSize);

        initGame();
        startGame();
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

        g.setFont(new Font(Font.MONOSPACED, Font.BOLD, 19));
        g.setColor(Color.BLACK);
        g.drawString("Time:" + counter, prefSize.width - 100, 20);

        for (Block block : blocks) {
            block.draw(g2);
        }
        //mapController.drawMap(g2);
        hero.draw(g2);
        g2.dispose();
    }

    public void updateLocations() {
        mapController.updateLocations();
        updateCamera();
        checkCollisions();
        for (Enemy enemy : enemies) {
            enemy.updateLocation();
        }
        for (Bullet bullet : bullets) {
            bullet.updateLocation();
        }
    }

    public void render() {
        repaint();
    }

    public KeyController getKeyController() {
        return keyController;
    }

    public void checkCollisions() {
        mapController.checkCollisions(this);
    }

    private void updateCamera() {
        Hero hero = mapController.getHero();
        double heroVelocityX = hero.getVelocityX();
        double shiftAmount = 0;

        if (heroVelocityX > 0 && hero.getX() - 600 > camera.getX()) {
            shiftAmount = heroVelocityX;
        }

        camera.moveCam(shiftAmount, 0);
    }

    public Point getCameraLocation() {
        return new Point((int) camera.getX(), (int) camera.getY());
    }

    public void resetCamera() {
        camera = new Camera();
        soundController.restartBackground();
    }

    public ImageLoader getImageLoader() {
        return imageLoader;
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
    //todo: diese kann danach um map zu erzeugen momentan funktioniert es noch leider nicht.
    private void createMap(String path, Hero hero) {
        boolean loaded = mapController.createMap(imageLoader, path, hero);
        if (loaded) {
            gameX.setStatus(Status.RUNNING);
            soundController.restartBackground();
        } else
            gameX.setStatus(Status.START_SCREEN);
    }

    public static final String IMAGE_DIR = "../media/";
    private final Dimension prefSize = new Dimension(960, 576);

    private ImageIcon backgroundImage;
    private final String background = "background.png";
    private final int bottomBorder = prefSize.height - 48;
    private boolean gameOver = false;
    private int counter = 500;
    private Timer t;
    private ArrayList<Bullet> bullets = new ArrayList<>();

    public void shoot() {
        Bullet bullet = hero.shoot(hero.isTowardsRight(), hero.getX(), hero.getY() - 36);
        if (bullet != null) {
            bullets.add(bullet);
        }
    }

    private void createGameObjects() {
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
    }
    private Award generateAward(double x, double y){
        return new X(x, y, this.x, 0);
    }
    public void setMapBackground() {
        String imagePath = IMAGE_DIR + background;
        URL imageURL = getClass().getResource(imagePath);
        backgroundImage = new ImageIcon(imageURL);
    }

    private void initGame() {
        //setBackgroundImage();
        setMapBackground();
        createMap("Map 1.png", hero);
        repaint();
        t = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                countDown();
            }
        });
        //repaint();
        //createGameObjects();
    }

    private void startGame() {
        if (isRunning) return;

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
        counter = 500;
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
        if (counter == 0) endGame();
        repaint();
    }
}
