package jfi.iu;


import jfi.events.PixelEvent;
import jfi.events.PixelListener;
import java.awt.AWTException;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JFrame;

/**
 * Clase que representa una ventana interna con una imagen
 * 
 * @author Jesús Chamorro
 */
public class ImageInternalFrame extends javax.swing.JInternalFrame {

    /**
    * Referencia al la ventana que ha lanzado este ventana interna
    */
    protected JFrame parent=null;
    /**
     * Tipo de ventana interna. Los tipos por defecto se declaran como constantes
     */
    protected int type;
    /**
     * Tipo de ventana estándar que almacena una imagen
     */
    final public static int TYPE_STANDAR = 1;
    /**
     * Ventana que almacena un contorno crisp
     */
    final public static int TYPE_CONTOUR = 2;    
    /**
     * Ventana que almacena un contorno difuso
     */
    final public static int TYPE_FUZZY_CONTOUR = 3;
    /**
     * Dimensión inicial de la ventana
     */
    private static Dimension INITIAL_SIZE = new Dimension(500,500);
    /**
     * Manejador del evento 'PixelEvent' 
     */
    private PixelListener pixelEventListener = null;
    
    /**
     * Construye una ventana interna que contiene la imagen pasada por parámetro.
     * Por defecto, la ventana es de tipo "estándar"
     * 
     * @param parent ventana desde la que se ha lanzado esta ventana interna
     * @param img imagen
     */
    public ImageInternalFrame(JFrame parent, BufferedImage img) {
        this(parent, img, TYPE_STANDAR);
    }
    
    /**
     * Construye una ventana interna del tipo indicado y que contiene la
     * imagen pasada por parámetro.
     * 
     * @param parent ventana que ha lanzado esta ventana interna
     * @param img imagen
     * @param type tipo de ventana interna
     */
    protected ImageInternalFrame(JFrame parent, BufferedImage img, int type) {
        initComponents(); // Código generado por Netbeans
        this.parent = parent;
        this.type = type;     
        this.panelImagen.setImage(img);
        //setIcon();
        setSize(INITIAL_SIZE);
    } 
    
    /**
     * Devuelve el tipo de ventana
     * 
     * @return el tipo de ventana 
     */
    public int getType(){
        return type;
    }
    
    /**
     * Establece el icono en función del tipo de imagen
     */
    private void setIcon(){
        String iconFile=null;
        switch(type){
            case TYPE_STANDAR:
                iconFile = "/icons/iconoVentanaEstandar.png";
                break;
            case TYPE_CONTOUR:
                iconFile = "/icons/iconoVentanaContorno.png";
                break;   
            case TYPE_FUZZY_CONTOUR:
                iconFile = "/icons/iconoContornoDifuso.png";
                break;    
        }
        if(iconFile!=null)
            setFrameIcon(new ImageIcon(getClass().getResource(iconFile)));
    } 
  
    /**
     * Devuelve la imagen del lienzo
     * 
     * @return la imagen del lienzo
     */
    public BufferedImage getImage(){
        return this.panelImagen.getImage();
    }
    
    /**
     * Establece como imagen la pasada por parámetro
     * 
     * @param image imagen 
     */
    public void setImagen(BufferedImage image) {
        panelImagen.setImage(image);
    }

    /**
     * Añade una imagen a la lista de imágenes de la ventana
     * 
     * @param image imagen
     */
    public void addImage(BufferedImage image){
        ((MultipleImagePanel)panelImagen).addImage(image);
    }
    
    /**
     * Elimina la última imagen de la lista de imágenes de la ventana. 
     * En caso de solo quede una, deja a <tt>null</tt> la imagen asociada a 
     * esta ventana
     */
    public void removeImage(){
        ((MultipleImagePanel)panelImagen).removeImage();
    }
    
    /**
     * Devuelve el número de imagenes que hay en la ventana
     * 
     * @return el número de imagenes de la ventana
     */
    public int numberOfImages(){        
        return ((MultipleImagePanel)panelImagen).numberOfImages();
    }
    
    /**
     * Devuelve el tamaño de la imagen msotarda en la ventana
     *
     * @return el tamaño de la imagen
     */
    public Dimension getImageSize() {
        return new Dimension(getImage().getWidth(), getImage().getHeight());
    }
    
    /**
     * Establece si hay o no grid
     * 
     * @param grid  
     */
    public void setGrid(boolean grid){
        panelImagen.setGrid(grid);
    }
    
    /**
     * Devuelve <tt>true</tt> si se usa grid
     * 
     * @return <tt>true</tt> si se usa grid 
     */
    public boolean isGridded(){
        return panelImagen.isGridded();
    }
    
    /**
     * Establece un nuevo valor de zoom
     * 
     * @param zoom nuevo valor de zoom
     */
    public void setZoom(int zoom){
        this.panelImagen.setZoom(zoom);
        this.panelImagen.revalidate();
    }
    
    /**
     * Devuelve el valor actual de zoom
     * 
     * @return el valor actual de zoom 
     */
    public int getZoom(){
        return this.panelImagen.getZoom();
    }
    
    /**
     * Adds the specified pixel listener to receive pixel events from
     * this internal frame
     * 
     * @param l the pixel listener
     */
    public void addPixelListener(PixelListener l){
        if (l != null) {
            this.pixelEventListener = l;
        }        
    }
    
    /**
     * Notify the pixel listener a new pixel event
     * @param evt 
     */
    private void notifyPixelEvent(PixelEvent evt){
        if(pixelEventListener!=null){
            pixelEventListener.positionChange(evt);
        }
    }
    
