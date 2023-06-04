package model.block;

import model.prize.Award;
import view.Animation;
import view.ImageLoader;

import java.awt.image.BufferedImage;

public class Magic extends Block{

    private BufferedImage emptyStyle;
    private Award award;

    public Magic(double x, double y, BufferedImage style, BufferedImage emptyStyle, Award award) {
        super(x, y, style);
        this.emptyStyle = emptyStyle;
        setEmpty(false);
        this.award = award;
    }

    @Override
    public Award reveal(){
        if(award != null){
            award.reveal();
        }

        setEmpty(true);
        setStyle(emptyStyle);

        Award toReturn = this.award;
        this.award = null;
        return toReturn;
    }
    public Award getAward(){
        return award;
    }
}

