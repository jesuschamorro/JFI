package jfi.shape;

import java.awt.geom.Point2D;
import java.security.InvalidParameterException;
import java.util.ArrayList;

/**
 * Class implementing contour filtering operations
 * 
 * @author Jesús Chamorro Martínez (jesus@decsai.ugr.es)
 */
public class ContourFilteringOp {

    /**
     * Mask to be applied in the filtering operation.
     */
    protected ArrayList<Double> kernel;


    /**
     * Constructs a new filtering operator.
     * 
     * @param kernel the filtering kernel
     */
    public ContourFilteringOp(ArrayList<Double> kernel){
        this.setKernel(kernel);
    }

    /**
     * Set the mask associated to this filtering operator
     * 
     * @param kernel the filtering mask
     */
    public final void setKernel(ArrayList<Double> kernel){
        this.kernel = kernel;
    }
    
    /**
     * Filter the given contour applying the mask associated to this operator.
     *      * 
     * @param contour the contour to be filtered
     * 
     * @return the filtered contour (the original one if the mask is set to <tt>null</tt>) 
     */
    public Contour apply(Contour contour){
        if(kernel==null){
            return contour;
        }
        Point2D.Double point;
        Contour filteredContour = new Contour();       
        for(int i = 0; i < contour.size(); i++){
            point = new Point2D.Double(0.0,0.0);
            for(int j = 0; j < kernel.size(); j++){
                point.x += contour.get((i+j-kernel.size()/2+contour.size())%contour.size()).getX()* kernel.get(j);
                point.y += contour.get((i+j-kernel.size()/2+contour.size())%contour.size()).getY()* kernel.get(j);
            }
            filteredContour.add(point);
        }
        return filteredContour;
    }
    
    /**
     * Creates a new Gaussian kernel
     * 
     * @param kernelSize size of the kernel
     * @param sigma standard deviation of the gaussian function
     * 
     * @return the gaussian kernel
     */
    static public ArrayList<Double> gaussianKernel(int kernelSize, double sigma){
        ArrayList<Double> kernel = new ArrayList(kernelSize);
        
        double sum = 0;
        double x2, gaussianValue;
        double exp_denominator = 2.0*Math.pow(sigma,2);
        for(int i = 0; i < kernelSize; i++){
            x2 = Math.pow(i-(kernelSize/2),2);
            gaussianValue = Math.exp(-(x2/exp_denominator));
            sum += gaussianValue;
            kernel.add(gaussianValue);
        }
        for(int i = 0; i < kernelSize; i++){
            kernel.set(i,kernel.get(i)/sum);
        }
        return kernel;
    }
    
    /**
     * Creates a new Gaussian kernel. The kernel size is automatically calculated
     * on the basis of the sigma parameter.
     * 
     * @param sigma standard deviation of the gaussian function
     * 
     * @return filtered contour
     */
    static public ArrayList<Double> gaussianKernel(double sigma){
        return gaussianKernel((int) Math.ceil(sigma*1.96)*2+1,sigma);
    }
    
    
    
    /**
     * Creates a new Gaussian kernel.
     * 
     *  ******************************
     *  *** Resolver dudas con Antonio
     *  ******************************
     * 
     * @param kernelSize size of the kernel
     * @param sigma standard deviation of the gaussian function
     * 
     * @return the gaussian kernel
     */
    private ArrayList<Double> _gaussianKernel(int kernelSize, double sigma) {
        ArrayList<Double> kernel = new ArrayList(kernelSize);
        
        double sum = 0;
        double x, gaussianValue;
        double exp_denominator = 2.0 * sigma * sigma;
        for (int i = 0; i < kernelSize; i++) {
            double left = i - kernelSize / 2 - 0.45;                    // -0.45 ???
            gaussianValue = 0;
            for (int nsam = 0; nsam < 10; nsam++) {                     // nsam? 10?
                x = left + nsam * 0.1;
                gaussianValue += Math.exp(-x * x / exp_denominator);    // Acumulado?
                sum += gaussianValue;                                   // Suma de acumulado?
            }
            kernel.add(gaussianValue);
        }
        for (int i = 0; i < kernelSize; i++) {
            kernel.set(i, kernel.get(i) / sum);
        }
        return kernel;
    }
    
