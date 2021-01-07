package jfi.geometry;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class representing a 3 dimensional vector.
 * 
 * @author Míriam Mengíbar Rodríguez (mirismr@correo.ugr.es)
 */
public class Vector3D {
    /**
     * Vector coordinates.
     */
    private Point3D coordinates;

    /**
     * Create a vector given its coordinates
     *
     * @param coordinates the vector coordinates
     */
    public Vector3D(Point3D coordinates) {
        this.coordinates = coordinates;
    }

    /**
     * Create a vector given its coordinates
     * 
     * @param coordinates the vector coordinates
     */
    public Vector3D(List<Double> coordinates) {
        this.coordinates = new Point3D(coordinates.get(0), coordinates.get(1), coordinates.get(2));
    }

    /**
     * Create a vector given two points. Vector coordinates are calculated as
     * destination - origin
     *
     * @param origin the origin point
     * @param destination the destination point.
     */
    public Vector3D(Point3D origin, Point3D destination) {
        try {
            this.coordinates = destination.subtract(origin);
        } catch (Exception ex) {
            Logger.getLogger(Vector3D.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Get the vector's coordinates
     * @return the vector's coordinates
     */
    public Point3D getCoordinates() {
        return coordinates;
    }

    /**
     * Set the vector's coordinates
     * @param coordinates the vector's coordinates
     */
    public void setCoordinates(Point3D coordinates) {
        this.coordinates = coordinates;
    }

    /**
     * Calcule the vector's module
     * @return the vector's module
     */
    public Double module() {
        Double module = 0.0;
        for (int i = 0; i < this.coordinates.getDimension(); i++) {
            try {
                module += this.coordinates.getValueOfCoordinate(i) * this.coordinates.getValueOfCoordinate(i);
            } catch (Exception ex) {
                Logger.getLogger(Vector3D.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return Math.sqrt(module);
    }

    /**
     * Check if this and another vector have same direction
     * @param v the second vector
     * @return true is both vectors have same direction, false otherwise
     */
    public boolean isSameDirection(Vector3D v) {
        double EPSILON = 0.0001;

        double alpha = this.dot(v) / (this.module() * v.module());
        if (1 - EPSILON < alpha && alpha < 1 + EPSILON) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Calcule scalar product between this vector and another one.
     * @param v the second vector
     * @return the scalar product
     */
    public Double dot(Vector3D v) {
        Double out = 0.0;
        
        for(int i = 0; i < this.coordinates.getDimension(); i++) {
            try {
                out += v.getCoordinates().getValueOfCoordinate(i) * this.coordinates.getValueOfCoordinate(i);
            } catch (Exception ex) {
                Logger.getLogger(Vector3D.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return out;
    }
    
    /**
     * Indicates whether some other object is "equal to" this one.
     *
     * @param vector reference object with which to compare.
     * @return {@code true} if this point is the same as the argument;
     * {@code false} otherwise.
     */
    public boolean equals(Vector3D vector) {
        return vector.coordinates.equals(this.coordinates);
    }
    
    /**
     * Returns a string that represents the value of this vector.
     *
     * @return a string representation of this vector.
     */
    public String toString() {
        return "Vector: "+this.coordinates.toString();
    }

}
