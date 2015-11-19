package algos.patternmatch.automaton;

import java.util.*;

/**
 * Created by owner on 30/01/2015.
 */
public class AhoCorasickAutomatonNode<T>  {
    private Map<T, AhoCorasickAutomatonNode<T>> children = new HashMap<T, AhoCorasickAutomatonNode<T>>();
    private AhoCorasickAutomatonNode<T> parent;
    private AhoCorasickAutomatonNode<T> fall;
    private T value;
    private boolean isEnd;
    private int addedWordsCounter = -1;
    private Integer recognisedPatternNum = null;

    public AhoCorasickAutomatonNode() {
    }

    private AhoCorasickAutomatonNode(T value) {
        this.value = value;
    }

    public AhoCorasickAutomatonNode<T> transition(T transitionValue){
        AhoCorasickAutomatonNode<T> next = this;
        AhoCorasickAutomatonNode<T> root = getRoot();
        while(next.getChild(transitionValue) == null && next != root) next = next.getFall();
        if(next == root){
            next = next.getChild(transitionValue);
            if(next == null) next = root;
        } else {
            next = next.getChild(transitionValue);
        }
        return next;
    }

    public Integer out(){
        AhoCorasickAutomatonNode<T> node = this;
        while(node != null){
            if(node.isEnd()) return node.recognisedPatternNum;
            node = node.getParent();
        }
        return null;
    }

    public void makeFallFunction(){
        Queue<AhoCorasickAutomatonNode<T>> queue = new LinkedList<AhoCorasickAutomatonNode<T>>();
        AhoCorasickAutomatonNode<T> root = getRoot();
        root.setFall(root);
        queue.add(root);
        while(!queue.isEmpty()){
            AhoCorasickAutomatonNode<T> node = queue.remove();
            for(AhoCorasickAutomatonNode<T> child: node.getChildren().values()){
                queue.add(child);
            }
            if(node == root) continue;
            AhoCorasickAutomatonNode<T> fall = node.getParent().getFall();
            while(fall.getChild(node.getValue()) == null && fall != root) fall = fall.getFall();
            node.setFall( fall.getChild(node.getValue()));
            if(node.getFall() == null) node.setFall(root);
            if(node.getFall() == node) node.setFall(root);
        }
    }

    public Map<Integer, AhoCorasickAutomatonNode<T>> search(Queue<T> string){
        int indexMatch = 0;
        AhoCorasickAutomatonNode<T> backtracker;
        AhoCorasickAutomatonNode<T> root = getRoot();
        AhoCorasickAutomatonNode<T> next = root;
        Map<Integer, AhoCorasickAutomatonNode<T>> result = new HashMap<Integer, AhoCorasickAutomatonNode<T>>();
        while(!string.isEmpty()){
            T nextChar = string.remove();
            indexMatch++;
            next = next.transition(nextChar);
            backtracker = next;
            while(backtracker != root){
                if(backtracker.isEnd()){
                    result.put(indexMatch - countParents(backtracker), backtracker);
                }
                backtracker = backtracker.getFall();
            }
        }
        return result;
    }

    public AhoCorasickAutomatonNode<T> getRoot(){
        AhoCorasickAutomatonNode<T> root = this;
        while(root.getValue() != null){
            root = root.getParent();
        }
        return root;
    }

    private AhoCorasickAutomatonNode<T> getParent() {
        return parent;
    }

    private void setParent(AhoCorasickAutomatonNode<T> parent) {
        this.parent = parent;
    }

    public AhoCorasickAutomatonNode<T> getFall() {
        return fall;
    }

    private void setFall(AhoCorasickAutomatonNode<T> fall) {
        this.fall = fall;
    }

    private Map<T, AhoCorasickAutomatonNode<T>> getChildren() {
        return children;
    }

    public AhoCorasickAutomatonNode<T> getChild(T value){
        return children.get(value);
    }

    private void setChildren(Map<T, AhoCorasickAutomatonNode<T>> children) {
        this.children = children;
    }

    public T getValue() {
        return value;
    }

    private void setValue(T value) {
        this.value = value;
    }

    public boolean isEnd() {
        return isEnd;
    }

    private void setEnd(boolean isEnd) {
        this.isEnd = isEnd;
    }

    public void setRecognisedPatternNum(int recognisedPatternNum) {
        this.recognisedPatternNum = recognisedPatternNum;
    }

    public int addWord(Queue<T> word){
        addedWordsCounter++;
        return addWordC(word, addedWordsCounter, 1);
    }

    public int addWordC(Queue<T> word, int wordCount, int set){
        T val = word.remove();
        AhoCorasickAutomatonNode<T> n = getChild(val);
        if( n == null){
            n = new AhoCorasickAutomatonNode(val);
            setChild(n);
        }
        if(!word.isEmpty()) return n.addWordC(word, wordCount, set);
        else{
            if(!n.isEnd()) {
                n.setEnd(true);
                n.setRecognisedPatternNum(wordCount);
            } else {
                return n.getRecognisedPatternNum();
            }
            return wordCount;
        }
    }

    public Integer getRecognisedPatternNum() {
        return recognisedPatternNum;
    }

    private void setChild(AhoCorasickAutomatonNode<T> child){
        child.setParent(this);
        children.put(child.getValue(), child);
    }

    private int countParents(AhoCorasickAutomatonNode<T> node){
        AhoCorasickAutomatonNode<T> buff = node;
        int parents = -1;
        while(buff != null){
            buff = buff.getParent();
            parents++;
        }
        return parents;

    }
}
