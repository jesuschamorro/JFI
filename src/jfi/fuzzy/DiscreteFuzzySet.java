package jfi.fuzzy;

import java.security.InvalidParameterException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map.Entry;
import java.util.Set;

/**
 * Class representing a fuzzy set on a discrete domain. The reference set is a
 * finite set made up only of isolated points; it is represented as a collection
 * of elements, where each of these elements have a degree of membership.
 * 
 * @param <D> the domain of the fuzzy set.
 * @author Jesús Chamorro Martínez (jesus@decsai.ugr.es)
 */
public class DiscreteFuzzySet<D> implements FuzzySet<D>, AlphaCuttable, Iterable<Entry<D,Double>> {
    /**
     * The label associated to the fuzzy set.
     */
    protected String label;
    /**
     * Mapping from domain elements (keys) to membership degrees (values). It
     * cannot contain duplicate keys and each key maps to one value.
     */
    protected final LinkedHashMap<D, Double> dataMap; //Ordered map

    /**
     * Constructs an empty fuzzy set.
     *
     * @param label label of the fuzzy set.
     */
    public DiscreteFuzzySet(String label) {
        this.label = label;
        this.dataMap = new LinkedHashMap<>();
    }

    /**
     * Constructs an empty fuzzy set with an empty label.
     *
     */
    public DiscreteFuzzySet() {
        this("");
    }

    /**
     * Adds the specified element to this fuzzy set if it is not already
     * present. If the element <tt>e</tt> is previously contained, the
     * memmership degree is updated to <tt>degree</tt>.
     *
     * @param e the new element to be added.
     * @param degree the memebership degree of the new element.
     * @return <tt>true</tt> if this set did not already contain the specified
     * element.
     */
    public boolean add(D e, double degree) {
        return dataMap.put(e, degree) == null;
    }

    /**
     * Removes the specified element from this fuzzy set, if it is present.
     *
     * @param o object to be removed from this set, if present.
     * @return <tt>true</tt> if the set contained the specified element.
     */
    public boolean remove(D o) {
        return dataMap.remove(o) != null;
    }

    /**
     * Return the number of elements in this fuzzy set.
     * 
     * @return the set size.
     */
    public int size(){
        return dataMap.size();
    }
    
    /**
     * Returns <tt>true</tt> if this set is empty.
     *
     * @return <tt>true</tt> if this set is empty.
     */
    public boolean isEmpty(){
        return dataMap.isEmpty();
    }
    
    /**
     * Returns an iterator over the elements in this fuzzy set. The elements are
     * represented as a pair (element of the domain, membership degree) and they
     * are returned in the order in which they were inserted into the set.
     *
     * @return an iterator over the elements in this fuzzy set
     */
    @Override
    public Iterator<Entry<D,Double>> iterator(){
        return dataMap.entrySet().iterator();
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
    public Set<D> getReferenceSet() {
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
    public double membershipDegree(D e) {
        return dataMap.get(e);
    }
    
    /**
     * Set a new membership degree to the element <tt>e</tt>, if it exists 
     * 
     * @param e the element to be modified
     * @param degree the new memebership degree
     * @return <tt>true</tt> if this set contains the specified element
     */
    public boolean setMembershipDegree(D e, double degree){
        if(degree<0.0 || degree>1.0){
            throw new InvalidParameterException("The degree must be between 0 and 1");
        }
        return dataMap.replace(e, degree) != null;
    } 

    /**
     * Return the alpha-cut of the fuzzy set for a given alpha.
     * 
     * The iteration ordering of the ouput crisp set is the order in which 
     * elements were inserted into the fuzzy set (insertion-order).
     *
     * @param alpha the alpha.
     * @return the alpha-cut.
     */
    @Override
    public Set<D> alphaCut(double alpha) {
        D element;
        LinkedHashSet<D> alpha_cut = new LinkedHashSet<>();

        Iterator<D> set = dataMap.keySet().iterator();
        while (set.hasNext()) {
            element = set.next();
            if (dataMap.get(element) >= alpha) {
                alpha_cut.add(element);
            }
        }
        return alpha_cut;
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
     * @return a set view of the mappings contained in this map.
     */
    public Set<Entry<D,Double>> entrySet() {
        return dataMap.entrySet();
    }
    
    /**
     * Returns a string representation of this fuzzy set.
     *
     * @return a string representation of this fuzzy set.
     */
    @Override
    public String toString(){
        return dataMap.toString();
    }
}
