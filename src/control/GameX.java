package control;

import model.hero.Hero;
import view.ImageLoader;

public class GameX {
    private Status status = Status.RUNNING;
    private Hero hero;
    private WindowController windowController;

    public Status getStatus() {
        return status;
    }

    public void setWindowController(WindowController windowController) {
        this.windowController = windowController;
    }

    public void receiveInput(Key key) {
        System.out.println(key);
        if (status == Status.RUNNING) {
            if (key == Key.JUMP) {
                hero.jump();
            } else if (key == Key.M_RIGHT) {
                hero.move(true, windowController.getCamera());
            } else if (key == Key.M_LEFT) {
                hero.move(false, windowController.getCamera());
            } else if (key == Key.ACTION_COMPLETED) {
                hero.setVelocityX(0);
            } else if (key == Key.SHOOT) {
                windowController.shoot();
            }
        }
        windowController.updateLocations();
        windowController.render();
    }
    public void setStatus(Status gameStatus) {
        this.status = gameStatus;
    }

    public void setHero(Hero hero) {
        this.hero = hero;
    }
}