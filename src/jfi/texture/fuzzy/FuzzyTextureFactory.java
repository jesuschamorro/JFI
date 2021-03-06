package jfi.texture.fuzzy;

import java.security.InvalidParameterException;
import jfi.fuzzy.membershipfunction.PolynomialFunction;
import jfi.texture.AmadasunCoarsenessMeasure;
import jfi.texture.CorrelationCoarsenessMeasure;
import jfi.texture.FDCoarsenessMeasure;
import jfi.texture.TextureMeasure;
import jfi.texture.TamuraContrastMeasure;
import jfi.texture.HaralickContrastMeasure;
import jfi.texture.TamuraDirectionalityMeasure;
import jfi.texture.AbbadeniDirectionalityMeasure;


/**
 * Class for fuzzy texture templates generation.
 * 
 * @author Jesús Chamorro Martínez (jesus@decsai.ugr.es)
 */
public class FuzzyTextureFactory {

    public static final int TYPE_COARSENESS_AMADASUN = 1;
    public static final int TYPE_COARSENESS_CORRELATION = 2;
    public static final int TYPE_COARSENESS_FD = 3;
    public static final int TYPE_COARSENESS_AMADASUN_CORRELATION = 4;
    public static final int TYPE_COARSENESS_FD_AMADASUN = 5;
    public static final int TYPE_COARSENESS_CORRELATION_FD = 6;
    public static final int TYPE_CONTRAST_TAMURA = 7;
    public static final int TYPE_CONTRAST_HARALICK = 8;
    public static final int TYPE_CONTRAST_TAMURA_HARALICK = 9;
    public static final int TYPE_DIRECTIONALITY_TAMURA = 10;
    public static final int TYPE_DIRECTIONALITY_ABBADENI = 11;
    public static final int TYPE_DIRECTIONALITY_TAMURA_ABBADENI = 12;

    public static FuzzyTexture getInstance(int type) {
        TextureMembershipFunction mfunction = FunctionFactory.getInstance(type);
        if(mfunction==null)
            throw new InvalidParameterException("Invalid type."); 
        switch (type) {
            case TYPE_COARSENESS_AMADASUN:
                return new FuzzyTexture("Texture.Coarseness.Amadasun.1D", mfunction);
            case TYPE_COARSENESS_CORRELATION:
                return new FuzzyTexture("Texture.Coarseness.Correlation.1D", mfunction);
            case TYPE_COARSENESS_FD:
                return new FuzzyTexture("Texture.Coarseness.FD.1D", mfunction);
            case TYPE_COARSENESS_AMADASUN_CORRELATION:
                return new FuzzyTexture("Texture.Coarseness.Amadasun.Correlation.2D", mfunction);
            case TYPE_COARSENESS_FD_AMADASUN:
                return new FuzzyTexture("Texture.Coarseness.FD.Amadasun.2D", mfunction);
            case TYPE_COARSENESS_CORRELATION_FD:
                return new FuzzyTexture("Texture.Coarseness.Correlation.FD.2D", mfunction);
            case TYPE_CONTRAST_TAMURA:
                return new FuzzyTexture("Texture.Contrast.Tamura.1D", mfunction);
            case TYPE_CONTRAST_HARALICK:
                return new FuzzyTexture("Texture.Contrast.Haralick.1D", mfunction);
            case TYPE_CONTRAST_TAMURA_HARALICK:
                return new FuzzyTexture("Texture.Contrast.Tamura.Haralick.2D", mfunction);
            case TYPE_DIRECTIONALITY_TAMURA:
                return new FuzzyTexture("Texture.Directionality.Tamura.1D", mfunction);
            case TYPE_DIRECTIONALITY_ABBADENI:
                return new FuzzyTexture("Texture.Directionality.Abbadeni.1D", mfunction);
            case TYPE_DIRECTIONALITY_TAMURA_ABBADENI:
                return new FuzzyTexture("Texture.DIrectionality.Tamura.Abbadeni.2D", mfunction);
            default:
                return null;
        }
    }

    /**
     * Class for texture membership function generation.
     */
    public static class FunctionFactory {

        /**
         * Create a new texture membership function of the specified type
         *
         * @param type type of texture membership function
         *
         * @return A new instance of <code>TextureMembershipFunction</code>
         */
        public static TextureMembershipFunction getInstance(int type) {

            switch (type) {
                case TYPE_COARSENESS_AMADASUN:
                    return getCoarsenessAmadasunInstance();
                case TYPE_COARSENESS_CORRELATION:
                    return getCoarsenessCorrelationInstance();
                case TYPE_COARSENESS_FD:
                    return getCoarsenessFDInstance();
                case TYPE_COARSENESS_AMADASUN_CORRELATION:
                    return getCoarsenessAmadasunCorrelationInstance();
                case TYPE_COARSENESS_FD_AMADASUN:
                    return getCoarsenessFDAmadasunInstance();
                case TYPE_COARSENESS_CORRELATION_FD:
                    return getCoarsenessCorrelationFDInstance();
                case TYPE_CONTRAST_TAMURA:
                    return getContrastTamuraInstance();
                case TYPE_CONTRAST_HARALICK:
                    return getContrastHaralickInstance();
                case TYPE_CONTRAST_TAMURA_HARALICK:
                    return getContrastTamuraHaralickInstance();
                case TYPE_DIRECTIONALITY_TAMURA:
                    return getDirectionalityTamuraInstance();
                case TYPE_DIRECTIONALITY_ABBADENI:
                    return getDirectionalityAbbadeniInstance();
                case TYPE_DIRECTIONALITY_TAMURA_ABBADENI:
                    return getDirectionalityTamuraAbbadeniInstance();
                default:
                    return null;
            }
        }

