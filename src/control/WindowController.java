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
    /**
     * von Weijie und Thien Atributte
     */
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
    private ArrayList<Bullet> bullets = new ArrayList<>();
    private MapController mapController;
    private Camera camera;
    /**
     * von Leni Attribute
     */
    public static final String IMAGE_DIR = "../images/";
    private final Dimension prefSize = new Dimension(960, 576);
    private final int bottomBorder = prefSize.height - 48;

    private ImageIcon backgroundImage;
    private final String background = "sky.jpg";

    private boolean gameOver = false;
    private int counter = 3600;
    private Timer t;
    private boolean isRunning;
    private Thread thread;


    public WindowController() {
        this.hero = new Hero(0, prefSize.height - 48 * 2);//, heroAnimation, imageLoader.getSpriteSubImage(3, 4, 24, 24)
        this.gameX = new GameX(hero);
        this.gameX.setWindowController(this);
        this.camera = new Camera();
        this.imageLoader = new ImageLoader();
        this.keyController = new KeyController(gameX);
        this.mapController = new MapController();
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
        // backgroundImage.paintIcon(null, g, 0, 0);

        g.setFont(new Font(Font.MONOSPACED, Font.BOLD, 19));
        g.setColor(Color.BLACK);
        g.drawString("Time:" + counter, prefSize.width - 100, 20);

//        for (Block block : blocks) {
//            block.draw(g2);
//        }
//
//        for (Award award : awards) {
//            if (award instanceof X) {
//                ((X) award).draw(g2);
//            }
//        }
//
//        for (Enemy enemy : enemies) {
//            enemy.draw(g2);
//        }
//
//        for (Bullet bullet : bullets) {
//            bullet.draw(g2);
//        }
//
//        hero.draw(g2);
//        if (injuredHero != null) {
//            injuredHero.draw(g2);
//            injuredHero.setY(injuredHero.getY() - 2);
//            if (injuredHero.getY() < 0) {
//                injuredHero = null;
//            }
//        }
        mapController.drawMap(g2);
        g2.dispose();
    }

    private void gameLoop() {
        updateLocations();
        if (isGameOver()) {
            gameX.setStatus(Status.GAME_OVER);
        }

        int missionPassed = passMission();
        if (missionPassed > -1) {
            mapController.acquirePoints(missionPassed);
            //setGameStatus(GameStatus.MISSION_PASSED);
        } else if (mapController.endLevel())
            gameX.setStatus(Status.MISSION_PASSED);
    }

    private int passMission() {
        return mapController.passMission();
    }

    public void updateLocations() {
        hero.updateLocation();
        mapController.checkCollisions(this);
        mapController.updateLocations();
        updateCamera();
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

    private void createMap(String path) {
        boolean loaded = mapController.createMap(imageLoader, path);
        if (loaded) {
            gameX.setStatus(Status.RUNNING);
//            soundManager.restartBackground();
        } else
            gameX.setStatus(Status.START_SCREEN);
    }


    public void render() {
        repaint();
    }

    public KeyController getKeyController() {
        return keyController;
    }

    public void shoot() {
        Bullet bullet = hero.shoot(hero.isTowardsRight(), hero.getX(), hero.getY() - 36);
        if (bullet != null) {
            bullets.add(bullet);
        }
    }

    private Award generateAward(double x, double y) {
        return new X(x, y, this.x, 0);
    }


    //    private void createGameObjects() {
//        // hier werden wir später die Spielobjekte erzeugen
//        this.groundBlock = imageLoader.getSpriteSubImage(2, 2, 48, 48);
//        for (int i = 0; i <= prefSize.width; i += 48) {
//            blocks.add(new Ground(i, bottomBorder, this.groundBlock));
//        }
//
//        //two normal blocks for testing jumping
//        this.normalBlock = imageLoader.getSpriteSubImage(1, 1, 48, 48);
//        blocks.add(new Normal(48 * 2, prefSize.height - 48 * 3, this.normalBlock));
//
//        this.magicBlock = imageLoader.getSpriteSubImage(2, 1, 48, 48);
//        this.emptyBlock = imageLoader.getSpriteSubImage(1, 2, 48, 48);
//        this.x = imageLoader.getSpriteSubImage(1, 5, 48, 48);
//        Award award = generateAward(48 * 6, prefSize.height - 48 * 5);
//        awards.add(award);
//        blocks.add(new Magic(48 * 6, prefSize.height - 48 * 4, this.magicBlock, this.emptyBlock, award));
//
//        this.mushroomLeft = imageLoader.getSpriteSubImage(2, 4, 48, 48);
//        this.mushroomRight = imageLoader.getSpriteSubImage(5, 4, 48, 48);
//        //two normal blocks for testing enemy interaction
//        blocks.add(new Normal(48 * 9, prefSize.height - 48 * 2, this.normalBlock));
//        enemies.add(new Mushroom(48 * 10, prefSize.height - 48 * 2, this.mushroomLeft, this.mushroomRight));
//        blocks.add(new Normal(48 * 15, prefSize.height - 48 * 2, this.normalBlock));
//    }
    public ImageLoader getImageLoader() {
        return imageLoader;
    }

    public void resetCamera() {
        camera = new Camera();
//        soundManager.restartBackground();
    }

    public Camera getCamera() {
        return camera;
    }

    private void initGame() {
        repaint();
        createMap("Map 1.png");
        t = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                countDown();
            }
        });
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

    public Point getCameraLocation() {
        return new Point((int) camera.getX(), (int) camera.getY());
    }

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
                    gameLoop();
                }
                delta--;
            }
            render();

            if (gameX.getStatus() != Status.RUNNING) {
                timer = System.currentTimeMillis();
            }

            if (System.currentTimeMillis() - timer > 1000) {
                timer += 1000;
                mapController.updateTime();
            }
        }
    }
}
