package algos.patternmatch;

import common.coords.BoxCoords;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertTrue;


public class Ricardo2DPatternMatchTest {
    Ricardo2DPatternMatchBufferedImage matcher = new Ricardo2DPatternMatchBufferedImage();

    @Test
    public void testSearch() throws Exception {
        BufferedImage screen = ImageIO.read(new File(this.getClass().getClassLoader().getResource("landscape.jpg").getPath()));
        int screenWidth = screen.getWidth();
        int screenHeight = screen.getHeight();

        Random r = new Random();
        int x, y, width, height;
        long startTime, endTime;
        BoxCoords box;
        for(int i = 0; i < 5; i++){
            System.out.println("Checking image: " + i);
            x = r.nextInt(screenWidth);
            y = r.nextInt(screenHeight);
            width = r.nextInt(screenWidth -x)+1;
            height = r.nextInt(screenHeight -y)+1;
            box = new BoxCoords(x, y, width, height);
            System.out.println(box.toString());
            BufferedImage subImage = screen.getSubimage(x, y, width, height);
            startTime = System.nanoTime();
            List<BoxCoords> results = matcher.search(screen, subImage);
            endTime = System.nanoTime();
            System.out.println("Image " + i+" took " + ((endTime-startTime)/ 1000000000.0)+"s. to find");
            System.out.println();
            assertTrue(results.contains(box));
        }
    }
}