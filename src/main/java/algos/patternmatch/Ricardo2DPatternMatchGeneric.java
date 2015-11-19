package algos.patternmatch;

import algos.patternmatch.automaton.AhoCorasickAutomatonNode;
import common.coords.BoxCoords;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Ricardo2DPatternMatchGeneric<T> {
    private Integer [] uniqueRowIndicies;
    private AhoCorasickAutomatonNode<T> automaton;

    public List<BoxCoords> search(Matchable<T> haystack, Matchable<T> needle){
        List<BoxCoords> matches = new ArrayList<BoxCoords>();
        preprocessPatternToFind(needle);
        for(int k = needle.getHeight()-1; k < haystack.getHeight(); k = k+needle.getHeight()){
            AhoCorasickAutomatonNode<T> node = automaton.getRoot();
            for(int j = 0; j < haystack.getWidth();j++){
                node = node.transition(haystack.getValue(j, k));
                if( node.out()!= null){
                    List<Integer> rowMatches = checkMatch(haystack, needle, k, j-needle.getWidth()+1, node.out());
                    for(Integer matchedRow: rowMatches){
                        matches.add(new BoxCoords(j-needle.getWidth()+1, matchedRow, needle.getWidth(), needle.getHeight()));
                    }
                }
            }
        }
        return matches;
    }

    private List<Integer> checkMatch(Matchable<T> haystack, Matchable<T> needle, int haystackRowMatched, int needleColumnMatched, int indexOfMatchedRow){
        int needleHeight = needle.getHeight();
        Integer [] needleMatched = new Integer[2*needleHeight-1];
        List<Integer> rows = new ArrayList<Integer>();
        for(int i = 0; i < 2*needleHeight-1; i++) needleMatched[i] = -1;
        needleMatched[needleHeight-1] = indexOfMatchedRow;
        for(int needleRow = 0; needleRow < needleHeight; needleRow++){
            if(uniqueRowIndicies[needleRow] != indexOfMatchedRow) continue;
            int needleRowMirrored = needleHeight - needleRow - 1;
            int r =0;
            for(r = haystackRowMatched - needleRow; r < haystackRowMatched -needleRow + needleHeight && r < haystack.getHeight(); r++){
                if(needleMatched[needleRowMirrored] == uniqueRowIndicies[r-haystackRowMatched+needleRow]) continue;
                needleRowMirrored = needleHeight - needleRow - 1 + r-haystackRowMatched+needleRow;
                AhoCorasickAutomatonNode<T> node = automaton.getRoot();
                for(int c = needleColumnMatched; c < needleColumnMatched +needle.getWidth(); c++){
                    node = node.getChild(haystack.getValue(c, r));
                    if(node == null) break;
                }
                if(node != null) {
                    needleMatched[needleRowMirrored] = node.out();
                }
                if(needleMatched[needleRowMirrored].intValue() != uniqueRowIndicies[r-haystackRowMatched+needleRow].intValue()){
                    break;
                }
            }
            if(r == haystackRowMatched - needleRow + needleHeight) rows.add(r-needleHeight);
        }
        return rows;
    }

    private void preprocessPatternToFind(Matchable<T> needle){
        uniqueRowIndicies = new Integer[needle.getHeight()];
        automaton = new AhoCorasickAutomatonNode<T>();
        Queue<T> row = new LinkedList<T>();
        for(int y = 0; y < needle.getHeight(); y++){
            row.clear();
            for(int x = 0; x < needle.getWidth(); x++){
                row.add(needle.getValue(x, y));
            }
            uniqueRowIndicies[y] = automaton.addWord(row);
        }
        automaton.makeFallFunction();
    }
}
