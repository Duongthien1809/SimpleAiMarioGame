package view;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class ImageLoader {
    private BufferedImage sprite;
    private BufferedImage heroImage;
//    private BufferedImage brickAnimation;

    public ImageLoader() {
        heroImage = loadImage("/hero.png");
//        sprite = loadImage("/sprite.png");
    }

    public BufferedImage loadImage(String path) {
        BufferedImage imageToReturn = null;

        try {
            imageToReturn = ImageIO.read(Objects.requireNonNull(getClass().getResource("/media" + path)));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return imageToReturn;
    }

//    public BufferedImage getSpriteSubImage(int col, int row, int w, int h){
//        if((col == 1 || col == 4) && row == 3){ //koopa
//            return sprite.getSubimage((col-1)*48, 128, w, h);
//        }
//        return sprite.getSubimage((col-1)*48, (row-1)*48, w, h);
//    }

    public BufferedImage[] getHeroLeftFrames() {
        BufferedImage[] leftFrames = new BufferedImage[5];
        int col = 1;
        int width = 52, height = 48;

        for (int i = 0; i < 5; i++) {
            leftFrames[i] = heroImage.getSubimage((col - 1) * width, (i) * height, width, height);
        }
        return leftFrames;
    }

    public BufferedImage[] getHeroRightFrames() {
        BufferedImage[] rightFrames = new BufferedImage[5];
        int col = 2;
        int width = 52, height = 48;

        for (int i = 0; i < 5; i++) {
            rightFrames[i] = heroImage.getSubimage((col - 1) * width, (i) * height, width, height);
        }
        return rightFrames;
    }

    public BufferedImage getSubImage(BufferedImage image, int col, int row, int w, int h) {
        if((col == 1 || col == 4) && row == 3){ //koopa
            return image.getSubimage((col-1)*48, 128, w, h);
        }
        return image.getSubimage((col-1)*48, (row-1)*48, w, h);
    }
//    public BufferedImage[] getBlockFrames() {
//        BufferedImage[] frames = new BufferedImage[4];
//        for(int i = 0; i < 4; i++){
//            frames[i] = blockAnimation.getSubimage(i*105, 0, 105, 105);
//        }
//        return frames;
//    }
}