package jfi.fuzzy.cardinal;

import jfi.fuzzy.DiscreteFuzzySet;

/**
 * Class representing the fuzzy cardinal sigma-count.
 * 
 * @author Jesús Chamorro Martínez (jesus@decsai.ugr.es)
 */
public class SigmaCount extends Number{
    /**
     * Sigma-count value
     */
    private Double sigma;
    
    /**
     * Constructs a new sigma-count cardinal
     * 
     * @param set the fuzzy set
     */
    public SigmaCount(DiscreteFuzzySet set){
        sigma = 0.0;
        for(Object e:set){
            sigma += set.membershipDegree(e);
        }
    } 

    /**
     * Return the sigma-count value
     * @return the sigma-count value
     */
    public Double getValue(){
        return sigma;
    } 
    
    // <editor-fold defaultstate="collapsed" desc="Number inherit methods"> 
    @Override
    public int intValue() {
        return sigma.intValue();
    }

    @Override
    public long longValue() {
        return sigma.longValue();
    }

    @Override
    public float floatValue() {
        return sigma.floatValue();
    }

    @Override
    public double doubleValue() {
        return sigma;
    }
    // </editor-fold>  
}
