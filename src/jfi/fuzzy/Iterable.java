package jfi.fuzzy;

import java.util.Iterator;
import jfi.fuzzy.Iterable.FuzzyItem;

/**
 * An interface representing an iterator over a fuzzy set. Implementing this
 * interface allows the fuzzy set to be the target of the "for-each loop"
 * statement. The type of elements returned by the iterator is
 * {@link jfi.fuzzy.Iterable.FuzzyItem}.
 *
 * @param <D> the domain of the fuzzy set.
 * 
 * @author Jesús Chamorro Martínez (jesus@decsai.ugr.es)
 */
public interface Iterable<D> extends java.lang.Iterable<FuzzyItem<D>>{
    /**
     * Returns an iterator over elements of type {@code FuzzyItem<D>}.
     *
     * @return an Iterator.
     */
    @Override
    Iterator<FuzzyItem<D>> iterator();
    
    /**
     * Class representing a fuzzy item, that is, a pair (element, degree).
     * 
     * @param <D> the domain of the fuzzy set.
     */
    public interface FuzzyItem<D>{
        /**
         * Returns the element of this fuzzy item.
         * 
         * @return the element of this fuzzy item.
         */
        public D getElement();
        /**
         * Returns the degree of this fuzzy item.
         * 
         * @return the degree of this fuzzy item.
         */
        public Double getDegree();
    }
}
