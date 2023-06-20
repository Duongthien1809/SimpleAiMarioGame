package control;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.Serializable;


public class KeyController implements KeyListener{
    private GameX gameX;

    public KeyController(GameX gameX) {
        this.gameX = gameX;
    }

    @Override
    public void keyPressed(KeyEvent event) {
        int keyCode = event.getKeyCode();
        Status status = gameX.getStatus();
        Key currentKey = Key.NO_ACTION;

        if (keyCode == KeyEvent.VK_UP) {
            if (status == Status.START_SCREEN || status == Status.MAP_SELECTION)
                currentKey = Key.GO_UP;
            else
                currentKey = Key.JUMP;
        } else if (keyCode == KeyEvent.VK_DOWN) {
            if (status == Status.START_SCREEN || status == Status.MAP_SELECTION)
                currentKey = Key.GO_DOWN;
        } else if (keyCode == KeyEvent.VK_RIGHT) {
            currentKey = Key.M_RIGHT;
        } else if (keyCode == KeyEvent.VK_LEFT) {
            currentKey = Key.M_LEFT;
        } else if (keyCode == KeyEvent.VK_ENTER) {
            currentKey = Key.SELECT;
        } else if (keyCode == KeyEvent.VK_ESCAPE) {
            if (status == Status.RUNNING || status == Status.PAUSED)
                currentKey = Key.PAUSE_RESUME;
            else
                currentKey = Key.GO_TO_START_SCREEN;

        } else if (keyCode == KeyEvent.VK_SPACE) {
            currentKey = Key.SHOOT;
        }

        notifyInput(currentKey);
    }

    @Override
    public void keyReleased(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.VK_RIGHT || event.getKeyCode() == KeyEvent.VK_LEFT)
            notifyInput(Key.ACTION_COMPLETED);
    }

    private void notifyInput(Key key) {
        if (key != Key.NO_ACTION)
            gameX.receiveInput(key);
    }

    @Override
    public void keyTyped(KeyEvent event) {

    }
}

