package jfi.utils;

import java.security.InvalidParameterException;

/**
 * Class with some linguistic hedges
 * 
 * @author Jesús Chamorro Martínez (jesus@decsai.ugr.es)
 */
public class FuzzyHedges {
    
    /**
     * Returns the linguistic hedges "very"
     * @param degree the degree
     * @return the linguistic hedges "very"
     */
    public static double very(double degree){
        return Math.pow(degree,2.0);
    }
    
    /**
     * Returns the linguistic hedges "very very"
     * @param degree the degree
     * @return the linguistic hedges "very very"
     */
    public static double veryvery(double degree){
        return Math.pow(degree,4.0);
    }
    
    /**
     * Returns the linguistic hedges "plus"
     * @param degree the degree
     * @return the linguistic hedges "plus"
     */
    public static double plus(double degree){
        return Math.pow(degree,1.25);
    }
    
    /**
     * Returns the linguistic hedges "slightly"
     * @param degree the degree
     * @return the linguistic hedges "slightly"
     */
    public static double slightly(double degree){
        return Math.pow(degree,0.5);
    }
    
    /**
     * Returns the linguistic hedges "minus"
     * @param degree the degree
     * @return the linguistic hedges "minus"
     */
    public static double minus(double degree){
        return Math.pow(degree,0.75);
    }
    
    /**
     * Returns a "concentration" linguistic hedge 
     * @param degree the degree
     * @param exponent exponent fot the concentration (must be greater than 1.0)
     * @return the linguistic hedges 
     */
    public static double concentration(double degree, double exponent){
        if(exponent<1.0)
            throw new InvalidParameterException("The exponent must be greater than 1.0");
        return Math.pow(degree,exponent);
    }
    
    /**
     * Returns a "dilation" linguistic hedge
     * @param degree the degree
     * @param exponent exponent fot the dilation (must be lower than 1.0)
     * @return the linguistic hedges 
     */
    public static double dilation(double degree, double exponent){
        if(exponent>1.0)
            throw new InvalidParameterException("The exponent must be lower than 1.0");
        return Math.pow(degree,exponent);
    }
}
