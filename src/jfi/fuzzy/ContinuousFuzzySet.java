package jfi.fuzzy;

import jfi.fuzzy.membershipfunction.MembershipFunction;


/**
 *
 * @author Jesús
 * @param <Domain>
 */
public abstract interface ContinuousFuzzySet<Domain> extends FuzzySet<Domain>{
    public MembershipFunction getMembershipFunction();
    
    
}
