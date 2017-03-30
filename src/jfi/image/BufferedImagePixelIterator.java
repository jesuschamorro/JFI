package jfi.image;

import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 *
 * @author Jesús Chamorro
 */
public class BufferedImagePixelIterator implements Iterator<BufferedImagePixelIterator.PixelData> {

    private final Raster raster;
    private final int numRows, numCols, length;
    private int cont = 0;
    private final int[] pixelComp=null;

    public BufferedImagePixelIterator(BufferedImage img) {
        raster = img.getRaster();
        numRows = raster.getHeight();
        numCols = raster.getWidth();
        length = numRows * numCols;
    }

    @Override
    public boolean hasNext() {
        return (cont < length);
    }

    @Override
    public PixelData next() {
        if (cont >= length) {
            throw new NoSuchElementException("No more samples");
        }
        int row = cont / numCols;
        int col = cont % numCols;
        cont++;
        
        return new PixelData(row, col, raster.getPixel(col, row, pixelComp));
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException("Remove operation not supported");
    }

    /**
     * Datos del pixel: localización y valor de cada banda
     */
    public class PixelData {
        public int row, col, numSamples;
        public int[] sample = null;
        
        public PixelData(int row, int col, int[] sample) {
            this.row = row;
            this.col = col;
            this.sample = sample;
            this.numSamples = sample!=null ? sample.length: 0;
        }
        
    }

}
