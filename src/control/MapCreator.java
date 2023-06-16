package control;


import model.Endpoint;
import model.Map;
import model.block.*;
import model.enemy.Enemy;
import model.enemy.Mushroom;
import model.hero.Hero;
import model.prize.Award;
import model.prize.X;
import view.ImageLoader;

import java.awt.*;
import java.awt.image.BufferedImage;

class MapCreator {

    private ImageLoader imageLoader;

    private BufferedImage backgroundImage;
    private BufferedImage superMushroom, oneUpMushroom, fireFlower, coin;
    private BufferedImage ordinaryBlock, surpriseBlock,emptyBlock, groundBlock, bin;
    private BufferedImage goombaLeft, goombaRight, koopaLeft, koopaRight, endFlag;
    private BufferedImage bulletStyle;


    MapCreator(ImageLoader imageLoader) {

        this.imageLoader = imageLoader;
        BufferedImage sprite = imageLoader.loadImage("/sprite.png");

        this.backgroundImage = imageLoader.loadImage("/background.png");
        this.superMushroom = imageLoader.getSubImage(sprite, 2, 5, 48, 48);
        this.oneUpMushroom = imageLoader.getSubImage(sprite, 3, 5, 48, 48);
        this.fireFlower = imageLoader.getSubImage(sprite, 4, 5, 48, 48);
        this.coin = imageLoader.getSubImage(sprite, 1, 5, 48, 48);
        this.ordinaryBlock = imageLoader.getSubImage(sprite, 1, 1, 48, 48);
        this.surpriseBlock = imageLoader.getSubImage(sprite, 2, 1, 48, 48);
        this.groundBlock = imageLoader.getSubImage(sprite, 2, 2, 48, 48);
        this.emptyBlock = imageLoader.getSubImage(sprite, 2, 2, 48, 48);
        this.bin = imageLoader.getSubImage(sprite, 3, 1, 96, 96);
        this.goombaLeft = imageLoader.getSubImage(sprite, 2, 4, 48, 48);
        this.goombaRight = imageLoader.getSubImage(sprite, 5, 4, 48, 48);
        this.koopaLeft = imageLoader.getSubImage(sprite, 1, 3, 48, 64);
        this.koopaRight = imageLoader.getSubImage(sprite, 4, 3, 48, 64);
        this.endFlag = imageLoader.getSubImage(sprite, 5, 1, 48, 48);
        this.bulletStyle = imageLoader.getSubImage(sprite, 3, 4, 24, 24);
    }

    Map createMap(String mapPath, double timeLimit) {
        BufferedImage mapImage = imageLoader.loadImage(mapPath);

        if (mapImage == null) {
            System.out.println("Given path is invalid...");
            return null;
        }

        Map createdMap = new Map(timeLimit, backgroundImage);
        String[] paths = mapPath.split("/");
        createdMap.setPath(paths[paths.length - 1]);

        int pixelMultiplier = 48;

        int heroRgb = new Color(160, 160, 160).getRGB();
        int normalBlock = new Color(0, 0, 255).getRGB();
        int magicBlock = new Color(255, 255, 0).getRGB();
        int groundBlock = new Color(255, 0, 0).getRGB();
        int bin = new Color(0, 255, 0).getRGB();
        int mushroom = new Color(0, 255, 255).getRGB();
        int end = new Color(160, 0, 160).getRGB();

        for (int x = 0; x < mapImage.getWidth(); x++) {
            for (int y = 0; y < mapImage.getHeight(); y++) {

                int currentPixel = mapImage.getRGB(x, y);
                int xLocation = x * pixelMultiplier;
                int yLocation = y * pixelMultiplier;

                if (currentPixel == normalBlock) {
                    Block block = new Normal(xLocation, yLocation, this.ordinaryBlock);
                    createdMap.addBrick(block);
                } else if (currentPixel == magicBlock) {
                    Award award = generateRandomCoin(xLocation, yLocation);
                    Block brick = new Magic(xLocation, yLocation, this.surpriseBlock,emptyBlock, award);
                    createdMap.addBrick(brick);
                } else if (currentPixel == groundBlock) {
                    Block brick = new Ground(xLocation, yLocation, this.groundBlock);
                    createdMap.addGroundBrick(brick);
                } else if (currentPixel == bin) {
                    //TODO trash bin Class to add
                    Bin trashBin = new Bin(xLocation, yLocation, this.bin);
                    createdMap.addBrick(trashBin);
                } else if (currentPixel == mushroom) {
                    Enemy enemy = new Mushroom(xLocation, yLocation, this.goombaLeft, this.goombaRight);
                    createdMap.addEnemy(enemy);
                } else if (currentPixel == heroRgb) {
                    Hero hero = new Hero(xLocation, yLocation, bulletStyle);
                    createdMap.setHero(hero);
                } else if (currentPixel == end) {
                    Endpoint endPoint = new Endpoint(xLocation + 24, yLocation, endFlag);
                    createdMap.setEndPoint(endPoint);
                }
            }
        }

        System.out.println("Map is created..");
        return createdMap;
    }

    private Award generateRandomCoin(double x, double y) {
        Award generated = null;
/*        //coin
        int random = (int) (Math.random() * 12);

        if (random == 0) { //super mushroom

        } else if (random == 1) { //fire flower

        } else if (random == 2) { //one up mushroom

        } else { //coin

        }*/
        generated = new X(x, y, this.coin, 50);

        return generated;
    }
}
