package jfi.fuzzy;

public interface FuzzyElement<Domain>{	//Segun Dani, ElementWithMembership; otra opción: Degreeable

    /**
     * Return the element
     * @return the element
     */
    public Domain getElement();
    
    /**
     * Return the membership degree of the object
     * @return the membership degree of the object
     */
    public double getDegree();	
}
