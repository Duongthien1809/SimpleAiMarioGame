import control.GameX;
import control.KeyController;
import control.WindowController;
import model.Hero;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class App {
    private final static int WIDTH = 600, HEIGHT = 400;
    private static GameWindow gameWindow = new GameWindow();

    public static void main(String[] args) {
//        Hero hero = new Hero(0, 400 - 48);
        Hero hero = new Hero(0, 0);
        GameX gameX = new GameX(hero);
        KeyController keyController = new KeyController(gameX);
        WindowController windowController = new WindowController(gameX, hero, WIDTH, HEIGHT);
        gameX.setWindowController(windowController);
        JFrame frame = new JFrame("GameX");

        createMenu(frame);
        registerWindowListener(frame);

        frame.add(windowController);
        frame.addKeyListener(keyController);
        frame.pack();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setResizable(true);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void createMenu(JFrame frame) {

        JMenuBar menuBar = new JMenuBar();
        frame.setJMenuBar(menuBar);

        JMenu fileMenu = new JMenu("File");
        JMenu gameMenu = new JMenu("Game");
        JMenu saveMenu = new JMenu("Save");

        menuBar.add(fileMenu);
        menuBar.add(gameMenu);
        menuBar.add(saveMenu);

        addFileMenuItems(fileMenu);
        menuBar.add(fileMenu);
        menuBar.add(gameMenu);
        menuBar.add(saveMenu);

        addFileMenuItems(fileMenu);
        gameWindow.addGameMenuItems(gameMenu);
        gameWindow.addSaveMenuItems(saveMenu);
    }

    public static void addFileMenuItems(JMenu fileMenu) {

        JMenuItem quitItem = new JMenuItem("Quit");
        fileMenu.add(quitItem);
        quitItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
    }

    public static void registerWindowListener(JFrame frame) {
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }

            @Override
            public void windowDeactivated(WindowEvent e) {
                // pausieren
            }

            @Override
            public void windowActivated(WindowEvent e) {
                //  Spiel wieder fortsetzen
            }
        });
    }
}
