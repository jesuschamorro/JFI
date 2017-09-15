package jfi.fuzzy.operators;

/**
 * Class representing an aggretation operation upon a list of operands.
 *
 * @param <T> the type of the operands.
 * @param <R> the type of the result of the aggregation.
 * 
 * @author Jesús Chamorro Martínez (jesus@decsai.ugr.es)
 */
@FunctionalInterface 
public interface Aggregation<T, R> {
    public R apply(T t, T... u);
}
