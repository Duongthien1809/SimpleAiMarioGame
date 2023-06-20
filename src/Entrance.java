public class Entrance {
    public static void main(String[] args) {
        RunThread runThread = new RunThread();
        InputThread inputThread = new InputThread();
        runThread.start();
        inputThread.start();
         }
}