    /*
     * Código generado por Netbeans para el diseño del interfaz
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        scrollPanel = new javax.swing.JScrollPane();
        panelImagen = new jfi.iu.MultipleImagePanel();

        FormListener formListener = new FormListener();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        addInternalFrameListener(formListener);
        addKeyListener(formListener);

        scrollPanel.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPanel.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPanel.setViewportBorder(javax.swing.BorderFactory.createEtchedBorder());

        panelImagen.addMouseMotionListener(formListener);
        panelImagen.addMouseListener(formListener);
        scrollPanel.setViewportView(panelImagen);

        getContentPane().add(scrollPanel, java.awt.BorderLayout.CENTER);

        pack();
    }

    // Code for dispatching events from components to event handlers.

    private class FormListener implements java.awt.event.KeyListener, java.awt.event.MouseListener, java.awt.event.MouseMotionListener, javax.swing.event.InternalFrameListener {
        FormListener() {}
        public void keyPressed(java.awt.event.KeyEvent evt) {
            if (evt.getSource() == ImageInternalFrame.this) {
                ImageInternalFrame.this.formKeyPressed(evt);
            }
        }

        public void keyReleased(java.awt.event.KeyEvent evt) {
        }

        public void keyTyped(java.awt.event.KeyEvent evt) {
        }

        public void mouseClicked(java.awt.event.MouseEvent evt) {
        }

        public void mouseEntered(java.awt.event.MouseEvent evt) {
        }

        public void mouseExited(java.awt.event.MouseEvent evt) {
            if (evt.getSource() == panelImagen) {
                ImageInternalFrame.this.panelImagenMouseExited(evt);
            }
        }

        public void mousePressed(java.awt.event.MouseEvent evt) {
        }

        public void mouseReleased(java.awt.event.MouseEvent evt) {
        }

        public void mouseDragged(java.awt.event.MouseEvent evt) {
        }

        public void mouseMoved(java.awt.event.MouseEvent evt) {
            if (evt.getSource() == panelImagen) {
                ImageInternalFrame.this.panelImagenMouseMoved(evt);
            }
        }

        public void internalFrameActivated(javax.swing.event.InternalFrameEvent evt) {
            if (evt.getSource() == ImageInternalFrame.this) {
                ImageInternalFrame.this.formInternalFrameActivated(evt);
            }
        }

        public void internalFrameClosed(javax.swing.event.InternalFrameEvent evt) {
        }

        public void internalFrameClosing(javax.swing.event.InternalFrameEvent evt) {
        }

        public void internalFrameDeactivated(javax.swing.event.InternalFrameEvent evt) {
        }

        public void internalFrameDeiconified(javax.swing.event.InternalFrameEvent evt) {
        }

        public void internalFrameIconified(javax.swing.event.InternalFrameEvent evt) {
        }

        public void internalFrameOpened(javax.swing.event.InternalFrameEvent evt) {
        }
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Maneja la pulsación de las teclas "flechas" para mover el puntero del
     * ratón
     * 
     * @param evt evento de teclado
     */
    private void formKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyPressed
        int dx=0,dy=0;
        switch(evt.getKeyCode()){
            case KeyEvent.VK_UP:
                dy=-1;
                break;
            case KeyEvent.VK_DOWN:
                dy=1;
                break;
            case KeyEvent.VK_LEFT:
                dx=-1;
                break;
            case KeyEvent.VK_RIGHT:
                dx=1;
                break;    
        }
        try {
            Robot robot = new Robot();
            Point p = MouseInfo.getPointerInfo().getLocation();
            robot.mouseMove(p.x+dx,p.y+dy);
        } catch (AWTException ex) {
            Logger.getLogger(ImageInternalFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_formKeyPressed

    /**
     * Le asigna el foco a la ventana cuando ésta se activa. Con ellos, se garantiza
     * la captura de eventos de ratón.
     * 
     * @param evt 
     */
    private void formInternalFrameActivated(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameActivated
        this.requestFocusInWindow();
    }//GEN-LAST:event_formInternalFrameActivated

    /**
     * Borra la información de la ventana principal relativa a la posición del
     * puntero sobre la imagen 
     * 
     * @param evt evento de ratón
     */
    private void panelImagenMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelImagenMouseExited
        notifyPixelEvent(new PixelEvent(this,null, null, null));
    }//GEN-LAST:event_panelImagenMouseExited

    /**
     * Obtiene las coordenadas del pixel sobre el que se situa el puntero del 
     * ratón, así como su color, y actualiza la información en la ventana principal 
     * 
     * @param evt evento de ratón
     */
    private void panelImagenMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelImagenMouseMoved
        BufferedImage img = panelImagen.getImage();
        Point p = evt.getPoint();
        p.x /= panelImagen.getZoom(); // Obtenemos coordeanas sobre imagen teniendo en cuenta
        p.y /= panelImagen.getZoom(); // tanto el zoom como la posición centrada en el panel
        p.translate(-panelImagen.getImageLocation().x, -panelImagen.getImageLocation().y);
        if (img != null && p.x >= 0 && p.x < img.getWidth() && p.y >= 0 && p.y < img.getHeight()) {
            Color c = new Color(img.getRGB(p.x, p.y));
            Raster CAlfa = img.getAlphaRaster();
            notifyPixelEvent(new PixelEvent(this, p, c, CAlfa == null ? null : CAlfa.getSample(p.x, p.y, 0)));
        } else {
            notifyPixelEvent(new PixelEvent(this, null, null, null));
        }
        childPanelImagenMouseMoved(p);
    }//GEN-LAST:event_panelImagenMouseMoved
    /*
    Método pensado para que las subclases incorporen nuevas funcionalidades
    */
    protected void childPanelImagenMouseMoved(Point p){}
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    protected jfi.iu.ImagePanel panelImagen;
    private javax.swing.JScrollPane scrollPanel;
    // End of variables declaration//GEN-END:variables

}
