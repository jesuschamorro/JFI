package jfi.image;

import java.awt.Point;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.WritableRaster;
import java.security.InvalidParameterException;
import jfi.fuzzy.AlphaCuttable;
import jfi.fuzzy.FuzzySet;

/**
 * Class representing a fuzzy image where each pixel have a membership degree.
 * It extends the standard {@link java.awt.image.BufferedImage} class
 * (inheriting its properties and behavior) and, in addition, implements the
 * {@link jfi.fuzzy.FuzzySet} interface (i.e, it is a fuzzy set o pixels).
 *
 * A fuzzy image can be used as an standard one (for displaying, filtering,
 * write on a file, etc.) and, in addition, it stores information about the
 * membership degree of each pixel. If alpha component is used, it will be
 * related to the membership degree: the pixel transparency will be proportional
 * to the degree of that pixel, being opaque if the membership degree is 1.0,
 * and full transparent if it is 0.0.
 * 
 * As a fuzzy set, we can get and set the membership degree of a given pixel,
 * calculate its alpha-cut, etc.
 *
 * @author Jesús Chamorro Martínez (jesus@decsai.ugr.es)
 */
public class FuzzyImage extends BufferedImage implements FuzzySet<Point>, AlphaCuttable{
    /**
     * The label associated to the fuzzy set.
     */
    protected String label;
    /**
     * The membership degree associated to each pixel.
     */
    private double degree[][];    
    /**
     * Represents an 8-bit RGB color image with the alpha component related to 
     * the membership degree. The pixel transparency will be proportional to the 
     * degree of that pixel, being opaque if the membership degree is 1.0, and 
     * full transparent if it is 0.0. 
     * 
     * This image has a {@link java.awt.image.DirectColorModel} with alpha and
     * uses the {@link java.awt.color.ColorSpace#CS_sRGB} color space. The color 
     * data in this image is not premultiplied with alpha.
     * 
     */
    public static final int TYPE_FUZZY_ALPHA_RGB = 101;
    /**
     * Represents a unsigned byte grayscale image with an alpha component
     * related to the membership degree. The pixel transparency will be
     * proportional to the degree of that pixel, being opaque if the membership
     * degree is 1.0, and full transparent if it is 0.0.
     *
     * This image has a {@link java.awt.image.ComponentColorModel} with a
     * {@link jfi.image.GreyColorSpace} color space. It uses two bands: one for
     * the grey level and another for the alpha value. Note that it does not use
     * the default {@link java.awt.color.ColorSpace#CS_GRAY} color space to
     * avoid the internal transformation to/from the CIEXYZ of the later (and
     * its darkering of the image).
     */
    public static final int TYPE_FUZZY_ALPHA_GREY = 102;
    /**
     * Represents a unsigned byte grayscale image without alpha component. In
     * that case, the grey level will be proportional to the degree of that
     * pixel, being black (grey level 0) if the membership degree is 1.0, and
     * white (grey level 255) if it is 0.0.
     *
     * This image has a {@link java.awt.image.ComponentColorModel} with a
     * {@link jfi.image.GreyColorSpace} color space and only one band. Note that
     * it does not use the default {@link java.awt.color.ColorSpace#CS_GRAY}  
     * color space to avoid the internal transformation to/from the CIEXYZ of 
     * the later (and its darkering of the image).
     */
    public static final int TYPE_FUZZY_NOALPHA_GREY = 103;
    /**
     * The image raster where the degrees are stored. 
     */
    private WritableRaster degreeRaster;
    
    /**
     * Constructs a new fuzzy image of one of the predefined image types and 
     * with the given size. By default, the membership degrees are initialized
     * to 1.0 and the image is white filled.
     * 
     * @param width the width of the image. 
     * @param height the height of the image.
     * @param imageType type of the fuzzy image (from the list showed below).
     * 
     * @see #TYPE_FUZZY_ALPHA_RGB
     * @see #TYPE_FUZZY_ALPHA_GREY
     * @see #TYPE_FUZZY_NOALPHA_GREY
     */
    public FuzzyImage(int width, int height, int imageType){        
        super(createColorModel(imageType),createRaster(width,height,imageType),false,null);
        // The image is white filled. If the image type is grey without alpha, 
        // this value will be related with a membership degree of 1.0 (that is
        // set below); if it has alpha, it will be set to 255 (opaque) and we  
        // assume the white as default background 
        createGraphics().fillRect(0,0,width,height);
        //The membership degree of each pixel is initialized to 1.0. This will
        //correspond to an alpha value of 255 (if available).
        degree = new double[width][height];
        initDegreeData(); // Set membeship degrees to 1.0 (alpha is already 255)
        degreeRaster = getColorModel().hasAlpha() ? getAlphaRaster() : getRaster();
    }
    
