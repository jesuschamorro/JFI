/*
  Discrete function representing the curvature of a contour

  @author Jesús Chamorro Martínez - UGR
*/
package jfi.shape;

import java.util.ArrayList;
import java.util.function.Function;


public class CurvatureFunction implements Function<Integer,Double>{
    /**
     * Curvature values
     */
    private final ArrayList<Double> curvature;
    /**
     * Construct an empty curvature function 
     */
    public CurvatureFunction(){
        curvature = new ArrayList();
    }
    
    /**
     * Applies the function to the given argument.
     *
     * @param t the function argument
     * @return the function result
     */
    @Override
    public Double apply(Integer t) {
        return curvature.get(t); 
    }
    
    /**
     * Add the specified value to the end of the x-domain list.
     * @param value value of the function to be appened
     */
    public void add(Double value){
        curvature.add(value);
    }
    
    /**
     * Add the specified element at the specified position in the x-domain list
     * Shifts the value currently at that position (if any) and
     * any subsequent elements to the right (adds one to their indices).
     *
     * @param index index at which the specified element is to be set
     * @param value element to be inserted
     * @throws IndexOutOfBoundsException {@inheritDoc}
     */
    public void add(Integer index, Double value){
        curvature.add(index, value);        
    }
    
    /**
     * Replaces the value at the specified position in the x-domain list with
     * the specified element.
     *
     * @param index index of the element to replace
     * @param value value to be stored at the specified position
     * @throws IndexOutOfBoundsException {@inheritDoc}
     */
    public void set(Integer index, Double value){
        curvature.set(index, value);
    }
    
    /**
     * Removes the value at the specified position.
     * Shifts any subsequent elements to the left (subtracts one from their
     * indices).
     *
     * @param index the index of the value to be removed
     * @throws IndexOutOfBoundsException {@inheritDoc}     * 
     */
    public void remove(Integer index){
        curvature.remove((int)index);
    }
    
    /**
     * Removes all of the values 
     */
    public void clear(){
        curvature.clear();
    }
    
    /**
     * Returns the number of elements in the x-domain.
     *
     * @return the number of elements in the x-domain
     */
    public int size(){
        return curvature.size();
    }
    
    /**
     * Returns an array containing all of the values of the function
     * in proper sequence (from first to last element).
     * @return an array containing all of the elements in this list in
     *         proper sequence
     */
    public Double[] toArray(){      
        Double[] array = new Double[curvature.size()]; 
        curvature.toArray(array);        
        return array;
    }
    
    /**
     * Scale de function in the X-domain 
     * @param new_dimensionX the new dimension in X axis
     */
    public void scale(int new_dimensionX){       
        //TODO
    }
    
    /**
     * Calculate the maximum value of the curvature function
     * @return the maximum value of the function
     */
    public double max(){
        double max=0.0;       
        for(int i = 0; i < this.size(); i++){
            if (max < Math.abs(this.apply(i)))
                max = Math.abs(this.apply(i));
        }       
        return max;
    }
    
    /**
     * Normalize the curvature function dividing by cte_normalization.
     * 
     * @param cte_normalization normalization constant
     */
    public void normalize(double cte_normalization){    
        for(int i = 0; i < this.size(); i++){
            this.set(i, this.apply(i)/cte_normalization);
        }     
    }
    
    /**
     * Normalize the curvature function dividing by the maximum
     */
    public void normalize(){
        normalize(this.max());
    }
}