        /**
         * Create a new polynomial membership function for the Amadasun coarseness
         * measure. For this purpouse, the coeficients proposed in <cite> <\cite>
         * are used by default
         *
         * @return a default polynomial membership function for Amadasun coarseness
         * measure
         */
        private static TextureMembershipFunction getCoarsenessAmadasunInstance() {
            double coeficients[] = {1.87066736922147, -6.48351627234047, 9.49014397876514, -6.61278585052942};
            PolynomialFunction mfunction = new PolynomialFunction(coeficients);
            TextureMeasure measure = new AmadasunCoarsenessMeasure();
            return new TextureMembershipFunction(mfunction, measure);
        }

        /**
         * Create a new polynomial membership function for the Correlation
         * coarseness measure. For this purpouse, the coeficients proposed in
         * <cite> <\cite> are used by default
         *
         * @return a default polynomial membership function for Correlation
         * coarseness measure
         */
        private static TextureMembershipFunction getCoarsenessCorrelationInstance() {
            double coeficients[] = {1.04855239337754, -1.70133043615012, 2.99614780551409, -3.31098414030745};
            PolynomialFunction mfunction = new PolynomialFunction(coeficients);
            TextureMeasure measure = new CorrelationCoarsenessMeasure();
            return new TextureMembershipFunction(mfunction, measure);
        }
        
        /**
         * Create a new polynomial membership function for the Fractal Dimension
         * (FD) coarseness measure. For this purpouse, the coeficients proposed in
         * <cite> <\cite> are used by default.
         *
         * @return a default polynomial membership function for FD coarseness measure
         */
        private static TextureMembershipFunction getCoarsenessFDInstance() {
            double coeficients[] = {-28.6901663311276, 24.491196501627, -6.8486819672468, 0.641828076149647};
            PolynomialFunction mfunction = new PolynomialFunction(coeficients);
            TextureMeasure measure = new FDCoarsenessMeasure();
            return new TextureMembershipFunction(mfunction, measure);
        }        

        /**
         * Create a new polynomial membership function of two variables for the
         * Amadasun and Correlation coarseness measures. For this purpouse, the
         * coeficients proposed in <cite> <\cite> are used by default
         *
         * @return a default polynomial membership function of two variables for
         * the Amadasun and Correlation coarseness measures
         */
        private static TextureMembershipFunction getCoarsenessAmadasunCorrelationInstance() {	
            double coeficients[] = {1.47499105940904, -1.40315232203117, -2.36176232354071,
                                    2.18145639951272, 2.03894057701965, -0.593502374987317,
                                    -2.21467438820318, 0.0, 0.0, 0.0};
            PolynomialFunction mfunction = new PolynomialFunction(coeficients, 2);
            TextureMeasure measure1 = new AmadasunCoarsenessMeasure();
            TextureMeasure measure2 = new CorrelationCoarsenessMeasure();
            return new TextureMembershipFunction(mfunction, measure1, measure2);
        }
        
        /**
         * Create a new polynomial membership function of two variables for the
         * Fractal Dimension (FD) and Amadasun coarseness measures. For this
         * purpouse, the coeficients proposed in <cite> <\cite> are used by default
         *
         * @return a default polynomial membership function of two variables for
         * the FD and Amadasun coarseness measures
         */
        private static TextureMembershipFunction getCoarsenessFDAmadasunInstance() {
            double coeficients[] = {-12.1564073984986, -27.3508169133145, 15.1935169107938,
                                    51.148613918219, 1.55839523317858, -4.51740426692254,
                                    -38.063965088385, -0.647538386874109, 0.0, 0.417530298268412};
            PolynomialFunction mfunction = new PolynomialFunction(coeficients, 2);
            TextureMeasure measure1 = new FDCoarsenessMeasure();
            TextureMeasure measure2 = new AmadasunCoarsenessMeasure();
            return new TextureMembershipFunction(mfunction, measure1, measure2);
        }
        