    /**
     * Constructs a new fuzzy image with the given size using the default image
     * type {@link #TYPE_FUZZY_NOALPHA_GREY}
     * 
     * @param width the width of the image 
     * @param height the height of the image
     */
    public FuzzyImage(int width, int height) {
        this(width, height, TYPE_FUZZY_NOALPHA_GREY);
    }
    
    /**
     * Constructs a new fuzzy image from a source image. If the source image is
     * of type {@link java.awt.image.BufferedImage#TYPE_BYTE_GRAY}, a fuzzy
     * image of type {@link #TYPE_FUZZY_ALPHA_GREY} is created; else, a
     * {@link #TYPE_FUZZY_ALPHA_RGB} one is constructed. If the source image has 
     * alpha component, the transparent pixels are drawn in white color.
     * 
     * @param src the source image. 
     */
    public FuzzyImage(BufferedImage src){
        this(src.getWidth(),src.getHeight(),
                src.getType()==BufferedImage.TYPE_BYTE_GRAY?
                TYPE_FUZZY_ALPHA_GREY:TYPE_FUZZY_ALPHA_RGB);
        createGraphics().drawImage(src,0,0,null); //Revisar: copia alfa 
    }    
    
    /**
     * Creates a color model according to the specified type.
     * 
     * @param imageType the fuzzy image type.
     * @return a color model according to the specified type.
     */
    private static ColorModel createColorModel(int imageType) {
        switch (imageType) {
            case TYPE_FUZZY_ALPHA_RGB:
                return ColorModel.getRGBdefault();
            case TYPE_FUZZY_ALPHA_GREY:
                int[] nBits_ag = {8, 8};
                return new ComponentColorModel(new GreyColorSpace(), nBits_ag, 
                        true, true, Transparency.TRANSLUCENT, DataBuffer.TYPE_BYTE);
            case TYPE_FUZZY_NOALPHA_GREY:
                int[] nBits_nag = {8};
                return new ComponentColorModel(new GreyColorSpace(), nBits_nag, 
                        false, true, Transparency.OPAQUE, DataBuffer.TYPE_BYTE);
            default:
                throw new IllegalArgumentException("Unknown fuzzy image type");
        }
    }

    /**
     * Creates an image raster according to the specified type.
     * 
     * @param width width of the raster.
     * @param height height of the raster.
     * @param imageType the fuzzy image type.
     * @return an image raster according to the specified type. 
     */
    private static WritableRaster createRaster(int width, int height, int imageType) {
        ColorModel cm = createColorModel(imageType);
        return cm.createCompatibleWritableRaster(width, height);
    }
    
    /**
     * Initializes the degree matrix to 1.0 
     */
    private void initDegreeData() {       
        for (int x = 0; x < this.getWidth(); x++) {
            for (int y = 0; y < this.getHeight(); y++) {
                this.degree[x][y] = 1.0;
            }
        }
    }
    
    /**
     * Fill the membeship degree data with the given value.
     *
     * @param degree the value used to initialize the memberdhip degree data.
     */
    public void fillDegreeData(double degree) {
        if(degree<0.0 || degree>1.0){
            throw new InvalidParameterException("The degree must be between 0 and 1");
        }
        for (int x = 0; x < this.getWidth(); x++) {
            for (int y = 0; y < this.getHeight(); y++) {
                this.degree[x][y] = degree;
                this.degreeRaster.setSample(x, y, 0, (byte)(degree*255));
            }
        }
    }
    
