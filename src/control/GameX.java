package control;

import model.Hero;

public class GameX {
    private Status status = Status.RUNNING;
    private Hero hero;
    private WindowController windowController;

    public GameX(Hero hero) {
        this.hero = hero;
    }

    public Status getStatus() {
        return status;
    }

    public void setWindowController(WindowController windowController) {
        this.windowController = windowController;
    }

    public void receiveInput(Key key) {
        if (status == Status.RUNNING) {
            if (key == Key.JUMP) {
                hero.jump();
            } else if (key == Key.M_RIGHT) {
                hero.move(true);
            } else if (key == Key.M_LEFT) {
                hero.move(false);
            } else if (key == Key.ACTION_COMPLETED) {
                hero.setVelocityX(0);
            }
        }
        windowController.updateLocations();
        windowController.render();
    }
}