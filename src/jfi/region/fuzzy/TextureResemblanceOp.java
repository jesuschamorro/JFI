package jfi.region.fuzzy;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import jfi.fuzzy.operators.TNorm;
import jfi.image.fuzzy.FuzzyMappingOp;
import jfi.image.fuzzy.TiledFuzzyMappingOp;
import jfi.texture.fuzzy.FuzzyTexture;
import jfi.texture.fuzzy.FuzzyTextureFactory;

/**
 * Class representing a pixel resemblance operator where the resemblance is
 * calculated on the basis of fuzzy textures.
 *
 * @author Jesús Chamorro Martínez (jesus@decsai.ugr.es)
 */
public class TextureResemblanceOp implements PixelResemblanceOp<Point> {  
    /**
     * List of texture fuzzy set associated to this operator.
     */
    private ArrayList<FuzzyTexture> fuzzyTextures;
    /**
     * A source image associated to this operator (if available).
     */
    private BufferedImage source = null;
    /**
     * The width of the window used to calculate the texture measures.
     */
    private int width = 32;
    /**
     * Half of the previous width. It is used for efficiency purposes.
     */
    private int half_width = width/2;
    /**
     * The height of the window used to calculate the texture measures.
     */
    private int height = 32;
    /**
     * Half of the previous height. It is used for efficiency purposes
     */
    private int half_height = height/2;
     /**
     * T-norm used to aggregate.
     */
    TNorm tnorm = TNorm.MIN;
    /**
     * Default coarseness measure used for building the texture fuzzy set
     */
    private static final int DEFAULT_COARSENESS_TYPE = FuzzyTextureFactory.TYPE_COARSENESS_AMADASUN;
    /**
     * Flag associated to the Goguen implication operator.
     */
    public final static int GOGUEN_IMPLICATION_TYPE = 1;
    /**
     * Flag associated to the Lukasiewicz implication operator.
     */
    public final static int LUKASIEWICZ_IMPLICATION_TYPE = 2;
    /**
     * The default implication operator.
     */
    public static int DEFAULT_IMPLICATION_TYPE = GOGUEN_IMPLICATION_TYPE;
    /**
     * List of image maps.
     */
    private ArrayList<BufferedImage> maps = null;
    /**
     * Flag to set whether the points in the border are processed or not. 
     */
    boolean analyzeBorder = false;
    
    /**
     * Constructs a new texture resemblance operator using the default fuzzy
     * texture sets. The given image will be associated to this operator; this
     * implies that a series of preliminary calculations will be made in order
     * to make the application of this operator more efficient if the image on
     * which it is applied is the one passed in this constructor.
     *
     * @param source image associated to this operator. 
     */
    public TextureResemblanceOp(BufferedImage source) {
        this();               //The default fuzzy texture sets are built
        this.source = source;
        this.sourceMapping(); //The texture maps are calculated
    }
    
    /**
     * Constructs a new texture resemblance operator using as fuzzy textures the
     * ones given by parameter. The given image will be associated to this
     * operator; this implies that a series of preliminary calculations will be
     * made in order to make the application of this operator more efficient if
     * the image on which it is applied is the one passed in this constructor.
     *
     * @param source image associated to this operator.
     * @param ft the first fuzzy texture set.
     * @param fts the other fuzzy testure sets.
     */
    public TextureResemblanceOp(BufferedImage source, FuzzyTexture ft, FuzzyTexture... fts) {
        this(ft,fts);
        this.source = source;
        this.sourceMapping(); //The texture maps are calculated
    }    
    
    /**
     * Constructs a new texture resemblance operator using as fuzzy textures the
     * ones given by parameter.
     * 
     * @param ft the first fuzzy texture set.
     * @param fts the other fuzzy testure sets.
     */
    public TextureResemblanceOp(FuzzyTexture ft, FuzzyTexture... fts){
        this.fuzzyTextures =  new ArrayList(Arrays.asList(fts));          
        this.fuzzyTextures.add(0,ft);
    }
    
    /**
     * Constructs a new texture resemblance operator using the default fuzzy
     * texture sets.
     */
    public TextureResemblanceOp(){       
        this.fuzzyTextures =  new ArrayList();
        this.fuzzyTextures.add(FuzzyTextureFactory.getInstance(DEFAULT_COARSENESS_TYPE));
    }
        
