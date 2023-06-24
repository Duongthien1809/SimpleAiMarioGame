import java.awt.*;
import java.awt.event.KeyEvent;

public class InputThread extends Thread{
    @Override
    public void run() {
        int[] keys = new int[]{KeyEvent.VK_UP, KeyEvent.VK_RIGHT};
        try {
            int count = 100;
            Robot robot = new Robot();
            while (true){
                for (int i = 0; i < keys.length; i++) {
                    robot.keyPress(keys[i]);
                    robot.delay(5);
                }
                for (int i = 0; i < keys.length; i++) {
                    robot.keyRelease(keys[i]);
                    robot.delay(5);
                }
            }
        } catch (AWTException e) {
            throw new RuntimeException(e);
        }
    }
}
