package algos.patternmatch;

public interface Matchable<T> {

    int getHeight();
    int getWidth();
    T getValue(int x, int y);
}
