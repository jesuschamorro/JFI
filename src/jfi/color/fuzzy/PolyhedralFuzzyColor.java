package jfi.color.fuzzy;

import java.awt.Color;
import java.util.List;
import jfi.fuzzy.FunctionBasedFuzzySet;
import jfi.fuzzy.membershipfunction.PolyhedralFunction;
import jfi.geometry.Point3D;
import jfi.geometry.Polyhedron;
import jfi.utils.Prototyped;

/**
 * Class representing a fuzzy color based on a polyhedral membership function.
 *
 * @author Míriam Mengíbar Rodríguez (mirismr@correo.ugr.es)
 */
public class PolyhedralFuzzyColor extends FunctionBasedFuzzySet<Point3D> implements FuzzyColor<Point3D>, Prototyped<Point3D> {

    /**
     * Creates a new fuzzy color based on a polyhedral membership function
     *
     * @param label the label associated to the fuzzy color.
     * @param centroid the center of the polyhedral function.
     * @param kernelVolume the polyhedron associate to the kernel of fuzzy set
     * @param alphaCut05Volume the polyhedron associate to the 0.5 alpha cut of
     * fuzzy set
     * @param supportVolume the polyhedron associate to the support of fuzzy set
     */
    public PolyhedralFuzzyColor(String label, Point3D centroid, Polyhedron kernelVolume, Polyhedron alphaCut05Volume, Polyhedron supportVolume) {
        super(label, new PolyhedralFunction(centroid, kernelVolume, alphaCut05Volume, supportVolume));
    }

    /**
     * Creates a new fuzzy color based on a polyhedral membership function with
     * a empty label
     *
     * @param centroid the center of the polyhedral function.
     * @param kernelVolume the polyhedron associate to the kernel of fuzzy set
     * @param alphaCut05Volume the polyhedron associate to the 0.5 alpha cut of
     * fuzzy set
     * @param supportVolume the polyhedron associate to the support of fuzzy set
     */
    public PolyhedralFuzzyColor(Point3D centroid, Polyhedron kernelVolume, Polyhedron alphaCut05Volume, Polyhedron supportVolume) {
        this("", centroid, kernelVolume, alphaCut05Volume, supportVolume);
    }

    /**
     * Returns the membership degree of the given crisp color to this fuzzy
     * color.
     *
     * @param c a crisp color.
     * @return the membership degree.
     */
    @Override
    public double membershipDegree(Color c) {
        Point3D p = new Point3D(c.getRed(), c.getGreen(), c.getBlue());
        return membershipDegree(p);
    }

    /**
     * Returns the prototype associated to this color which corresponds to the 
     * centroid of this polyhedral-based fuzzy color.
     * 
     * @return the prototype associated to this fuzzy color
     */
    @Override
    public Point3D getPrototype() {
        return ((PolyhedralFunction) this.getMembershipFunction()).getCentroid();
    }

    /**
     * Returns the volumes associated to this color which corresponds to the
     * kernel, 0.5 alpha-cut and support of this polyhedral-based fuzzy color.
     * @return the volumes associated to this fuzzy color.
     */
    public List<Polyhedron> getVolumes() {
        return ((PolyhedralFunction) this.mfunction).getVolumes();
    }

    /**
     * Returns a string that represents the value of this fuzzy color.
     *
     * @return a string representation of this fuzzy color.
     */
    @Override
    public String toString() {
        String out = "Label: " + this.label + "\t";
        out += "Centroid: " + this.getPrototype();
        return out;
    }
}
