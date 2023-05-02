package GameX;
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class GameWindow extends JFrame{

    public GameWindow() {

        JPanel testPanel = new JPanel();
        testPanel.setPreferredSize(new Dimension(600, 400));

        add(testPanel);
        pack();

        setTitle("GameX - Das Spiel, welches noch keinen Namen hat");
        setLocation(10, 10);
        setResizable(true);

        setVisible(true);
    }

}
