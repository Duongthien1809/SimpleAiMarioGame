package GameX;
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JPanel;

import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class GameWindow extends JFrame{

    private final GameConsole gameConsole;

    public GameWindow() {

        this.gameConsole = new GameConsole();
        createMenu();
        registerWindowListener();

        add(gameConsole);
        pack();

        setTitle("GameX - Das Spiel, dass noch keinen Namen hat");
        setLocation(10, 10);
        setResizable(true);

        setVisible(true);
    }

    private void createMenu() {

        JMenuBar menuBar = new JMenuBar();
        this.setJMenuBar(menuBar);

        JMenu fileMenu = new JMenu("File");
        JMenu gameMenu = new JMenu("Game");
        JMenu saveMenu = new JMenu("Save");

        menuBar.add(fileMenu);
        menuBar.add(gameMenu);
        menuBar.add(saveMenu);

        addFileMenuItems(fileMenu);
        addGameMenuItems(gameMenu);
        addSaveMenuItems(saveMenu);
    }

    private void addFileMenuItems(JMenu fileMenu) {

        JMenuItem quitItem = new JMenuItem("Quit");
        fileMenu.add(quitItem);
        quitItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

    }

    private void addGameMenuItems(JMenu gameMenu){
        JMenuItem pauseItem = new JMenuItem("pause");
        gameMenu.add(pauseItem);
        pauseItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameConsole.pauseGame();
            }
        });

        JMenuItem continueItem = new JMenuItem("continue");
        gameMenu.add(continueItem);
        continueItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameConsole.continueGame(); //
            }
        });

        JMenuItem restartItem = new JMenuItem("restart");
        gameMenu.add(restartItem);
        restartItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameConsole.restartGame();
            }
        });
    }
    private void addSaveMenuItems(JMenu saveMenu){
        JMenuItem save1 = new JMenuItem("state 1");
        saveMenu.add(save1);
        save1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // save state 1
            }
        });
        JMenuItem save2 = new JMenuItem("state 2");
        saveMenu.add(save2);
        save2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // save state 2
            }
        });

        JMenuItem save3 = new JMenuItem("state 3");
        saveMenu.add(save3);
        save3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // save state 3
            }
        });

    }

    private void registerWindowListener() {
        addWindowListener(new WindowAdapter() {
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
