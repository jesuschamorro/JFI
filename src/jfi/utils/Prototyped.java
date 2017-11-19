package jfi.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Class representing a prototype-based object.
 * 
 * @param <T> the type of the prototype.
 * @author Jesús Chamorro Martínez (jesus@decsai.ugr.es)
 */
public interface Prototyped<T> {
    /**
     * Returns the main prototype associated to this object.
     * 
     * @return the main prototype associated to this object, <code>null</code>
     * if not available
     */
    public T getPrototype();
    
    /**
     * Returns all the prototypes associated to this object.
     * 
     * @return a list with all the prototypes associated to this object. If no 
     * prototypes are available, and empty list will be returned.
     */
    default public List<T> getAllPrototypes(){
        T prototype = getPrototype();
        return prototype!=null ? Arrays.asList(prototype) : new ArrayList(0); 
    }
}
