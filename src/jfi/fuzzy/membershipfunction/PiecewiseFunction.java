package jfi.fuzzy.membershipfunction;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Class representing a piecewise function, that is, a function which is defined
 * by multiple sub-functions, each of them applying to a certain interval of the
 * main function's domain.
 * 
 * <p>
 * It may be usefull, for example, with circular domains (as angular ones) where
 * some membership function need to be circular (for example, the fuzzy set 
 * "around zero degrees").
 *
 * @param <D> domain of the function.
 * @author Jesús Chamorro Martínez (jesus@decsai.ugr.es)
 */
public class PiecewiseFunction<D> extends ArrayList<MembershipFunction> implements MembershipFunction<D> {

    /**
     * Constructs an empty piecewise function.
     */
    public PiecewiseFunction() {
        super();
    }

    /**
     * Constructs a piecewise function containing the functions of the specified
     * collection, in the order they are returned by the collection's iterator.
     *
     * @param c the collection whose elements are to be placed into this
     * piecewise function
     * @throws NullPointerException if the specified collection is null
     */
    public PiecewiseFunction(Collection<MembershipFunction> c) {
        super(c);
    }

    /**
     * Applies this membership function to the given argument.
     * 
     * @param x the function argument
     * @return the function result
     */
    @Override
    public Double apply(D x) {
        Double output = 0.0;
        for(MembershipFunction f: this){
            output = Math.max(output , (Double)f.apply(x) );
        }
        return output;
    }
    
    /**
     * Returns an alpha-cut associated to the membership function. Specifically, 
     * returns a list of alpha-cuts, one for each sub-function of this 
     * piecewise.
     *
     * @param alpha the alpha value
     * @return the alpha-cut represented as a list of sub-function alpha-cuts, 
     * null if alpha-cut is not available for some of the functions.
     */     
    @Override
    public ArrayList alphaCut(double alpha) {
        ArrayList output = new ArrayList();
        Object alphacut;
        for (MembershipFunction f : this) {
            alphacut = f.alphaCut(alpha);
            if(alphacut!=null) 
                output.add(alphacut);
            else return null;
        }
        return output;
    }
    
    /**
     * Returns a string representation of this function.
     *
     * @return a string representation of this function.
     */
    @Override
    public String toString(){
        String output = this.getClass().getSimpleName()+"[";
        for(MembershipFunction f: this){
            output+=f.toString()+",";
        }
        output = output.substring(0, output.length() - 1) + "]";
        return output;
    }
}
