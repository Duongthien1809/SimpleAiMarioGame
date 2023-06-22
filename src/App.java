public class App {
    public static void main(String[] args) {
        GameRunThread gameRunThread = new GameRunThread();
        gameRunThread.start();
//        KeyboardInputThread keyboardInputThread = new KeyboardInputThread();
//        keyboardInputThread.start();
    }
}