package jfi.fuzzy.membershipfunction;

import java.util.function.Function;
import jfi.utils.Interval;
/**
 * 
 * @author Jesús Chamorro Martínez <jesus@decsai.ugr.es>
 * @param <Domain> domain of the membership function
 */
public interface MembershipFunction<Domain> extends Function<Domain, Double> {
    /**
     * Return an alpha-cut associated to the membership function
     *
     * @param alpha the alpha
     * @return the alpha-cut
     */
    public Interval<Number> getAlphaCut(double alpha);
}
