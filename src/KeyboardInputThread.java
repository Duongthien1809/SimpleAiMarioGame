import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Random;

public class KeyboardInputThread extends Thread {
    @Override
    public void run() {
        int count = 1000;
//         int[] keyEvents = new int[]{KeyEvent.VK_RIGHT, KeyEvent.VK_UP, KeyEvent.VK_SPACE};
        int[] keyEvents = new int[]{KeyEvent.VK_RIGHT, KeyEvent.VK_UP};
        Random random = new Random();
        try {
            while (true) {
                Robot robot = new Robot();

/*                int keyEvent = keyEvents[random.nextInt(2)];
                robot.keyPress(keyEvent);
                robot.keyRelease(keyEvent);
                robot.delay(50);*/

                for (int i = 0; i < keyEvents.length; i++) {
                    robot.keyPress(keyEvents[i]);
                    robot.delay(50);
                }

                for (int i = keyEvents.length - 1; i >= 0; i--) {
                    robot.keyRelease(keyEvents[i]);
                    robot.delay(50);
                }
            }
        } catch (AWTException e) {
            throw new RuntimeException(e);
        }
    }

    public void log(){

    }
}