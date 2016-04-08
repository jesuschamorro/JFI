package jfi.fuzzy.cardinal;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import jfi.fuzzy.DiscreteFuzzySet;

/**
 * Class representing the fuzzy cardinal ED defined in:
 * 
 * <cite> M.Delgado, M.J. Martin-Bautista, D. Sanchez, M.A, Vila. "A Probabilistic 
 * Definition of a Nonconvex Fuzzy Cardinality". Fuzzy Sets and Systems. 126(2).
 * pp.41-54. 2002</cite>
 * 
 * @author Jesús Chamorro Martínez (jesus@decsai.ugr.es)
 */
public class EDCardinal {
    /**
     * The cardinal data member
     */
    private final LinkedHashMap<Integer, Double> cardinalMap = new LinkedHashMap<>(); //Ordered map
    
    /**
     * Constructs a new ED fuzzy cardinal 
     * @param fset the fuzzy set 
     */
    public EDCardinal(DiscreteFuzzySet fset){
        calculateCardinal(fset);
    } 
    
    /**
     * Calculates the ED fuzzy cardinal
     * 
     * @param fset the fuzzy set 
     */
    private void calculateCardinal(DiscreteFuzzySet fset){
        Double alpha_i, alpha_iplus1, value;
        BigDecimal difference;
        int i;
        
        if(fset.size()>0){
            ArrayList<Entry> entryList = new ArrayList(fset.entrySet());
            Comparator comp = Map.Entry.comparingByValue();
            entryList.sort(Collections.reverseOrder(comp)); //Descending order
            Double degree = (Double)entryList.get(0).getValue();
            if(degree < 1.0){  //Checking the case of alpha-cut=1.0
               BigDecimal d = new BigDecimal("1.0").subtract(new BigDecimal(degree.toString()));
               cardinalMap.put(0, d.doubleValue() );
            }
            for (i = 0; i < entryList.size() - 1; i++) {
                alpha_i = (Double) entryList.get(i).getValue();
                alpha_iplus1 = (Double) entryList.get(i + 1).getValue();
                //For decimal precision, BigDecimal is used to calculate (alpha_i - alpha_iplus1): 
                difference = new BigDecimal(alpha_i.toString()).subtract(new BigDecimal(alpha_iplus1.toString()));
                if((value=difference.doubleValue())>0)
                    cardinalMap.put(i+1, value);
            }
            if((value=(Double)entryList.get(i).getValue())>0)
                cardinalMap.put(i+1, value); //The last alpha-cut
        }
    }
    
    /**
     * Returns a string representation of this cardinal.
     * @return 
     */
    @Override
    public String toString(){
        String output = "";
        for(Entry<Integer,Double> e:cardinalMap.entrySet()){
            output += "["+e.getValue()+"/"+e.getKey()+"] ";
        }
        return output;
    }
}
