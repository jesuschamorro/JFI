package jfi.fuzzy;

public interface FuzzyElement<Domain> extends Fuzzy{	

    /**
     * Return the element
     * @return the element
     */
    public Domain getElement();
}
