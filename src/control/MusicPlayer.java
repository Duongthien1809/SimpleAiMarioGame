package control;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.BufferedInputStream;
import java.io.InputStream;

public class MusicPlayer {

    private static Clip background = getClip(loadAudio("background"));
    private static long clipTime = 0;

    private static AudioInputStream loadAudio(String url) {
        try {
            InputStream audioSrc = MusicPlayer.class.getResourceAsStream("/media/audio/" + url + ".wav");
            InputStream bufferedIn = new BufferedInputStream(audioSrc);
            return AudioSystem.getAudioInputStream(bufferedIn);

        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        return null;
    }

    private static Clip getClip(AudioInputStream stream) {
        try {
            Clip clip = AudioSystem.getClip();
            clip.open(stream);
            return clip;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void resumeBackground(){
        background.setMicrosecondPosition(clipTime);
        background.start();
    }

    public static void pauseBackground(){
        clipTime = background.getMicrosecondPosition();
        background.stop();
    }

    public static void restartBackground() {
        clipTime = 0;
        resumeBackground();
    }

    public static void resetBackground(){
        background.stop();
        background.start();
    }

    public static void playJump() {
        Clip clip = getClip(loadAudio("jump"));
        clip.start();

    }

    public static void playAcquireX() {
        Clip clip = getClip(loadAudio("coin"));
        clip.start();

    }

    public static void playShoot() {
        Clip clip = getClip(loadAudio("fireball"));
        clip.start();

    }

    public static void playGameOver() {
        Clip clip = getClip(loadAudio("gameOver"));
        clip.start();

    }

    public static void playStomp() {
        Clip clip = getClip(loadAudio("stomp"));
        clip.start();

    }

    public static void playOneUp() {
        Clip clip = getClip(loadAudio("oneUp"));
        clip.start();

    }

    public static void playSuperMushroom() {

        Clip clip = getClip(loadAudio("superMushroom"));
        clip.start();

    }

    public static void playHeroDies() {

        Clip clip = getClip(loadAudio("marioDies"));
        clip.start();

    }
}

