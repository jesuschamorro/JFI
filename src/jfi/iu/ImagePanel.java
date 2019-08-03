package jfi.iu;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

/**
 * Panel que contiene y muestra una imagen
 * 
 * @author Jesús Chamorro Martínez (jesus@decsai.ugr.es)
 */
public class ImagePanel extends javax.swing.JPanel {
    /**
     * Imagen asociada al panel
     */
    protected BufferedImage img;
    /**
     * Localización de la imagen dentro del panel (esquina superior izquierda)
     */
    protected int x_image, y_image;
    /**
     * Patrón de discontinuidad del grid
     */
    private final float grid_dash[] = {1.0f,1.0f};
    /**
     * Trazo del grid
     */
    private final BasicStroke grid = new BasicStroke(1.0f,0,0,1.0f,grid_dash,0.0f);
    /**
     * Tamaño de la retícula del grid
     */
    private final int grid_size = 30;
    /**
     * Determina si se usa o no grid
     */
    protected boolean use_grid = true;
    /**
     * Zoom (escalado) a aplicar
     */
    protected int zoom = 1;

    /**
     * Construye un panel sin imagen
     */
    public ImagePanel() {
        this(null);
    }
    
    /**
     * Cosntruye un panel con la imagen pasada por parámetro
     * 
     * @param img imagen
     */
    public ImagePanel(BufferedImage img){
        initComponents();
        this.setImage(img);
    }
    
    /**
     * Asigna una imagen al panel
     * 
     * @param img la imagen a asignar 
     */
    public final void setImage(BufferedImage img) {
        this.img = img;
        if (img != null) {
            this.setPreferredSize(new Dimension(img.getWidth(), img.getHeight()));
        }
    }

    /**
     * Devuelve la imagen del panel
     * 
     * @return la imagen del panel 
     */
    public BufferedImage getImage(){
        return(img);
    }

    /**
     * Devuelve la localización de la imagen dentro del panel
     * 
     * @return la localización de la imagen dentro del panel
     */
    public Point getImageLocation(){
        return new Point(x_image,y_image);
    }
    
    /**
     * Establece si hay o no grid
     * 
     * @param grid  
     */
    public void setGrid(boolean grid){
        this.use_grid = grid;
    }
    
    /**
     * Devuelve <tt>true</tt> si se usa grid
     * 
     * @return <tt>true</tt> si se usa grid 
     */
    public boolean isGridded(){
        return use_grid;
    }
    
    /**
     * Establece un nuevo valor de zoom
     * 
     * @param zoom nuevo valor de zoom
     */
    public void setZoom(int zoom){
        this.zoom = zoom;
        if (img != null) {
            this.setPreferredSize(new Dimension(img.getWidth()*zoom, img.getHeight()*zoom));
        }
    }
    
    /**
     * Devuelve el valor actual de zoom
     * 
     * @return el valor actual de zoom 
     */
    public int getZoom(){
        return this.zoom;
    }
    
    
    /**
     * Pinta el panel
     * 
     * @param g gráfico asociado al panel 
     */
    @Override
    public void paint(Graphics g) {
        super.paint(g);     
        ((Graphics2D)g).scale(zoom, zoom);
        if (img != null) {
            Rectangle r = this.getParent().getBounds();
            x_image = (int)Math.max(0,r.getCenterX() - (img.getWidth()*zoom/2)) / zoom;
            y_image = (int)Math.max(0,r.getCenterY() - (img.getHeight()*zoom/2)) / zoom;
            if(use_grid) paintGrid(g);
            g.drawImage(img, x_image, y_image, this);
        }
    }
    
    /**
     * Pinta un grid
     * 
     * @param g gráfico asociado al panel 
     */
    protected void paintGrid(Graphics g){
        Rectangle bounds = this.getParent().getBounds();
        ((Graphics2D)g).setStroke(grid);
        g.setColor(Color.LIGHT_GRAY);
        for(int x=x_image%grid_size;x<bounds.width;x+=grid_size){
            g.drawLine(x,0,x,bounds.height);
        }
        for(int y=y_image%grid_size;y<bounds.height;y+=grid_size){
            g.drawLine(0,y,bounds.width,y);
        }
    }
    
     /*
     * Código generado por Netbeans para el diseño del interfaz
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setAlignmentX(0.0F);
        setAlignmentY(0.0F);
        setCursor(new java.awt.Cursor(java.awt.Cursor.CROSSHAIR_CURSOR));
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

}