        /**
         * Create a new polynomial membership function of two variables for the
         * Correlation and Fractal Dimension (FD) coarseness measures. For this
         * purpouse, the coeficients proposed in <cite> <\cite> are used by default
         *
         * @return a default polynomial membership function of two variables for
         * the Correlation and FD coarseness measures
         */
        private static TextureMembershipFunction getCoarsenessCorrelationFDInstance() {
            double coeficients[] = {-14.002884725279, 13.0289019271327, 4.36543898439526,
                                    -3.8050189067351, -1.57795467281365, -4.62307282421302,
                                    0.372563573848496, 0.0, 1.33113686816522, 0.0};
            PolynomialFunction mfunction = new PolynomialFunction(coeficients, 2);
            TextureMeasure measure1 = new CorrelationCoarsenessMeasure();
            TextureMeasure measure2 = new FDCoarsenessMeasure();
            return new TextureMembershipFunction(mfunction, measure1, measure2);
        }
        
        
        /**
         * Create a new polynomial membership function for the Tamura contrast
         * measure. For this purpouse, the coeficients proposed in <cite> <\cite>
         * are used by default
         *
         * @return a default polynomial membership function for Tamura contrast
         * measure
         */
        private static TextureMembershipFunction getContrastTamuraInstance() {
            double coeficients[] = {-0.572797975960776, 3.8763399205201, -3.95358266570628, 1.68772834619706};
            PolynomialFunction mfunction = new PolynomialFunction(coeficients);
            TextureMeasure measure = new TamuraContrastMeasure();
            return new TextureMembershipFunction(mfunction, measure);
        }
        
        /**
         * Create a new polynomial membership function for the Haralick contrast
         * measure. For this purpouse, the coeficients proposed in <cite> <\cite>
         * are used by default
         *
         * @return a default polynomial membership function for Haralick contrast
         * measure
         */
        private static TextureMembershipFunction getContrastHaralickInstance() {
            double coeficients[] = {-0.410013110132171, 2.06793922263109, -1.87136583491134, 0.623174661550225};
            PolynomialFunction mfunction = new PolynomialFunction(coeficients);
            TextureMeasure measure = new HaralickContrastMeasure();
            return new TextureMembershipFunction(mfunction, measure);
        }
        
        /**
         * Create a new polynomial membership function of two variables for
         * Tamura and Haralick contrast measures. For this purpouse, the
         * coeficients proposed in <cite> <\cite> are used by default
         *
         * @return a default polynomial membership function of two variables for
         * Tamura and Haralick contrast measures
         */
        private static TextureMembershipFunction getContrastTamuraHaralickInstance() {
            double coeficients[] = {-0.763798479121629, -0.156200555711584, 6.06372176140489,
                                    0.0341192712377689, 0.515847371994987, -10.4566818022673,
                                    -0.0130882977049654, 0.0, -0.112520260691099, 6.17548971591803};
            PolynomialFunction mfunction = new PolynomialFunction(coeficients, 2);
            TextureMeasure measure1 = new TamuraContrastMeasure();
            TextureMeasure measure2 = new HaralickContrastMeasure();
            return new TextureMembershipFunction(mfunction, measure1, measure2);
        }
        
        
        /**
         * Create a new polynomial membership function for the Tamura directionality
         * measure. For this purpouse, the coeficients proposed in <cite> <\cite>
         * are used by default
         *
         * @return a default polynomial membership function for Tamura directionality
         * measure
         */
        private static TextureMembershipFunction getDirectionalityTamuraInstance() {
            double coeficients[] = {-511.968522225476, 1657.3455394384, -1792.31164934142, 648.139541015052};
            PolynomialFunction mfunction = new PolynomialFunction(coeficients);
            TextureMeasure measure = new TamuraDirectionalityMeasure();
            return new TextureMembershipFunction(mfunction, measure);
        }
        
         /**
         * Create a new polynomial membership function for the Abbadeni directionality
         * measure. For this purpouse, the coeficients proposed in <cite> <\cite>
         * are used by default
         *
         * @return a default polynomial membership function for Abbadeni directionality
         * measure
         */
        private static TextureMembershipFunction getDirectionalityAbbadeniInstance() {
            double coeficients[] = {-5.85660764047519, 26.6931630330267, -38.9620785247236, 19.5973685907395};
            PolynomialFunction mfunction = new PolynomialFunction(coeficients);
            TextureMeasure measure = new AbbadeniDirectionalityMeasure();
            return new TextureMembershipFunction(mfunction, measure);
        }
        
        /**
         * Create a new polynomial membership function of two variables for
         * Tamura and Abbadeni directionality measures. For this purpouse, the
         * coeficients proposed in <cite> <\cite> are used by default
         *
         * @return a default polynomial membership function of two variables for
         * Tamura and Abbadeni directionality measures
         */
        private static TextureMembershipFunction getDirectionalityTamuraAbbadeniInstance() {
            double coeficients[] = {-204.789238636328, -1.33549175271706, 648.462555731697,
                                    -5.7224187417743, 5.83078827470319, -686.199629240745,
                                    2.90474813082403, 0.0, 0.0, 242.0293116841};
            PolynomialFunction mfunction = new PolynomialFunction(coeficients, 2);
            TextureMeasure measure1 = new TamuraDirectionalityMeasure();
            TextureMeasure measure2 = new AbbadeniDirectionalityMeasure();
            return new TextureMembershipFunction(mfunction, measure1, measure2);
        }

    }

}
