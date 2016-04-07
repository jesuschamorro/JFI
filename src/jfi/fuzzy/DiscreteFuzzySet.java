package jfi.fuzzy;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map.Entry;
import java.util.Set;
import jfi.utils.JFIMath;

/**
 *
 *
 * @author Jesús Chamorro Martínez (jesus@decsai.ugr.es)
 * @param <Domain>
 */
public class DiscreteFuzzySet<Domain> implements FuzzySet<Domain>, Iterable<Domain> {

    protected String label;
    protected final LinkedHashMap<Domain, Double> dataMap; //Ordered map

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
     * @param e the new element to be added
     * @param degree the memebership degree of the new element
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
    @Override
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
    public double membershipDegree(Domain e) {
        return dataMap.get(e);
    }
    
    /**
     * Set a new membership degree to the element <tt>e</tt>, if it exists 
     * 
     * @param e the element to be modified
     * @param degree the new memebership degree
     * @return <tt>true</tt> if this set contains the specified element
     */
    public boolean setMembershipDegree(Domain e, double degree){
        return dataMap.replace(e, degree) != null;
    } 

    /**
     * Return the alpha-cut of the fuzzy set for a given alpha.
     * 
     * Thee iteration ordering of the ouput crisp set is the order in which 
     * elements were inserted into the fuzzy set (insertion-order)
     *
     * @param alpha the alpha
     * @return the alpha-cut
     */
    @Override
    public Set<Domain> alphaCut(double alpha) {
        Domain element;
        LinkedHashSet<Domain> alpha_cut = new LinkedHashSet<>();

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
    public Set<Domain> kernel() {
        return alphaCut(1.0);
    }

    /**
     * Return the support of the fuzzy set
     *
     * @return the support of the fuzzy set
     */
    @Override
    public Set<Domain> support() {
        return alphaCut(0.0+JFIMath.EPSILON);
    }

    /**
     * Returns a {@link Set} view of the mappings contained in this fuzzy set, 
     * that is, a set of pairs (element, degree).
     * 
     * The set is backed by the fuzzy set, so changes to the fuzzy set are
     * reflected in the entry set, and vice-versa. If the fuzzy set is
     * modified while an iteration over the entry set is in progress, the
     * results of the iteration are undefined. The entry set supports
     * element removal, which removes the corresponding mapping from the fuzzy
     * set; It does not support the add operation.
     * 
     * @return a set view of the mappings contained in this map
     */
    public Set<Entry<Domain,Double>> entrySet() {
        return dataMap.entrySet();
    }
    
}