    /**
     * Calculates the mappings of the source image. 
     */
    private void sourceMapping() {
        if (this.maps == null) {
            this.maps = new ArrayList();
        } else {
            maps.clear();
        }
        FuzzyMappingOp mapTexture;
        for (FuzzyTexture ft : fuzzyTextures) {
            mapTexture = new TiledFuzzyMappingOp(ft, this.width, this.height);
            maps.add(mapTexture.filter(this.source, null, false));
        }
    }
    
    /**
     * Apply this resemblance operator.
     * 
     * @param t the coordinates of the first pixel. 
     * @param u the coordinates of the second pixel.
     * @param image the image associated to the pixel coordinates.
     * @return the resemblance result.
     */
    @Override
    public Double apply(Point t, Point u, BufferedImage image) {
        //If the image is the one associated to this operator, the faster apply
        //method is used (in that case, the precalculated degrees will be used)
        if(image == source ){
            return apply(t,u);
        }
        //If the 'noBorder' flag is activated, the border points will be discarded
        if(!this.analyzeBorder && isOutside(u, image)){
            return 0.0;
        }
        
        //The subimages are calculated making sure they are inside the image
        int x,y,w,h;
        x = Math.max(0,t.x-half_width);
        y = Math.max(0,t.y-half_height);
        w = t.x+width < image.getWidth() ? width : image.getWidth()-t.x+1;
        h = t.y+height < image.getHeight() ? height : image.getHeight()-t.y+1;
        BufferedImage imt = image.getSubimage(x, y, w, h);
        x = Math.max(0,u.x-half_width);
        y = Math.max(0,u.y-half_height);
        w = u.x+width < image.getWidth() ? width : image.getWidth()-u.x+1;
        h = u.y+height < image.getHeight() ? height : image.getHeight()-u.y+1;
        BufferedImage imu = image.getSubimage(x, y, w, h);
        
        //The resemblance is calculated on the basis of the texture fuzzy sets. 
        //It is assumed that the resemblance between two fuzzy sets is 1.0 if
        //and only if they are the same set, 0.0 in other case. This assumption
        //simplifies the resemblance calculus based on double inclusion
        double degreeT, degreeU;
        double resemblanceTU, resemblance = Double.MAX_VALUE;
        for (FuzzyTexture ft : fuzzyTextures) {
            degreeT = ft.membershipDegree(imt);
            degreeU = ft.membershipDegree(imu);
            
            //The resemblace is calculated by means a double inclusion. A way
            //to do that is:
            //   TinU = this.inclusion(degreeT, ft, degreeU, ft);
            //   UinT = this.inclusion(degreeU, ft, degreeT, ft); 
            //   resemblanceTU = tnorm.apply(TinU,UinT);
            //Since resemblance(ft,ft) is 1.0, the call to the inclusion method
            //is equivalent to the call to the implication method. If we assume
            //the Goguen implication operator, the previous code is equivalent
            //to the following one:
            resemblanceTU = degreeT<=degreeU ? ( degreeU==0.0 ? 1.0 : degreeT/degreeU) : degreeU/degreeT;
            
            //Lukasiewicz case:
            //resemblanceTU = Math.min( 1.0, Math.min(1.0-degreeT+degreeU,1.0-degreeU+degreeT));
            
            resemblance = Math.min(resemblance, resemblanceTU);
        }
        return resemblance;
    }
       
