package jfi.graph;

import jfi.fuzzy.FuzzySet;

/**
 *
 * @param <T> domain of the property associated to this node
 * @author Jesús Chamorro Martínez (jesus@decsai.ugr.es)
 */
public class FuzzyNode<T extends FuzzySet<D>,D> extends Node<T> implements FuzzySet<D>{
    /**
     * The label associated to the fuzzy set.
     */
    protected String label;
    
    /**
     * 
     * @param source 
     */
    public FuzzyNode(T source){
        super(source);
    }
    
    /**
     * Returns the label associated to the fuzzy set.
     *
     * @return the label associated to the fuzzy set.
     */
    @Override
    public String getLabel() {
        return label;
    }

    /**
     * Set the label associated to the fuzzy set.
     *
     * @param label the new label.
     */
    @Override
    public void setLabel(String label) {
        this.label = label;
    }

    /**
     * 
     * @param e
     * @return 
     */
    @Override
    public double membershipDegree(D e) {
        return this.getSource().membershipDegree(e);
    }
    
}
