package jfi.color.fuzzy;

import java.awt.Color;
import jfi.fuzzy.FuzzySet;

/**
 * A fuzzy color is a fuzzy subsets of colors.
 *
 * <p>
 * A generic type <tt>T</tt> is introduced for customizing the way a crisp color
 * is represented (in other words, to specify the domain of the fuzzy set).
 * Although it is well known that the {@link java.awt.Color} class is the
 * standard class for representing a color in Java, a diferent one could be used
 * (for example, a three-dimensional point storing three color componets).
 * Nevertheless, by default, the membeship degree for a standard crisp
 * {@link java.awt.Color} must be provided for any fuzzy color.
 *
 * @param <T> the type in which the crisp color is represented.
 *
 * @author Jesús Chamorro Martínez (jesus@decsai.ugr.es)
 */
public interface FuzzyColor<T> extends FuzzySet<T>{
    /**
     * Returns the membership degree of a crisp color to this fuzzy color.
     *
     * @param c an crisp color.
     * @return the membership degree.
     */
    public double membershipDegree(Color c);
}
