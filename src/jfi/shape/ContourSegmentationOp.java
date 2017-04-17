package jfi.shape;

import java.awt.geom.Point2D;
import java.util.ArrayList;

/**
 * Class implementing the segmentation of a contour.
 * 
 * @author Jesús Chamorro Martínez (jesus@decsai.ugr.es)
 */
public class ContourSegmentationOp {   
    /**
     * Minimun segment size.
     */
    int MINIMUN_SIZE = 10;
    /**
     * Sigma parameter used in the gaussian filtering.
     */
    private Double sigma;    
    /**
     * The curvature operator used for curvature estimation.
     */
    CurvatureOp curvatureOp;
    
    /**
     *  Constructs a new segmentation operator using the default parameters.
     */
    public ContourSegmentationOp(){
        this(null, CurvatureOp.DEFAULT_SEGMENT_SIZE_RATIO, CurvatureOp.DEFAULT_OFFSET);
    }
    
    /**
     * Constructs a new segmentation operator where a gaussian filtering is 
     * applied using the given sigma. For curvature estimation, the default 
     * parameters are used.
     * 
     * @param sigma the sigma parameter in the gaussian filtering.
     */
    public ContourSegmentationOp(Double sigma){
        this(sigma,CurvatureOp.DEFAULT_SEGMENT_SIZE_RATIO, CurvatureOp.DEFAULT_OFFSET);      
    }
    
    /**
     * Constructs a new segmentation operator where a gaussian filtering is 
     * applied using the given sigma. For curvature estimation, the given 
     * parameters are used.
     * 
     * @param sigma the sigma parameter in the gaussian filtering.
     * @param segmentSizeRatio the segment size ratio for curvature estimation. 
     * It must be a value between 0 an 1.
     * @param offset the offset for curvature estimation.
     */
    public ContourSegmentationOp(Double sigma, double segmentSizeRatio, int offset){
        this.setSigma(sigma);
        curvatureOp = new CurvatureOp(segmentSizeRatio,offset);
        //curvatureOp.setEstimationMethod(CurvatureOp.Approach.LINE_BASED_APPROACH);
        //curvatureOp.setEstimationMethod(CurvatureOp.Approach.LINE_BASED_APPROACH_IMPROVED);
        curvatureOp.setEstimationMethod(CurvatureOp.Approach.LINE_BASED_APPROACH_SOTO);
        //curvatureOp.setEstimationMethod(CurvatureOp.Approach.STANDAR_APPROACH);
    }
    
    /**
     * Set the sigma parameter for the gaussian filtering. If it is <tt>null</tt>,
     * no filtering is applied.
     * 
     * @param sigma the sigma parameter for the gaussian filtering
     */
    public final void setSigma(Double sigma){
        this.sigma = sigma;
    }
    
    /**
     * Returns the sigma parameter used for the gaussian filtering.
     * 
     * @return the sigma parameter
     */
    public double getSigma(){
        return sigma;
    }
    
    /**
     * Set the parameters used for curvature estimation.
     * 
     * @param segmentSizeRatio the segment size ratio for curvature estimation. 
     * It must be a value between 0 an 1.
     * @param offset the offset for curvature estimation.
     */
    public final void setCurvatureParameters(double segmentSizeRatio, int offset){
        curvatureOp = new CurvatureOp(segmentSizeRatio,offset);
    }
    
    /**
     * Apply this operator to the given contour.
     * 
     * @param contour the contour to be analyzed
     * @return the contour segmentation
     */
    public ContourSegmentation apply(Contour contour){
        ContourSegmentation segmentation = new ContourSegmentation(contour);
        if(contour!=null && curvatureOp!=null){
            //Firstly, we caculate the segment endpoits            
            ArrayList<Point2D> endpoints = this.endPoints(contour);
            //Secondly, we create the contour segments. 
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
     * Calculates the segment endpoints as the zero cross of the curvature.
     * 
     * @param contour the contour to be analyzed
     * @return the segment endpoints
     */
    private ArrayList<Point2D> endPoints(Contour contour) {
        ArrayList<Point2D> endpoints = new ArrayList<>();
        Contour filtered_contour = contour;
        if (sigma != null) {
            filtered_contour = contour.gaussianFiltering(sigma);
        }
        CurvatureFunction curvature = curvatureOp.apply(filtered_contour);
        Double left_value, right_value, central_value;
        boolean isPseudoInflection;
        int length = 0;
        for (int i = 0; i < filtered_contour.size(); i++) {
            left_value = curvature.apply(prevIndex(i, filtered_contour));
            right_value = curvature.apply(nextIndex(i, filtered_contour));
            central_value = curvature.apply(i);
            length++;            
            //Quantization
            //left_value = JFIMath.round(left_value);
            //right_value = JFIMath.round(right_value);
            //central_value = JFIMath.round(central_value);
            // We have a pseudo-inflection point if there is a zero cross 
            // (different sign), or the point is zero and the previous (or next)
            // one is not zero
            isPseudoInflection = left_value*right_value < 0 || (central_value==0 && left_value!=0) || (central_value==0 && right_value!=0);
            if (isPseudoInflection && length>MINIMUN_SIZE){ 
                endpoints.add(contour.get(i)); // Note that endpoints are points of the original contour
                length = 0;
            }
        }
        return endpoints;
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
