import control.WindowController;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class GameRunThread extends Thread {
    @Override
    public void run() {
        play();
    }

    private static WindowController windowController;
    public int getScore(){
        if (windowController != null){
            return windowController.getScore();
        }
        return 0;
    }

    public void resetGame(){
        windowController.resetGame();
    }

    public static void play() {
        windowController = new WindowController();
        JFrame frame = new JFrame("GameX");

        createMenu(frame, windowController);
//        registerWindowListener(frame);

        frame.add(windowController);
        frame.addKeyListener(windowController.getKeyController());
        frame.pack();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setResizable(true);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        windowController.play();
    }

    public static void createMenu(JFrame frame, WindowController windowController) {

        JMenuBar menuBar = new JMenuBar();
        frame.setJMenuBar(menuBar);

        JMenu fileMenu = new JMenu("File");
        JMenu gameMenu = new JMenu("Game");
//        JMenu saveMenu = new JMenu("Save");

        menuBar.add(fileMenu);
        menuBar.add(gameMenu);
//        menuBar.add(saveMenu);

        addFileMenuItems(fileMenu);
        addGameMenuItems(gameMenu, windowController);
//        addSaveMenuItems(saveMenu);
    }

/*    public static void registerWindowListener(JFrame frame) {
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
    }*/

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

    public static void addGameMenuItems(JMenu gameMenu, WindowController windowController) {
        JMenuItem pauseItem = new JMenuItem("pause");
        gameMenu.add(pauseItem);
        pauseItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                windowController.pauseGame();
            }
        });

        JMenuItem continueItem = new JMenuItem("continue");
        gameMenu.add(continueItem);
        continueItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                windowController.continueGame();
            }
        });

        JMenuItem restartItem = new JMenuItem("restart");
        gameMenu.add(restartItem);
        restartItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                windowController.restartGame();
            }
        });
    }

/*    public static void addSaveMenuItems(JMenu saveMenu) {
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
    }*/
}
