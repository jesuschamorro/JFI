package jfi.texture;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

/**
 *
 * @author Pedro M. Martínez Jiménez
 * @author Jesús Chamorro Martínez (jesus@decsai.ugr.)
 */
public class AmadasunCoarsenessMeasure implements TextureMeasure {

    private float distance;
    private float greyLevels;
    public double value;

    public static final float DEFAULT_DISTANCE = 1;
    public static final float DEFAULT_GREY_LEVELS = 256;
    public static final int WINDOW_SIZE = 32;

    /**
     * Constructs the measure object using the default parameters.
     */
    public AmadasunCoarsenessMeasure() {
        this(DEFAULT_DISTANCE, DEFAULT_GREY_LEVELS);
    }

    /**
     * Constructs a measure object based on the Amadasun coarseness measure.
     *
     * @param distance distance parameter of the Amadasun measure.
     * @param greyLevels number of grey levels of the image.
     */
    public AmadasunCoarsenessMeasure(float distance, float greyLevels) {
        this.distance = distance;
        this.greyLevels = greyLevels;
    }

    @Override
    public Double apply(BufferedImage image) {
        float[] param = {distance, greyLevels};
        return amadasunMeasure(image, param);
    }

    /// ¿Los niveles de gris debe ser parámetro en el constructor?
    ///------------------- Código de Pedro:
    private double amadasunMeasure(BufferedImage I, float[] param) {
        int[] imagGris = transformaNivelesGris(I);
        int d = (int) param[0];
        int nivelesGris = (int) param[1];
        int rows = I.getHeight();
        int cols = I.getWidth();
        double cte = java.lang.Double.MIN_VALUE;
        double W = Math.pow(2.0 * d + 1, 2);
        int tope_max_k = rows - d;
        int tope_max_l = cols - d;

        ////// Para hacer la medida independiente del tamaño de la ventana
        int reduccion = 32;
        int tam_p = nivelesGris / reduccion;
        int i, k, l, m, j, suma, indice;
        double A_i, grosor;

        if ((tope_max_k < 0) || (tope_max_l < 0)) {
            System.err.println("La imagen es demasiado pequeña para aplicar el tamaño de ventana\n");
            return -1;
        }
        double[] s = new double[tam_p];
        double[] p = new double[tam_p];
        int n_2 = (rows - 2 * d) * (cols - 2 * d);
        for (i = 0; i < tam_p; i++) {
            s[i] = 0;
            p[i] = 0;
        }
        for (k = d; k < tope_max_k; k++) {
            for (l = d; l < tope_max_l; l++) {
                i = imagGris[k * cols + l];
                suma = 0;
                for (m = k - d; m <= k + d; m++) {
                    for (j = l - d; j <= l + d; j++) {
                        suma += imagGris[m * cols + j];
                    }
                }
                A_i = ((double) (suma - i)) / (W - 1);
                indice = (int) Math.floor((double) i / (double) reduccion);
                p[indice] += 1;
                s[indice] += Math.abs(i - A_i);
            }
        }
        grosor = 0;
        for (i = 0; i < tam_p; i++) {
            grosor += (p[i] / n_2) * s[i];
        }
        grosor /= n_2;
        grosor = 1.0 / (grosor + cte);
        return grosor;
    }

    public int[] transformaNivelesGris(BufferedImage bi) {
        // Copiamos los datos de la imagen en el objeto 'wr' y lo comprobamos
        WritableRaster wr = bi.getRaster();
        int[] imagGris = null;

        if (wr.getNumBands() == 3) {
            int ancho = bi.getWidth(), alto = bi.getHeight(), tam = ancho * alto;
            int[] x = new int[tam];
            int[] y = new int[tam];
            int[] z = new int[tam];
            imagGris = new int[tam];
            //  Creamos el array de imagen con los p�xeles de la imagen
            x = wr.getSamples(0, 0, ancho, alto, 0, x);
            y = wr.getSamples(0, 0, ancho, alto, 1, y);
            z = wr.getSamples(0, 0, ancho, alto, 2, z);
            for (int i = 0; i < tam; i++) {
                imagGris[i] = (int) Math.round(0.30 * x[i] + 0.59 * y[i] + 0.11 * z[i]);
            }
        } else if (wr.getNumBands() == 1) {
            imagGris = new int[bi.getWidth() * bi.getHeight()];
            //  Creamos el array de imagen con los p�xeles de la imagen
            imagGris = wr.getSamples(0, 0, bi.getWidth(), bi.getHeight(), 0, imagGris);
        }
        return imagGris;
    }

}
