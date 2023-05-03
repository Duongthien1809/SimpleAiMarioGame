package GameX;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;


public class GameConsole extends JPanel {

    public static final String IMAGE_DIR = "images/";
    private final Dimension prefSize = new Dimension(1180, 780);

    private ImageIcon backgroundImage;
    private final String background = "sky.jpg";

    private boolean gameOver = false;
    private Timer t;

    private void createGameObjects() {
        // hier werden wir später die Spielobjekte erzeugen
    }

    public void setBackgroundImage() {
        String imagePath = IMAGE_DIR + background;
        URL imageURL = getClass().getResource(imagePath);
        backgroundImage = new ImageIcon(imageURL);
    }

    private void initGame () {
        setBackgroundImage();
        repaint();
        //createGameObjects();
    } @Override
    public void paintComponent (Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        backgroundImage.paintIcon(null, g, 0, 0);
    }

    private void startGame() {
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
        setGameOver(false);
        createGameObjects();
        startGame();
    }

    private void endGame() {
        setGameOver(true);
        pauseGame();
    }

    public GameConsole() {
        setFocusable(true);
        setPreferredSize(prefSize);

        initGame();
        //startGame();
    }
}
