package jfi.fuzzy;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Set;

/**
 *
 *
 * @author Jesús Chamorro Martínez
 * @param <Domain>
 */
public class DiscreteFuzzySet<Domain> implements FuzzySet<Domain> {

    private String label;
    private final LinkedHashMap<Domain, Double> dataMap; //Ordered map

    /**
     * Constructs an empty fuzzy set.
     *
     * @param label label of the fuzzy set
     */
    public DiscreteFuzzySet(String label) {
        this.label = label;
        this.dataMap = new LinkedHashMap<>();
    }

    /**
     * Constructs an empty fuzzy set with an empty label .
     *
     */
    public DiscreteFuzzySet() {
        this("");
    }

    /**
     * Adds the specified element to this fuzzy set if it is not already
     * present. If the element <tt>e</tt> is previously contained, the
     * memmership degree is updated to <tt>degree</tt>
     *
     * @param e
     * @param degree
     * @return <tt>true</tt> if this set did not already contain the specified
     * element
     */
    public boolean add(Domain e, double degree) {
        return dataMap.put(e, degree) == null;
    }

    /**
     * Removes the specified element from this fuzzy set, if it is present.
     *
     * @param o object to be removed from this set, if present
     * @return <tt>true</tt> if the set contained the specified element
     */
    public boolean remove(Domain o) {
        return dataMap.remove(o) != null;
    }

    /**
     * Return the number of elements in this fuzzy set
     * @return the set size
     */
    public int size(){
        return dataMap.size();
    }
    
    /**
     * Returns an iterator over the elements in this fuzzy set.  The elements 
     * are returned in the order in which they were inserted into the set.
     *
     * @return an iterator over the elements in this fuzzy set
     */
    public Iterator<Domain> iterator(){
        return dataMap.keySet().iterator();
    }
    
    /**
     * Return a set view of the reference set associated to this fuzzy set
     *
     *
     * The set is backed by the fuzzy set, so changes to the fuzzy set are
     * reflected in the reference set, and vice-versa. If the fuzzy set is
     * modified while an iteration over the reference set is in progress, the
     * results of the iteration are undefined. The reference set supports
     * element removal, which removes the corresponding mapping from the fuzzy
     * set; It does not support the add operation.
     *
     * @return a set view the reference set associated to this fuzzy set
     */
    public Set<Domain> getReferenceSet() {
        return dataMap.keySet();
    }

    /**
     * Return the label associated to the fuzzy set
     *
     * @return the label associated to the fuzzy set
     */
    @Override
    public String getLabel() {
        return label;
    }

    /**
     * Set the label associated to the fuzzy set
     *
     * @param label the new label
     */
    @Override
    public void setLabel(String label) {
        this.label = label;
    }

    /**
     * Return the membership degree of the element <tt>e</tt> to the fuzzy set
     *
     * @param e an element of the fuzzy set domain
     * @return the membership degree
     */
    @Override
    public double getMembershipValue(Domain e) {
        return dataMap.get(e);
    }

    /**
     * Return the alpha-cut of the fuzzy set for a given alpha
     *
     * @param alpha the alpha
     * @return the alpha-cut
     */
    @Override
    public Collection<Domain> getAlphaCut(double alpha) {
        Domain element;
        HashSet<Domain> alpha_cut = new HashSet<>();

        Iterator<Domain> set = dataMap.keySet().iterator();
        while (set.hasNext()) {
            element = set.next();
            if (dataMap.get(element) >= alpha) {
                alpha_cut.add(element);
            }
        }
        return alpha_cut;
    }

    /**
     * Return the kernel of the fuzzy set
     *
     * @return the kernel of the fuzzy set
     */
    @Override
    public Collection<Domain> getKernel() {
        return getAlphaCut(1.0f);
    }

    /**
     * Return the support of the fuzzy set
     *
     * @return the support of the fuzzy set
     */
    @Override
    public Collection<Domain> getSupport() {
        return getAlphaCut(0.0f);
    }

}