    /**
     * Apply this resemblance operator to the associated image (if available).
     * 
     * @param t the coordinates of the first pixel. 
     * @param u the coordinates of the second pixel. 
     * @return the resemblance result. 
     * @throws NullPointerException if there is not image associated to this operator.
     */
    public Double apply(Point t, Point u) {
        if (this.source == null) {
            throw new NullPointerException("There is not image associated to this operator");
        }
        //If the 'noBorder' flag is activated, the border points will be discarded
        if(!this.analyzeBorder && isOutside(u, source)){
            return 0.0;
        }
        //Since the membership degrees are precalculated, we just need to access
        //to the degree values in each fuzzy texture map.
        double degreeT, degreeU;
        double resemblanceTU, resemblance = Double.MAX_VALUE;
        for(BufferedImage img: this.maps){
            degreeT = ((double)img.getRaster().getSample(t.x,t.y,0)) / 255.0;
            degreeU = ((double)img.getRaster().getSample(u.x,u.y,0)) / 255.0;
            //See the comments  about resemblance calculus in the previous apply method
            resemblanceTU = degreeT<=degreeU ? ( degreeU==0.0 ? 1.0 : degreeT/degreeU) : degreeU/degreeT;                        
            //Lukasiewicz case:
            //resemblanceTU = Math.min( 1.0, Math.min(1.0-degreeT+degreeU,1.0-degreeU+degreeT));
            resemblance = Math.min(resemblance, resemblanceTU);
        }        
        return resemblance;
    }
    
    /**
     * Check if the given point is outside the border limits.
     * 
     * @param p th point to be checked.
     * @param image the image.
     * @return <tt>true</tt> if the point is outside; <tt>false</tt> otherwise.
     */
    boolean isOutside(Point p, BufferedImage image){
        return p.x<this.half_width || p.x>=image.getWidth()-this.half_width ||
               p.y<this.half_height || p.y>=image.getHeight()-this.half_height;             
    }
    
    
    /**
     * Set the width of the window used to calculate the texture measures.
     * 
     * @param width the width of the window
     */
    public void setWidth(int width) {
        if (this.width != width) {
            this.width = Math.max(1, width);
            this.half_width = this.width / 2;
            if (source != null) {
                //If there is a source image, it is necessary to recalculate the 
                //texture mappings using the new width
                this.sourceMapping();
            }
        }
    }

    /**
     * Returns the width of the window used to calculate the texture measures.
     *
     * @return the width of the window used to calculate the texture measures.
     */
    public int getWidth() {
        return this.width;
    }

    /**
     * Set the height of the window used to calculate the texture measures.
     *
     * @param height the height of the window
     */
    public void setHeight(int height) {
        if (this.height != height) {
            this.height = Math.max(1, height);
            this.half_height = this.height / 2;
            if (source != null) {
                //If there is a source image, it is necessary to recalculate the 
                //texture mappings using the new height
                this.sourceMapping();
            }
        }
    }
    
    /**
     * Returns the height of the window used to calculate the texture measures.
     * 
     * @return the height of the window used to calculate the texture measures.
     */
    public int getHeight(){
        return this.height;
    }
    
    /**
     * Flag to set whether the points in the border are processed or not.
     *
     * @param b <tt>true</tt> if the border points want to be analyzed;
     * <tt>false</tt> otherwise.
     */
    public void analyzeBorder(boolean b) {
        this.analyzeBorder = b;
    }
    
    /**
     * To do....
     * 
     * @param ft1
     * @param ft2
     * @return 
     */
    private double resemblanceFuzzySet(FuzzyTexture ft1, FuzzyTexture ft2){
        return ft1.equals(ft2) ? 1.0 : 0.0;
    }
    
    /**
     * Fuzzy implication operator. It measures the degree of 'a->b'
     *
     * @param a firts degree in 'a->b'
     * @param b second degree in 'a->b'
     * @param type type of implication operator.
     * @return the 'a->b' implication degree.
     */
    private double implication(double a, double b, int type) {
        switch (type) {
            case GOGUEN_IMPLICATION_TYPE:
                return a <= b ? 1 : b / a;
            case LUKASIEWICZ_IMPLICATION_TYPE:
                return Math.min(1.0, 1.0 - a + b);
        }
        return 0.0;

    }
    
    /**
     * Fuzzy inclusion operator. It measures the degree in which 'a/fa' is
     * included in 'b/fb'.
     *
     * @param a first degree, which is asociated to the fuzzy set 'fta'.
     * @param fta first fuzzy set.
     * @param b second degree, which is asociated to the fuzzy set 'ftb'.    
     * @param ftb seconf fuzzy set.
     * @return 
     */
    private double inclusion(double a, FuzzyTexture fta, double b, FuzzyTexture ftb){
        return tnorm.apply(this.implication(a, b, DEFAULT_IMPLICATION_TYPE),
                           this.resemblanceFuzzySet(fta,ftb));
    }
}
