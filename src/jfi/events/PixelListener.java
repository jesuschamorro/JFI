package jfi.events;

import java.util.EventListener;

/**
 *
 * @author Jesús Chamorro Martínez (jesus@decsai.ugr.es)
 */
public interface PixelListener extends EventListener {
    public void positionChange(PixelEvent evt);
}
