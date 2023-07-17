public class App {
    public static void main(String[] args) {
        GameRunThread gameRunThread = new GameRunThread();
        gameRunThread.start();

        AIPlayerThread aiPlayerThread = new AIPlayerThread(gameRunThread);
        aiPlayerThread.start();
    }
}