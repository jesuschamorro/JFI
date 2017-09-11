package jfi.relations;

/**
 *
 * @author Jesús Chamorro Martínez (jesus@decsai.ugr.es)
 * @param <T>
 * @param <U>
 */
public class Pair<T,U> {
    T left;
    U right;

    public void setLeft(T left) {
        this.left = left;
    }

    public void setRight(U right) {
        this.right = right;
    }

    public T getLeft() {
        return left;
    }

    public U getRight() {
        return right;
    }
    
}
