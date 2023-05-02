package GameX;
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JPanel;

import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

public class GameWindow extends JFrame{

    public GameWindow() {

        JPanel testPanel = new JPanel();
        testPanel.setPreferredSize(new Dimension(600, 400));
        createMenu();

        add(testPanel);
        pack();

        setTitle("GameX - Das Spiel, welches noch keinen Namen hat");
        setLocation(10, 10);
        setResizable(false);

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
    }

    private void addFileMenuItems(JMenu fileMenu) {

        JMenuItem quitItem = new JMenuItem("Quit");
        fileMenu.add(quitItem);
    }

}
