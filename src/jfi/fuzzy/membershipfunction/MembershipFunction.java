package jfi.fuzzy.membershipfunction;

import jfi.fuzzy.AlphaCuttable;
import java.util.function.Function;
/**
 * 
 * Class representing a membership function of a fuzzy set. 
 * 
 * <p>
 * For any set X, a membership function on X is any function from X to the real
 * unit interval [0,1]. The value 0 means that x is not a member of the fuzzy
 * set; the value 1 means that x is fully a member of the fuzzy set. The values
 * between 0 and 1 characterize fuzzy members, which belong to the fuzzy set
 * only partially.
 * 
 * @param <D> domain of the membership function
 * @author Jesús Chamorro Martínez (jesus@decsai.ugr.es)
 */
@FunctionalInterface 
public interface MembershipFunction<D> extends Function<D, Double>, AlphaCuttable {
    /**
     * By default, the alpha-cut associated to a membership function is null
     * (which implies that any alpha-cut of the fuzzy set associated to this
     * membeship function is also null).
     *
     * The way of calculating the alpha-cut will depend on the function, so its
     * implementation is left to the subclases. Depending on the complexity of
     * the function, it will not always be available; nevertheless, an answer is
     * needeed if an alpha-cut is required to a function-based fuzzy set: for 
     * this reason, it is assumed the <tt>null</tt> return understood as 
     * "no alpha-cut".
     * 
     * In addition, in order to keep the behavior of a functional interface, 
     * only one abstract method is allowed, so default implementation is needed
     * for the other ones.
     *
     * @param alpha the alpha value.
     * @return the alpha-cut.
     */
    @Override
    default public <R> R alphaCut(double alpha){
        return null;
    }  
}
