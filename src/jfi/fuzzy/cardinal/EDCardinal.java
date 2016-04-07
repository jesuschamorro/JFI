package jfi.fuzzy.cardinal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.Map.Entry;
import jfi.fuzzy.DiscreteFuzzySet;

/**
 * Class representing the fuzzy cardinal ED defined as:
 * 
 * ED(F) = SUM_(a_i){(a_i - a_(i+1))/|Fa_i|}
 * 
 * with F being a fuzzy set, {a_1,...,a_m} the representative alpha-cuts of F, 
 * and with a_1=1.0 > and a_(m+1)=0.0
 * 
 * @author Jesús Chamorro Martínez (jesus@decsai.ugr.es)
 */
public class EDCardinal {
    
    ArrayList<EDCardinalItem> cardinal ;
    
    public EDCardinal(DiscreteFuzzySet fset){
                
        ArrayList<Entry> entryList = new ArrayList(fset.entrySet());
        Comparator comp = Map.Entry.comparingByValue();
        entryList.sort(Collections.reverseOrder(comp)); //Descending order
        
        double prev_degree=0.0, difference, alfa_i, alfa_iplus1;
        int count = 0;
        
        if(entryList.size()>0){
            //Checking the case of alpha-cut=1.0
            double degree = (Double)entryList.get(0).getValue();
            if(degree < 1.0)         
               cardinal.add(new EDCardinalItem(0, 1.0-degree));
        }
        
        for(int i=0; i<entryList.size()-1; i++){
              
           alfa_i = (Double)entryList.get(i).getValue();
           alfa_iplus1 = (Double)entryList.get(i+1).getValue();
           difference = alfa_i - alfa_iplus1;
           
           cardinal.add(new EDCardinalItem(i, difference));
        }
        
        
    } 
    
    public class EDCardinalItem{
        public double probability;
        public int cardinal;
        
        public EDCardinalItem(int cardinal, double probability) {
            this.cardinal = cardinal;
            this.probability = probability;
        }
    }
}