    /**
     * Creates a first order derivative Gaussian kernel.
     * 
     * @param kernelSize size of the kernel
     * @param sigma standard deviation of the gaussian function
     * 
     * @return the gaussian kernel
     */
    private ArrayList<Double> gaussianFirstDerivativeKernel(int kernelSize, double sigma) {
        ArrayList<Double> kernel = new ArrayList(kernelSize);

        double sum_p = 0.0; // area under sampled curve for positive values
        double sum_n = 0.0; // area under sampled curve for negative values
        double exp_denominator = 2.0 * sigma * sigma;
        double gaussianValue;
        for (int i = 0; i < kernelSize; i++) {
            double left = i - kernelSize / 2 - 0.5;
            double right = i - kernelSize / 2 + 0.5;
            gaussianValue = Math.exp(-right*right / exp_denominator) - Math.exp(-left*left / exp_denominator);
            if (gaussianValue > 0.0) {
                sum_p += gaussianValue;
            } else {
                sum_n += gaussianValue;
            }
            kernel.add(gaussianValue);
        }

        double norm_p = 0.5/sum_p;
        double norm_n = -0.5/sum_n;
        for (int i = 0; i < kernelSize; i++) {
            if (kernel.get(i) > 0.0) {
                kernel.set(i, kernel.get(i) * norm_p);
            } else {
                kernel.set(i, kernel.get(i) * norm_n);
            }
        }

        return kernel;
    }
    
    
    /**
     * Creates a second order derivative Gaussian kernel.
     * 
     * 
     * ********************************************************************
     * Duda Antonio: la única diferencia es el cálculo del valor gaussiano?
     * ********************************************************************
     * 
     * 
     * @param kernelSize size of the kernel
     * @param sigma standard deviation of the gaussian function
     * 
     * @return the gaussian kernel
     */
    private ArrayList<Double> gaussianSecondDerivativeKernel(int kernelSize, double sigma) {
        ArrayList<Double> kernel = new ArrayList(kernelSize);

        double sum_p = 0.0; // area under sampled curve for positive values
        double sum_n = 0.0; // area under sampled curve for negative values
        double exp_denominator = 2.0 * sigma * sigma;
        double gaussianValue;

        for (int i = 0; i < kernelSize; i++) {
            double left = i - kernelSize / 2 - 0.5;
            double right = i - kernelSize / 2 + 0.5;
            gaussianValue = (-right) * Math.exp(-right * right / exp_denominator)
                    - (-left) * Math.exp(-left * left / exp_denominator);
            if (gaussianValue > 0.0) {
                sum_p += gaussianValue;
            } else {
                sum_n += gaussianValue;
            }
            kernel.add(gaussianValue);
        }

        double norm_p = 0.5 / sum_p;
        double norm_n = -0.5 / sum_n;
        for (int i = 0; i < kernelSize; i++) {
            if (kernel.get(i) > 0.0) {
                kernel.set(i, kernel.get(i) * norm_p);
            } else {
                kernel.set(i, kernel.get(i) * norm_n);
            }
        }

        return kernel;
    }
    
    /**
     * 
     * @param sigma
     * @param diff
     * @return 
     */
    private ArrayList<Double> generateGaussianFilter(double sigma, int diff) {
        int kernelSize = (int) (7 * sigma);
        if (kernelSize % 2 == 0) {
            kernelSize++;
        }

        ArrayList<Double> kernel = new ArrayList(kernelSize);
        double dos_sig2 = 2.0 * sigma * sigma;

        if (diff == 0) {
            kernel = _gaussianKernel(kernelSize,sigma);
        } else if (diff == 1) {            
            kernel = gaussianFirstDerivativeKernel(kernelSize,sigma);
        } else if (diff == 2) {
            kernel = gaussianSecondDerivativeKernel(kernelSize,sigma);
        } else if (diff == 3) {
            double sum_p = 0.0; // area under sampled curve for positive values
            double sum_n = 0.0; // area under sampled curve for negative values
            // La integral es la primera derivada de gausiana
            double gaussianValue;

            for (int i = 0; i < kernelSize; i++) {
                double left = i - kernelSize / 2 - 0.5;
                double right = i - kernelSize / 2 + 0.5;
                gaussianValue = (right * right - sigma * sigma) * Math.exp(-right * right / dos_sig2)
                        - (left * left - sigma * sigma) * Math.exp(-left * left / dos_sig2);
                if (gaussianValue > 0.0) {
                    sum_p += gaussianValue;
                } else {
                    sum_n += gaussianValue;
                }
                kernel.add(gaussianValue);
            }

            double norm_p = 0.5 / sum_p;
            double norm_n = -0.5 / sum_n;
            for (int i = 0; i < kernelSize; i++) {
                if (kernel.get(i) > 0.0) {
                    kernel.set(i, kernel.get(i) * norm_p);
                } else {
                    kernel.set(i, kernel.get(i) * norm_n);
                }
            }
        } else {
            throw new InvalidParameterException("Max diff for gaussian kernel generation is 3.");
        }
        
        return kernel;
    }
    
    
    
}
