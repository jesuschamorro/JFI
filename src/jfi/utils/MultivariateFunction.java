package jfi.utils;

/**
 * Represents a function that accepts a miltivariable argument and produces a
 * result.
 *
 * <p>
 * This is a functional interface whose functional method is
 * {@link #apply(java.lang.Object, java.lang.Object...) }.
 *
 * @param <D> the type of the input to the function.
 * @param <R> the type of the result of the function.
 */
@FunctionalInterface
public interface MultivariateFunction<D, R> {

    /**
     * Applies this function to the given argument.
     *
     * @param x1 the first element of the sequence.
     * @param x2toN the rest of element in the sequence.
     * @return the function result
     */
    R apply(D x1, D... x2toN);
}
