package jfi.shape;

import java.awt.geom.Point2D;
import java.util.ArrayList;

/**
 *
 * @author Jesús Chamorro Martínez (jesus@decsai.ugr.es)
 */
public class ContourSegmentationOp {
    private Double sigma = null;
    private Double windowSizeRatio = Contour.DEFAULT_WINDOW_RATIO_SIZE;
    private int offset = 5;
    
    /**
     * 
     */
    public ContourSegmentationOp(){
        this(null);
    }
    
    /**
     * 
     * @param sigma 
     */
    public ContourSegmentationOp(Double sigma){
        this.sigma = sigma;
    }
    
    public ContourSegmentation apply(Contour contour){
        ContourSegmentation segmentation = new ContourSegmentation(contour);
        
        if(contour!=null){
            Contour filtered_contour = contour;
            if(sigma!=null){
                filtered_contour = contour.gaussianFilter(sigma);
            }
            
            CurvatureOp curvatureOp = new CurvatureOp(windowSizeRatio,offset);
            //curvatureOp.setEstimationMethod(CurvatureOp.Approach.LINE_BASED_APPROACH);
            //curvatureOp.setEstimationMethod(CurvatureOp.Approach.LINE_BASED_APPROACH_IMPROVED);
            curvatureOp.setEstimationMethod(CurvatureOp.Approach.LINE_BASED_APPROACH_SOTO);
            CurvatureFunction curvature = curvatureOp.apply(contour);
            
            //Firstly, we caculate the segment endpoits as the zero cross of the curvature
            ArrayList<Point2D> endpoints = new ArrayList<>();
            Double left_value,right_value,central_value;            
            for(int i=0; i<filtered_contour.size(); i++){
                left_value = curvature.apply(prevIndex(i,filtered_contour));
                right_value = curvature.apply(nextIndex(i,filtered_contour));
                central_value = curvature.apply(i);
                if(left_value*right_value<0 && Math.abs(central_value)>0.001 ) { // Different sign: zero cross
                    endpoints.add(contour.get(i));
                    //endpoints.add(filtered_contour.get(i));
                    //System.out.print(i+"("+filtered_contour.get(i).getX()+","+filtered_contour.get(i).getY()+") ");
                }
            }    
            // Secondly, we create the contour segments. Note that the endpoints are poits of the original
            // contour (no the filtered one)
            if (!endpoints.isEmpty()) {
                ContourSegment segment;
                for (int p = 0; p < endpoints.size() - 1; p++) {
                    segment = new ContourSegment(endpoints.get(p), endpoints.get(p + 1), contour);
                    segmentation.add(segment);
                }
                segment = new ContourSegment(endpoints.get(endpoints.size() - 1), endpoints.get(0), contour);
                segmentation.add(segment);
            }
        }
        return segmentation;
    }
    
    /**
     * Returns the next index in the given contour.
     *
     * @return the next index in the given contour.
     */
    private int nextIndex(int index, Contour contour) {
        return contour.isClockwise() ? (index + 1) % contour.size() : (index - 1 + contour.size()) % contour.size();
    }
    
    /**
     * Returns the previous index in the given contour.
     *
     * @return the previous index in the given contour.
     */
    private int prevIndex(int index, Contour contour) {    
        return contour.isClockwise() ? (index - 1 + contour.size()) % contour.size() : (index + 1) % contour.size();
    }
}
