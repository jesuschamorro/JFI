package jfi.utils;

import java.lang.reflect.Constructor;
import java.util.Map;
import jfi.fuzzy.DiscreteFuzzySet;

/**
 * Class with some utilities for fuzzy sets
 * 
 * @author Jesús Chamorro Martínez (jesus@decsai.ugr.es)
 */
public class FuzzyUtils {
    /**
     * Default precision in rounding operations
     */
    private final static double DEFAULT_PRECISION = 100.0; // Two decimal 
    
    /**
     * Returns a new fuzzy set whith de membership degrees rounded to a default 
     * precision 
     * 
     * @param fuzzyset the fuzzy set 
     * @return a new fuzzy set with the rounded membership degrees
     */
    public static DiscreteFuzzySet round(DiscreteFuzzySet fuzzyset){
        DiscreteFuzzySet rounded_fset;        
        Double degree;

        try {
            //We try to create an object of the same class of 'fuzzyset' using
            //the empty constructor; if it is not available, a generic 
            //'DiscreteFuzzySet' will be used
            Constructor constructor = fuzzyset.getClass().getConstructor();
            rounded_fset = (DiscreteFuzzySet)constructor.newInstance();
        } catch (Exception ex) {
            rounded_fset = new DiscreteFuzzySet();
        }       
        for (Object e : fuzzyset) {
            degree = (Double) ((Map.Entry) e).getValue();
            rounded_fset.add(((Map.Entry) e).getKey(), round(degree));
        }
        return rounded_fset;
    }
    
    /**
     * Returns a rounded version of a given number (using the default precision)
     * 
     * @param number the number to be rounded
     * @return the rounded number
     */
    public static Double round(Double number){
        Double round = Math.round(number*DEFAULT_PRECISION)/DEFAULT_PRECISION;      
        return round;
    }
    
    /**
     * Returns the negatiom of the given fuzzy set 
     * 
     * @param fuzzyset the fuzzy set 
     * @return the negated fuzzy set 
     */
    public static DiscreteFuzzySet negation(DiscreteFuzzySet fuzzyset){
        DiscreteFuzzySet negation_fset;        
        Double degree;

        try {
            //We try to create an object of the same class of 'fuzzyset' using
            //the empty constructor; if it is not available, a generic 
            //'DiscreteFuzzySet' will be used
            Constructor constructor = fuzzyset.getClass().getConstructor();
            negation_fset = (DiscreteFuzzySet)constructor.newInstance();
        } catch (Exception ex) {
            negation_fset = new DiscreteFuzzySet();
        }       
        for (Object e : fuzzyset) {
            degree = (Double) ((Map.Entry) e).getValue();
            negation_fset.add(((Map.Entry) e).getKey(), 1.0-degree);
        }
        return negation_fset;
    }
}
