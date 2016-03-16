
package jfi.shape;

import java.awt.Point;
import java.awt.geom.Point2D;
import jfi.fuzzy.FuzzyElement;

public class FuzzyPoint extends Point2D implements FuzzyElement<Point2D> {

    private Point2D.Double point;
    public float degree;

    public FuzzyPoint(Point2D p, float degree) {
        this.point = new Point.Double(p.getX(),p.getY());
        this.degree = degree;
    }

    public FuzzyPoint(int x, int y, float degree) {
        this.point = new Point.Double(x,y);
        this.degree = degree;
    }

    public FuzzyPoint() {
        this(0, 0, 1.0f);
    }

    /**
     * Returns a string representation of this fuzzy point
     *
     * @return a string representation of this fuzzy point
     */
    @Override
    public String toString() {
        return getClass().getName() + "[x=" + this.getX() + ",y=" + this.getY() + ",dg=" + degree + "]";
    }

    @Override
    public Point2D getElement() {
        return new Point2D.Double(this.getX(), this.getY());
    }

    @Override
    public double getDegree() {
        return degree;
    }

    @Override
    public double getX() {
        return this.point.x;
    }

    @Override
    public double getY() {
        return this.point.y;
    }

    @Override
    public void setLocation(double x, double y) {
        this.point.x = x;
        this.point.y = y;
    }
}
