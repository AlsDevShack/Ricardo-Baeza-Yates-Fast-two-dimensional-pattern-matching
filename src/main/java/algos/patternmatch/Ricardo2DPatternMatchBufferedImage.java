package algos.patternmatch;

import common.coords.BoxCoords;

import java.awt.image.BufferedImage;
import java.util.List;

public class Ricardo2DPatternMatchBufferedImage {
    Ricardo2DPatternMatchGeneric<Integer> matcher = new Ricardo2DPatternMatchGeneric<Integer>();
    List<BoxCoords> search(BufferedImage image, BufferedImage subImage){
        return matcher.search(imageToMatchable(image), imageToMatchable(subImage));
    }


    public static Matchable<Integer> imageToMatchable(final BufferedImage image){
        return new Matchable<Integer>() {
            @Override
            public int getHeight() {
                return image.getHeight();
            }

            @Override
            public int getWidth() {
                return image.getWidth();
            }

            @Override
            public Integer getValue(int x, int y) {
                return image.getRGB(x,y);
            }
        };
    }

}
