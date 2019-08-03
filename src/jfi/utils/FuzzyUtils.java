package jfi.utils;

import java.lang.reflect.Constructor;
import jfi.fuzzy.DiscreteFuzzySet;
import jfi.fuzzy.Iterable.FuzzyItem;

/**
 * Class with some utilities for fuzzy sets
 * 
 * @author Jesús Chamorro Martínez (jesus@decsai.ugr.es)
 */
public class FuzzyUtils {    
    /**
     * Returns a new fuzzy set whith de membership degrees rounded to the default 
     * precision ({@value jfi.utils.JFIMath#DEFAULT_ROUNDING_DECIMALS} decimals)
     * 
     * @param fuzzyset the fuzzy set 
     * @return a new fuzzy set with the rounded membership degrees
     */
    public static DiscreteFuzzySet round(DiscreteFuzzySet fuzzyset){
        DiscreteFuzzySet rounded_fset;        
        FuzzyItem fi;

        try {
            //We try to create an object of the same class of 'fuzzyset' using
            //the empty constructor; if it is not available, a generic 
            //'DiscreteFuzzySet' will be used
            Constructor constructor = fuzzyset.getClass().getConstructor();
            rounded_fset = (DiscreteFuzzySet)constructor.newInstance();
        } catch (Exception ex) {
            rounded_fset = new DiscreteFuzzySet();
        }       
        for(Object e : fuzzyset){
            fi = (FuzzyItem)e;           
            rounded_fset.add(fi.getElement(), JFIMath.round(fi.getDegree()));
        }       
        return rounded_fset;
    }
    
    /**
     * Returns the negatiom of the given fuzzy set 
     * 
     * @param fuzzyset the fuzzy set 
     * @return the negated fuzzy set 
     */
    public static DiscreteFuzzySet negation(DiscreteFuzzySet fuzzyset){
        DiscreteFuzzySet negation_fset;        
        FuzzyItem fi;

        try {
            //We try to create an object of the same class of 'fuzzyset' using
            //the empty constructor; if it is not available, a generic 
            //'DiscreteFuzzySet' will be used
            Constructor constructor = fuzzyset.getClass().getConstructor();
            negation_fset = (DiscreteFuzzySet)constructor.newInstance();
        } catch (Exception ex) {
            negation_fset = new DiscreteFuzzySet();
        }               
        for(Object e : fuzzyset){
            fi = (FuzzyItem)e;           
            negation_fset.add(fi.getElement(), 1.0-fi.getDegree());
        }       
        return negation_fset;
    }
}
