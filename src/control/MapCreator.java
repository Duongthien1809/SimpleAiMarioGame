package control;

import model.EndPoint;
import model.Map;
import model.block.Block;
import model.block.Ground;
import model.block.Normal;
import model.enemy.Enemy;
import model.enemy.Mushroom;
import model.hero.Hero;
import view.ImageLoader;

import java.awt.*;
import java.awt.image.BufferedImage;

public class MapCreator {
    private ImageLoader imageLoader;

    private BufferedImage backgroundImage;
    private BufferedImage normalBlock, surpriseBlock, groundBlock, pipe, endPoint;
    private BufferedImage goombaLeft, goombaRight, koopaLeft, koopaRight, endFlag;

    public MapCreator(ImageLoader imageLoader) {
        this.imageLoader = imageLoader;
        this.backgroundImage = imageLoader.loadImage("/background.png");
    }

    public Map createMap(String mapPath, double timeLimit, Hero hero1) {
        BufferedImage mapImage = imageLoader.loadImage(mapPath);

        if (mapImage == null) {
            System.out.println("Given path is invalid...");
            return null;
        }

        Map createdMap = new Map(timeLimit, backgroundImage);
        String[] paths = mapPath.split("/");
        createdMap.setPath(paths[paths.length-1]);

        int pixelMultiplier = 48;

        int hero = new Color(160, 160, 160).getRGB();
        int normalBlock = new Color(0, 0, 255).getRGB();
        int surpriseBrick = new Color(255, 255, 0).getRGB();
        int groundBlock = new Color(255, 0, 0).getRGB();
        int pipe = new Color(0, 255, 0).getRGB();
        int mushroom = new Color(0, 255, 255).getRGB();
        int koopa = new Color(255, 0, 255).getRGB();
        int end = new Color(160, 0, 160).getRGB();

        for (int x = 0; x < mapImage.getWidth(); x++) {
            for (int y = 0; y < mapImage.getHeight(); y++) {

                int currentPixel = mapImage.getRGB(x, y);
                int xLocation = x*pixelMultiplier;
                int yLocation = y*pixelMultiplier;

                if (currentPixel == normalBlock) {
                    Block block = new Normal(xLocation, yLocation, this.normalBlock);
                    createdMap.addBlock(block);
                }
//                else if (currentPixel == surpriseBlock) {
//                    Prize prize = generateRandomPrize(xLocation, yLocation);
//                    Brick brick = new SurpriseBrick(xLocation, yLocation, this.surpriseBrick, prize);
//                    createdMap.addBrick(brick);
//                }
//                else if (currentPixel == pipe) {
//                    Brick brick = new Pipe(xLocation, yLocation, this.pipe);
//                    createdMap.addGroundBrick(brick);
//                }
                else if (currentPixel == groundBlock) {
                    Block brick = new Ground(xLocation, yLocation, this.groundBlock);
                    createdMap.addGround(brick);
                }
                else if (currentPixel == mushroom) {
                    Enemy enemy = new Mushroom(xLocation, yLocation, this.goombaLeft,this.goombaRight);
                    createdMap.addEnemy(enemy);
                }
//                else if (currentPixel == koopa) {
//                    Enemy enemy = new KoopaTroopa(xLocation, yLocation, this.koopaLeft);
//                    ((KoopaTroopa)enemy).setRightImage(koopaRight);
//                    createdMap.addEnemy(enemy);
//                }
                else if (currentPixel == hero) {
                    createdMap.setHero(hero1);
                }
                else if(currentPixel == end){
                    EndPoint endPoint= new EndPoint(xLocation+24, yLocation, this.endPoint);
                    createdMap.setEndPoint(endPoint);
                }
            }
        }
        System.out.println("Map is created..");
        return createdMap;
    }
}
