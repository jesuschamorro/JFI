package jfi.fuzzy.resemblance;

import java.util.ArrayList;
import java.util.function.BiFunction;
import jfi.fuzzy.operator.Aggregation;
import jfi.fuzzy.operator.TNorm;
        
/**
 * Class representing a fuzzy resemblance operation.
 *
 * @param <T> the class of the elements to be compared.
 * 
 * @author Jesús Chamorro Martínez (jesus@decsai.ugr.es)
 */
@FunctionalInterface 
public interface ResemblanceOp<T> extends BiFunction<T, T, Double>{
     /**
     * Applies this resemblance operator to the given arguments.
     *
     * @param t the first element to be compared. 
     * @param u the second element to be compared.  
     * @return the resemblance result
     */
    @Override
    public Double apply(T t, T u);    
    
    /**
     * 
     * @param <T>
     */
    public class Collection<T> extends ArrayList<ResemblanceOp> implements ResemblanceOp<T>{
        /**
         * 
         */
        protected Aggregation<Double, Double> aggregator;
        /**
         * 
         */
        private static final Aggregation<Double, Double> DEFAULT_AGGREGATOR = TNorm.MIN;
        
        /**
         * 
         * @param aggregator 
         */
        public Collection(Aggregation<Double, Double> aggregator){
            this.aggregator = aggregator!=null ? aggregator : DEFAULT_AGGREGATOR;
        }
        
        /**
         * Constructs a new collection of resemblance operators containing the
         * elements of the specified collection, in the order they are returned
         * by the collection's iterator.
         *
         * @param resemblance_operators the collection whose elements are to be
         * placed into this fuzzy sets collection.
         * @param aggregator the aggregator associted to this operator.
         * @throws NullPointerException if the specified collection is null.
         */
        public Collection(java.util.Collection<ResemblanceOp> resemblance_operators, Aggregation<Double, Double> aggregator) {
            super(resemblance_operators);
            this.aggregator = aggregator!=null ? aggregator : DEFAULT_AGGREGATOR;
        }

        
        /**
         * 
         * @param t
         * @param u
         * @return 
         */
        @Override
        public Double apply(T t, T u) { 
            Double resemblance = -1.0, resemblance_i;
            for(ResemblanceOp op:this){                
                resemblance_i = op.apply(t, u);
                resemblance = resemblance<0.0 ? resemblance_i : aggregator.apply(resemblance, resemblance_i);
            }
            return Math.max(0, resemblance); //If empty, 0 is returned
        }

        /**
         * 
         * @return 
         */
        public Aggregation getAggregator() {
            return aggregator;
        }

        /**
         *
         * @param aggregator
         */
        public void setAggregator(Aggregation<Double, Double> aggregator) {
            if (aggregator == null) {
                throw new NullPointerException("Null aggregator.");
            }
            this.aggregator = aggregator;
        }
    }
}
