package jfi.iu;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * Panel que contiene y muestra un conjunto de imágenes
 * 
 * @author Jesús Chamorro Martínez (jesus@decsai.ugr.es)
 */
public class MultipleImagePanel extends ImagePanel {
    /**
     * Imagen asociada al panel
     */
    private final ArrayList<BufferedImage> extraImages = new ArrayList<>();
    

    /**
     * Construye un panel sin imagen
     */
    public MultipleImagePanel() {
        this(null);
    }
    
    /**
     * Construye un panel con la imagen pasada por parámetro
     * 
     * @param img imagen
     */
    public MultipleImagePanel(BufferedImage img){
        super(img);
        initComponents();
    }
    
    /**
     * Añade una nueva imagen a la lista de imágenes del panel 
     * 
     * @param img 
     */
    public void addImage(BufferedImage img){
        if(this.getImage()==null){
            this.setImage(img);
        }
        else this.extraImages.add(img);
    }

    /**
     * Elimina la última imagen de la lista de imágenes del panel. En caso de 
     * solo quede una, deja a <tt>null</tt> la imagen asociada a este panel
     */
    public void removeImage(){        
        if(extraImages.isEmpty()){
            this.setImage(null);
        }
        else extraImages.remove(extraImages.size()-1);
    }
    
    /**
     * Devuelve el número de imagenes que hay en el panel
     * 
     * @return el número de imagenes del panel
     */
    public int numberOfImages(){
        int num = this.img!=null ? 1 : 0;
        num += extraImages.size();
        return num;
    }
    
    /**
     * Pinta el panel
     * 
     * @param g gráfico asociado al panel 
     */
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        for(BufferedImage img: extraImages){         
            if (img != null) {               
                g.drawImage(img, x_image, y_image, this);
            }
        }      
    }
    
     /*
     * Código generado por Netbeans para el diseño del interfaz
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

}
