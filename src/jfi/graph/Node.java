package jfi.graph;

/**
 *
 * @param <T> domain of the property associated to this node
 * @author Jesús Chamorro Martínez (jesus@decsai.ugr.es)
 */
public class Node<T> {
    /**
     *
     */
    protected T source;
    
    /**
     * 
     * @param source 
     */
    public Node(T source){
        this.source = source;
    }
    
    /**
     * Returns the source associated to this node
     *
     * @return the node source
     */
    final public T getSource() {
        return source;
    }
    
    /**
     * Set the source of this node.
     * 
     * @param source the node source
     */
    final public void setSource(T source){
        this.source = source;
    }
    
}
