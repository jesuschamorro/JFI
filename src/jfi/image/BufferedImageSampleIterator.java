package jfi.image;

import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 *
 * @author Jesús Chamorro
 */
public class BufferedImageSampleIterator implements Iterator<BufferedImageSampleIterator.SampleData> {

    private final Raster raster;
    private final int numRows, numCols, numBands, length;
    private int cont = 0;

    public BufferedImageSampleIterator(BufferedImage img) {
        raster = img.getRaster();
        numRows = raster.getHeight();
        numCols = raster.getWidth();
        numBands = raster.getNumBands();
        length = numRows * numCols * numBands;
    }

    @Override
    public boolean hasNext() {
        return (cont < length);
    }

    @Override
    public SampleData next() {
        if (cont >= length) {
            throw new NoSuchElementException("No more samples");
        }
        int row = cont / (numCols * numBands);
        int col = (cont / numBands) % numCols;
        int band = cont % numBands;
        cont++;
        return new SampleData(row, col, band, raster.getSample(col, row, band));
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException("Remove operation not supported");
    }

    /**
     * Datos del componente (sample): localización y valor
     */
    public class SampleData {
        public int row, col, band, value;
        
        public SampleData(int row, int col, int band, int value) {
            this.row = row;
            this.col = col;
            this.band = band;
            this.value = value;
        }
    }

}
