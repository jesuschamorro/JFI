/*
  Interval of numbers

  @author Jesús Chamorro Martínez - UGR
*/

package jfi.utils;

import java.util.Collection;
import java.util.Iterator;

public class Interval<N extends Number> implements Collection<N>{

    /**
     *
     */
    private N low;
    /**
     *
     */
    private N high;

    /**
     *
     * @param low
     * @param high
     */
    public Interval(N low, N high) {
        this.low = low;
        this.high = high;
    }

    /**
     *
     * @param number
     * @return
     */
    public boolean contains(N number) {
        return (number.doubleValue() >= low.doubleValue() && number.doubleValue() <= high.doubleValue());
    }

    /**
     *
     * @return
     */
    public N getLow() {
        return low;
    }

    /**
     *
     * @return
     */
    public N getHigh() {
        return low;
    }

    /**
     *
     * @param low
     */
    public void setLow(N low) {
        this.low = low;
    }

    /**
     *
     * @param high
     */
    public void setHigh(N high) {
        this.high = high;
    }

    
    // <editor-fold defaultstate="collapsed" desc="Collection methods">    
    /* 
        The following methods must be implemented in order to be a Collection.
    
        Note that an interval can be viewed as a "continous collection" but not 
        a classical one (in the sense of a group of finite elements), so much of
        the Collection interface methods has no sense
     */
    @Override
    public int size() {
        return high.intValue() - low.intValue();
    }

    @Override
    public boolean isEmpty() {
        return size() <= 0;
    }

    @Override
    public boolean contains(Object o) {
        if (!(o instanceof Number)) {
            throw new IllegalArgumentException("Not a number");
        } else {
            return this.contains(o);
        }
    }

    @Override
    public Iterator<N> iterator() {
        throw new UnsupportedOperationException("Not supported for intervals");
    }

    @Override
    public Object[] toArray() {
        throw new UnsupportedOperationException("Not supported for intervals");
    }

    @Override
    public <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException("Not supported for intervals");
    }

    @Override
    public boolean add(N e) {
        throw new UnsupportedOperationException("Not supported for intervals");
    }

    @Override
    public boolean remove(Object o) {
        throw new UnsupportedOperationException("Not supported for intervals");
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        throw new UnsupportedOperationException("Not supported for intervals");
    }

    @Override
    public boolean addAll(Collection<? extends N> c) {
        throw new UnsupportedOperationException("Not supported for intervals");
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException("Not supported for intervals");
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException("Not supported for intervals");
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException("Not supported for intervals");
    }
    // </editor-fold>
}
