package algos.patternmatch;


import algos.patternmatch.automaton.AhoCorasickAutomatonNode;
import common.coords.BoxCoords;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

@Deprecated
public class Ricardo2DPatternMatch {
    private Integer [] uniqueRowIndicies;
    private AhoCorasickAutomatonNode<Integer> automaton;

    public List<BoxCoords> search(BufferedImage image, BufferedImage subImage){
        List<BoxCoords> matches = new ArrayList<BoxCoords>();
        preprocessSubImage(subImage);
        for(int k = subImage.getHeight()-1; k < image.getHeight(); k = k+subImage.getHeight()){
            AhoCorasickAutomatonNode<Integer> node = automaton.getRoot();
            for(int j = 0; j < image.getWidth();j++){
                node = node.transition(image.getRGB(j,k));
                if( node.out()!= null){
                    List<Integer> rowMatches = checkMatch(image, subImage, k, j-subImage.getWidth()+1, node.out());
                    for(Integer matchedRow: rowMatches){
                        matches.add(new BoxCoords(j-subImage.getWidth()+1, matchedRow, subImage.getWidth(), subImage.getHeight()));
                    }
                }
            }
        }
        return matches;
    }

    private List<Integer> checkMatch(BufferedImage image, BufferedImage subImage, int imageRowMatched, int imageColumnMatch, int indexOfMatchedRow){
        int subImageHeight = subImage.getHeight();
        Integer [] imageMatched = new Integer[2*subImageHeight-1];
        List<Integer> rows = new ArrayList<Integer>();
        for(int i = 0; i < 2*subImageHeight-1; i++) imageMatched[i] = -1;
        imageMatched[subImageHeight-1] = indexOfMatchedRow;
        for(int subImageRow = 0; subImageRow < subImageHeight; subImageRow++){
            if(uniqueRowIndicies[subImageRow] != indexOfMatchedRow) continue;
            int subImageRowMirrored = subImageHeight - subImageRow - 1;
            int r =0;
            for(r = imageRowMatched - subImageRow; r < imageRowMatched -subImageRow + subImageHeight && r < image.getHeight(); r++){
                if(imageMatched[subImageRowMirrored] == uniqueRowIndicies[r-imageRowMatched+subImageRow]) continue;
                subImageRowMirrored = subImageHeight - subImageRow - 1 + r-imageRowMatched+subImageRow;
                AhoCorasickAutomatonNode<Integer> node = automaton.getRoot();
                for(int c = imageColumnMatch; c < imageColumnMatch +subImage.getWidth(); c++){
                    node = node.getChild(image.getRGB(c, r));
                    if(node == null) break;
                }
                if(node != null) {
                    imageMatched[subImageRowMirrored] = node.out();
                }
                if(imageMatched[subImageRowMirrored].intValue() != uniqueRowIndicies[r-imageRowMatched+subImageRow].intValue()){
                    break;
                }
            }
            if(r == imageRowMatched - subImageRow + subImageHeight) rows.add(r-subImageHeight);
        }
        return rows;
    }

    private void preprocessSubImage(BufferedImage subImage){
        uniqueRowIndicies = new Integer[subImage.getHeight()];
        automaton = new AhoCorasickAutomatonNode<Integer>();
        Queue<Integer> row = new LinkedList<Integer>();
        for(int y = 0; y < subImage.getHeight(); y++){
            row.clear();
            for(int x = 0; x < subImage.getWidth(); x++){
                row.add(subImage.getRGB(x,y));
            }
            uniqueRowIndicies[y] = automaton.addWord(row);
        }
        automaton.makeFallFunction();
    }

}
