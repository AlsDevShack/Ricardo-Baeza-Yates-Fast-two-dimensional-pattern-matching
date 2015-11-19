package algos.patternmatch.automaton;

import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class AhoCorasickAutomatonNodeTest {

    @Test
    public void testAutomaton(){
        AhoCorasickAutomatonNode<Character> root = new AhoCorasickAutomatonNode<Character>();
        assertEquals(0, root.addWord(stringToCharQueue("needle")));
        assertEquals(1, root.addWord(stringToCharQueue("needse")));
        assertEquals(2, root.addWord(stringToCharQueue("needsejoe")));
        assertEquals(3, root.addWord(stringToCharQueue("hehey")));
        assertEquals(4, root.addWord(stringToCharQueue("nose")));
        assertEquals(4, root.addWord(stringToCharQueue("nose")));
        assertEquals(2, root.addWord(stringToCharQueue("needsejoe")));
        root.makeFallFunction();
        String text = "hanoseystackadsdneeneedsejoedhaneedleuoerkhehasdlneedleaheheysd";
        Map<Integer, AhoCorasickAutomatonNode<Character>> result = root.search(stringToCharQueue(text));
        Set<Integer> resultIndexes = result.keySet();
        List<Integer> correctIndexes = Arrays.asList(2, 19, 19, 31, 49, 56);
        assertTrue(resultIndexes.containsAll(correctIndexes));
        assertTrue(correctIndexes.containsAll(resultIndexes));
        assertEquals((Integer) 4, result.get(2).out());
        assertEquals((Integer)2, result.get(19).out());
        assertEquals((Integer)0, result.get(31).out());
        assertEquals((Integer)0, result.get(49).out());
        assertEquals((Integer)3, result.get(56).out());
        assertNull(result.get(56).getFall().out());
    }

    public static Queue<Character> stringToCharQueue(String string){
        Queue<Character> charQueue = new LinkedList<Character>();
        for (int i = 0; i < string.length(); i++) {
            charQueue.add(string.charAt(i));
        }
        return charQueue;
    }

}