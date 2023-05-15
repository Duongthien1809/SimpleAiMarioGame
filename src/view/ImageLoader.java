package view;

import model.Hero;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class ImageLoader {

    private BufferedImage heroImage;

    public ImageLoader() {
        heroImage = loadImage("/mario-forms.png");
    }

    public BufferedImage loadImage(String path) {
        BufferedImage imageToReturn = null;

        try {
            imageToReturn = ImageIO.read(getClass().getResource("/media" + path));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return imageToReturn;
    }

    public BufferedImage getHeroImage() {
        return heroImage;
    }

    public BufferedImage[] getLeftFrames() {
        BufferedImage[] leftFrames = new BufferedImage[5];
        int col = 1;
        int width = 52, height = 48;

        for (int i = 0; i < 5; i++) {
            leftFrames[i] = heroImage.getSubimage((col - 1) * width, (i) * height, width, height);
        }
        return leftFrames;
    }

    public BufferedImage[] getRightFrames() {
        BufferedImage[] rightFrames = new BufferedImage[5];
        int col = 2;
        int width = 52, height = 48;

        for (int i = 0; i < 5; i++) {
            rightFrames[i] = heroImage.getSubimage((col - 1) * width, (i) * height, width, height);
        }
        return rightFrames;
    }
}