import control.WindowController;
import io.FileIO;
import io.InstanceManager;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class App {
    private static InstanceManager instanceManager;
    private static WindowController windowController;
    private static FileIO fileSystem;
    public static void main(String[] args) {
        instanceManager = new InstanceManager();
        windowController = new WindowController();
        instanceManager.setWindowController(windowController);
        run(instanceManager.getWindowController());
    }

    public static void run(WindowController windowController){
        fileSystem = new FileIO(windowController);
        JFrame frame = new JFrame("GameX");

        createMenu(frame, windowController);
        registerWindowListener(frame);

        frame.add(windowController);
        frame.addKeyListener(windowController.getKeyController());
        frame.pack();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        windowController.play();
    }

    public static void createMenu(JFrame frame, WindowController windowController) {

        JMenuBar menuBar = new JMenuBar();
        frame.setJMenuBar(menuBar);

        JMenu fileMenu = new JMenu("File");
        JMenu gameMenu = new JMenu("Game");
        JMenu saveMenu = new JMenu("Save");
        JMenu loadMenu = new JMenu("Load");

        menuBar.add(fileMenu);
        menuBar.add(gameMenu);
        menuBar.add(saveMenu);
        menuBar.add(loadMenu);

        addFileMenuItems(fileMenu);
        addGameMenuItems(gameMenu, windowController);
        addSaveMenuItems(saveMenu);
        addLoadMenuItems(loadMenu);
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

    public static void addSaveMenuItems(JMenu saveMenu) {
        JMenuItem save1 = new JMenuItem("state 1");
        saveMenu.add(save1);
        save1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fileSystem.saveFileJBP("state1.xml");
            }
        });
        JMenuItem save2 = new JMenuItem("state 2");
        saveMenu.add(save2);
        save2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fileSystem.saveFileJBP("state2.xml");
            }
        });
    }

    public static void addLoadMenuItems(JMenu loadMenu){
        JMenuItem save1 = new JMenuItem("state 1");
        loadMenu.add(save1);
        save1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                instanceManager.setWindowController(fileSystem.loadFileJBP("state1.xml"));
                run(instanceManager.getWindowController());
            }
        });

        JMenuItem save2 = new JMenuItem("state 2");
        loadMenu.add(save2);
        save2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                instanceManager.setWindowController(fileSystem.loadFileJBP("state2.xml"));
                run(instanceManager.getWindowController());
            }
        });
    }
}
