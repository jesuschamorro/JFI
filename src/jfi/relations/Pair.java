package jfi.relations;

/**
 * Class representirn a pair of elements.
 * 
 * @param <T> the type of the first element.
 * @param <U> the type of the second element.
 * 
 * @author Jesús Chamorro Martínez (jesus@decsai.ugr.es)
 */
public class Pair<T,U> {
    /**
     * First element of the pair.
     */
    T left;
    /**
     * Second element of the pair.
     */
    U right;

    /**
     * Constructs a new pair of elements.
     * 
     * @param left first element of the pair.
     * @param right second element of the pair.
     */
    public Pair(T left, U right){
        this.set(left, right);
    }
    
    /**
     * Set the left element of the pair.
     * 
     * @param left the new element. 
     */
    public void setLeft(T left) {
        this.left = left;
    }

    /**
     * Set the right element of the pair.
     * 
     * @param right the new element. 
     */
    public void setRight(U right) {
        this.right = right;
    }
    
    /**
     * Set both the left and rigth elements of the pair.
     * 
     * @param left the new element. 
     * @param right the new element.
     */
    public final void set(T left, U right) {
        this.left = left;
        this.right = right;
    }

    /**
     * Returns the left element of the pair.
     * 
     * @return the left element of the pair.
     */
    public T getLeft() {
        return left;
    }

    /**
     * Returns the right element of the pair.
     * 
     * @return the right element of the pair.
     */
    public U getRight() {
        return right;
    }  
}
