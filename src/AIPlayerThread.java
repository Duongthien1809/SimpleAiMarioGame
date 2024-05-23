import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.*;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;

public class AIPlayerThread extends Thread {
    private int stepNum = 100;
    private int period = 100;
    private final int[] keyEvents = new int[]{KeyEvent.VK_RIGHT, KeyEvent.VK_LEFT, KeyEvent.VK_UP, KeyEvent.VK_SPACE};
    private double[][] transformMatrix = new double[keyEvents.length][keyEvents.length];
    private double[][] countMatrix = new double[keyEvents.length][keyEvents.length];

    private Random random = new Random();

    private GameRunThread gameRunThread;

    public AIPlayerThread(GameRunThread gameRunThread) {
        this.gameRunThread = gameRunThread;
    }

    @Override
    public void run() {
        trainModel();
//        testModel();
    }

    private void testModel() {
        loadTransformMatrix();

        int curKeyIndex = 0;//initial action VK_RIGHT
        int nextKeyIndex;

        Robot robot;
        try {
            robot = new Robot();
            while (true) {
                nextKeyIndex = getNextKeyIndex(curKeyIndex);
                updateCountMatrix(curKeyIndex, nextKeyIndex);
//                System.out.println(nextKeyIndex);
                robot.keyPress(keyEvents[curKeyIndex]);
                robot.delay(50);
                robot.keyPress(keyEvents[nextKeyIndex]);
                robot.delay(50);
                robot.keyRelease(keyEvents[nextKeyIndex]);
                robot.delay(50);
                robot.keyRelease(keyEvents[curKeyIndex]);
                robot.delay(50);
                curKeyIndex = getNextKeyIndex(nextKeyIndex);
            }
        } catch (AWTException e) {
            throw new RuntimeException(e);
        }
    }

    private void trainModel() {
        loadTransformMatrix();

        int curKeyIndex = 0;//initial action VK_RIGHT
        int nextKeyIndex;

        Robot robot;
        try {
            robot = new Robot();
            int maxScore = Integer.MIN_VALUE;
            while (period-- > 0) {
                while (stepNum-- > 0) {
                    nextKeyIndex = getNextKeyIndex(curKeyIndex);
                    updateCountMatrix(curKeyIndex, nextKeyIndex);
//                System.out.println(nextKeyIndex);
                    robot.keyPress(keyEvents[curKeyIndex]);
                    robot.delay(50);
                    robot.keyPress(keyEvents[nextKeyIndex]);
                    robot.delay(50);
                    robot.keyRelease(keyEvents[nextKeyIndex]);
                    robot.delay(50);
                    robot.keyRelease(keyEvents[curKeyIndex]);
                    robot.delay(50);
                    curKeyIndex = getNextKeyIndex(nextKeyIndex);
                }
                int curScore = gameRunThread.getScore();
                if (curScore > maxScore) {
                    updateTransformMatrix();
                    saveTransformMatrix();
                    maxScore = curScore;
                }
                //TODO: resetting game in another thread may cause inconsistency
                gameRunThread.resetGame();
                stepNum = 100;
            }
        } catch (AWTException e) {
            throw new RuntimeException(e);
        }
    }

    public int getNextKeyIndex(int curKeyIndex) {
        int curIndex = 0;
        int[] jumpTable = new int[100];
        for (int i = 0; i < transformMatrix[curKeyIndex].length; i++) {
            int size = (int) (transformMatrix[curKeyIndex][i] * 100);
            for (int j = 0; j < size; j++) {
                jumpTable[curIndex++] = i;
            }
        }
        if (curIndex < 99) {
            curIndex++;
            jumpTable[curIndex] = keyEvents.length - 1;
        }
        return jumpTable[random.nextInt(100)];
    }

    public void loadTransformMatrix() {
        try {
            String pathname = "./transformMatrix.txt";
            File filename = new File(pathname);
            InputStreamReader reader = new InputStreamReader(new FileInputStream(filename));
            BufferedReader br = new BufferedReader(reader);
            String line = br.readLine();
            LinkedList<Double> doubles = new LinkedList<>();
            while (line != null) {
                String[] parts = line.split(" ");
                for (String part : parts) {
                    doubles.add(Double.parseDouble(part));
                }
                line = br.readLine();
            }
            Iterator<Double> doubleIterator = doubles.iterator();
            for (int i = 0; i < transformMatrix.length; i++) {
                for (int j = 0; j < transformMatrix.length; j++) {
                    transformMatrix[i][j] = doubleIterator.next();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveTransformMatrix() {
        try {
            File writer = new File("./transformMatrix.txt");
            writer.createNewFile();
            BufferedWriter out = new BufferedWriter(new FileWriter(writer));
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < transformMatrix.length; i++) {
                for (int j = 0; j < transformMatrix.length; j++) {
                    stringBuilder.append(transformMatrix[i][j]).append(" ");
                }
                stringBuilder.append("\r\n");
            }
            out.write(stringBuilder.toString());
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateCountMatrix(int curKeyIndex, int nextKeyIndex) {
        countMatrix[curKeyIndex][nextKeyIndex] += 1;
    }

    public void updateTransformMatrix() {
        for (int i = 0; i < countMatrix.length; i++) {
            double sum = Arrays.stream(countMatrix[i]).sum();
            for (int j = 0; j < countMatrix.length; j++) {
                transformMatrix[i][j] = countMatrix[i][j] / sum;
            }
        }
    }
}