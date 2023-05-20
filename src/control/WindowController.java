package control;

import model.Hero;
import model.block.Block;
import model.block.Ground;
import model.block.Normal;
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

    private ArrayList<Block> blocks = new ArrayList<>();

    private ImageLoader imageLoader = new ImageLoader();

    private KeyController keyController;

    public WindowController() {
        BufferedImage[] leftFrames = imageLoader.getHeroLeftFrames();
        BufferedImage[] rightFrames = imageLoader.getHeroRightFrames();
        Animation heroAnimation = new Animation(leftFrames, rightFrames);
        this.hero = new Hero(0, prefSize.height - 48 * 2, heroAnimation);

        this.gameX = new GameX(hero);
        this.gameX.setWindowController(this);

        this.keyController = new KeyController(gameX);

        for (int i = 0; i <= prefSize.width; i += 48) {
            blocks.add(new Ground(i, prefSize.height - 48, imageLoader.getSpriteSubImage(2, 2, 48, 48)));
        }

        //two normal blocks for test
        blocks.add(new Normal(48 * 2, prefSize.height - 48 * 3, imageLoader.getSpriteSubImage(1, 1, 48, 48)));
        blocks.add(new Normal(48 * 6, prefSize.height - 48 * 4, imageLoader.getSpriteSubImage(1, 1, 48, 48)));

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
        // backgroundImage.paintIcon(null, g, 0, 0);

        g.setFont(new Font(Font.MONOSPACED, Font.BOLD, 19));
        g.setColor(Color.BLACK);
        g.drawString("Time:" + counter, prefSize.width - 100, 20);

        for (Block block : blocks){
            block.draw(g2);
        }

        hero.draw(g2);
        g2.dispose();
    }

    public void updateLocations() {
        hero.updateLocation();
        checkCollisions();
    }

    public void render() {
        repaint();
    }

    public KeyController getKeyController() {
        return keyController;
    }

    public void checkCollisions() {
        checkBottomCollisions();
        checkTopCollisions();
        checkHeroHorizontalCollision();
    }

    private void checkBottomCollisions() {
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

    private void checkTopCollisions() {
        Rectangle marioTopBounds = hero.getTopBounds();

        for (Block block : blocks) {
            Rectangle brickBottomBounds = block.getBottomBounds();
            if (marioTopBounds.intersects(brickBottomBounds)) {
                hero.setVelocityY(0);
                hero.setY(block.getY() + block.getDimension().height);
            }
        }
    }

    private void checkHeroHorizontalCollision(){
        boolean towardsRight = hero.isTowardsRight();

        Rectangle marioBounds = towardsRight ? hero.getRightBounds() : hero.getLeftBounds();

        for (Block block : blocks) {
            Rectangle brickBounds = !towardsRight ? block.getRightBounds() : block.getLeftBounds();
            if (marioBounds.intersects(brickBounds)) {
                hero.setVelocityX(0);
                if(towardsRight)
                    hero.setX(block.getX() - hero.getDimension().width);
                else
                    hero.setX(block.getX() + block.getDimension().width);
            }
        }

        //enemy

        //camera
    }

    private boolean isRunning;
    public void play(){
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

            if(gameX.getStatus() != Status.RUNNING) {
                timer = System.currentTimeMillis();
            }

            if (System.currentTimeMillis() - timer > 1000) {
                timer += 1000;
//                mapManager.updateTime();
            }
        }
    }

    public static final String IMAGE_DIR = "../images/";
    private final Dimension prefSize = new Dimension(960, 576);

    private ImageIcon backgroundImage;
    private final String background = "sky.jpg";

    private boolean gameOver = false;
    private int counter = 0;
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
        counter++;
        if (counter > 240)
            endGame();
        repaint();
    }
}