    /**
     * Forces the alpha component (or the grey level, if alpha is not available) 
     * to match the membership degrees of this fuzzy images. 
     * 
     * In a standard use of this class, both the alpha and the membeship degrees
     * are automatically synchorinzed. Only if the alpha component (or the grey
     * level, if alpha is not available) is externally modified without using
     * the <tt>setMembershipDegree</tt> method (bad practice), this method will
     * allow to forces the alpha component to match the membership degrees.
     */
    public void coerceAlpha(){
        for (int x = 0; x < this.getWidth(); x++) {
            for (int y = 0; y < this.getHeight(); y++) {
                degreeRaster.setSample(x, y, 0, (byte) (degree[x][y] * 255));
            }
        }
    }    

    /**
     * Return the label associated to the fuzzy set
     *
     * @return the label associated to the fuzzy set
     */
    @Override
    public String getLabel() {
        return label;
    }

    /**
     * Set the label associated to the fuzzy set
     *
     * @param label the new label
     */
    @Override
    public void setLabel(String label) {
        this.label = label;
    }

    /**
     * Return the membership degree of the pixel <tt>p</tt>.
     *
     * @param p the pixel
     * @return the membership degree
     */
    @Override
    public double membershipDegree(Point p) {
        return degree[p.x][p.y];
    }
    
    /**
     * Return the membership degree of the pixel of coordinates <tt>(x,y)</tt>.
     *
     * @param x x-coordinate of the pixel.
     * @param y x-coordinate of the pixel.
     * @return the membership degree
     */
    public double membershipDegree(int x, int y) {
        return degree[x][y];
    }
    
    /**
     * Set a new membership degree to the pixel <tt>p</tt>, if it exists 
     * 
     * @param p the element to be modified
     * @param degree the new membership degree
     * @return <tt>true</tt> if this image contains the specified pixel
     */
    public boolean setMembershipDegree(Point p, double degree){
        return this.setMembershipDegree(p.x,p.y, degree);
    } 
    
    /**
     * Set a new membership degree to the pixel of coordinates <tt>(x,y)</tt>, 
     * if it exists 
     * 
     * @param x x-coordinate of the pixel.
     * @param y x-coordinate of the pixel.
     * @param degree the new membership degree
     * @return <tt>true</tt> if this image contains the specified pixel
     */
    public boolean setMembershipDegree(int x, int y, double degree){
        if(degree<0.0 || degree>1.0){
            throw new InvalidParameterException("The degree must be between 0 and 1");
        }
        try {
            this.degree[x][y] = degree;
            this.degreeRaster.setSample(x, y, 0, (int)(degree * 255.0));
            return true;
        } catch (Exception ex) {
            return false;
        }
    } 

    /**
     * Returns the alpha-cut of the fuzzy image. Is is represented as an image 
     * of type {@link java.awt.image.BufferedImage#TYPE_BYTE_BINARY}, with a 
     * white value for pixels belonging to the alpha-cut (black if not).
     *
     * @param alpha the alpha.
     * @return the alpha-cut as a bibary image.
     * 
     */
    @Override
    public BufferedImage alphaCut(double alpha) {
        BufferedImage alphaImage = new BufferedImage(getWidth(),getHeight(),
                BufferedImage.TYPE_BYTE_BINARY);
        for (int x = 0; x < getWidth(); x++) {
            for (int y = 0; y < getHeight(); y++) {
                alphaImage.setRGB(x, y, degree[x][y]<alpha? 0 : 0x00ffffff);                
            }
        }
        return alphaImage;
    }
    
    /**
     * Returns the alpha-cut of the fuzzy image. It allows to retain the
     * original RGB (or grey) values in the pixels belonging to the alpha-cut by
     * means of the <tt>useOriginal</tt> parameter.
     *
     * @param alpha the alpha.
     * @param useOriginal if <tt>true</tt>, the original RGB (or grey) values
     * are retained.
     * @return the alpha-cut as a binary image, if <tt>useOriginal</tt> is 
     * <tt>flase</tt>, or a RGB one (if it is <tt>true</tt>).
     * 
     */
    public BufferedImage alphaCut(double alpha, boolean useOriginal) {
        if(!useOriginal){
            return this.alphaCut(alpha);
        }
        BufferedImage alphaImage = new BufferedImage(getWidth(),getHeight(),
                BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < getWidth(); x++) {
            for (int y = 0; y < getHeight(); y++) {
                alphaImage.setRGB(x, y, degree[x][y]<alpha? 0 : getRGB(x,y));                
            }
        }
        return alphaImage;
    }
}
