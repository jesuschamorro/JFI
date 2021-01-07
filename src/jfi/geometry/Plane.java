package jfi.geometry;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class representing a plane. We can define a plane 
 * as x1*a1 + x2*a2 + x3*a3 + b = 0,
 * where ai are the normal vector component to the plane
 *
 * @author Míriam Mengíbar Rodríguez (mirismr@correo.ugr.es)
 */
public class Plane {

    /**
     * A orthogonal (normal) vector to the plane.
     */
    private Vector3D orthogonalVector;

    /**
     * The 'b' termin in plane equation.
     */
    private Double independentTerm;

    /**
     * Create a hyperplane through a orthogonal vector to the plane and a point
     * belonging to the hyperplane.
     *
     * @param orthogonalVector the orthogonal vector.
     * @param belongerPoint a point belonging to the hyperplane.
     */
    public Plane(Vector3D orthogonalVector, Point3D belongerPoint) {
        this.modifyData(orthogonalVector, belongerPoint);
    }

    /**
     * Create a new plane through his general equation
     * x1*a1 + x2*a2 + x3*a3 + b = 0
     *
     * @param a1 the first component defining orthogonal vector
     * @param a2 the second component defining orthogonal vector
     * @param a3 the third component defining orthogonal vector
     * @param b the independent term (b)
     */
    public Plane(double a1, double a2, double a3, double b) {
        this.orthogonalVector = new Vector3D(new Point3D(a1, a2, a3));
        this.independentTerm = b;
    }

    /**
     * Update data according to an orthogonal vector and a belonger point.
     *
     * @param orthogonalVector The orthogonal vector to this plane
     * @param belongerPoint The point which belongs to this plane
     */
    public void modifyData(Vector3D orthogonalVector, Point3D belongerPoint) {
        this.orthogonalVector = orthogonalVector;
        try {
            // evaluate the belonger point in the plane and
            // solve the equation for 'b'
            this.independentTerm = this.coeficientsEvaluation(belongerPoint) * (-1);
        } catch (Exception ex) {
            Logger.getLogger(Plane.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Vector3D getOrthogonalVector() {
        return orthogonalVector;
    }

    public Double getIndependentTerm() {
        return independentTerm;
    }

    /**
     * Evaluate a point in the plane follow the equation 
     * x1*a1 + x2*a2 + x3*a3 + b = 0
     * @param p a point to be evaluated. p is represented as p = (x1, x2, x3)
     * @return the evaluation result.
     */
    public double evaluatePoint(Point3D p) throws Exception {
        Double coefEvaluation = this.coeficientsEvaluation(p);
        return coefEvaluation + this.independentTerm;
    }

    /**
     * Replace the point coordinates in the plane equation. This method is used
     * internally.
     *
     * @param p the point
     * @return the sumatory of ai*xi
     */
    private Double coeficientsEvaluation(Point3D p) {
        Double coefEvaluation = 0.0;

        for (int i = 0; i < p.getDimension(); i++) {
            coefEvaluation += p.getValueOfCoordinate(i) * this.orthogonalVector.getCoordinates().getValueOfCoordinate(i);
        }

        return coefEvaluation;
    }

    /**
     * Check if a point is contained by the plane.
     *
     * @param pointToEvaluate point to check
     * @return true if point is in plane, false otherwise.
     */
    public boolean isInPlane(Point3D pointToEvaluate) {
        double EPSILON = 0.0001;
        try {
            Double eval = this.evaluatePoint(pointToEvaluate);
            return eval > -1 * EPSILON && eval < EPSILON;
        } catch (Exception ex) {
            Logger.getLogger(Plane.class.getName()).log(Level.SEVERE, null, ex);
        }

        return false;
    }

    /**
     * Give the intersection point with a line. We substitute the parametric
     * equation's line in the general equation's hyperplane. Calculate lamda as:
     * lamda =     -D - O1*P1 - O2*P2 - O3*P3
     *         ----------------------------------
     *                    V1 + V2 + V3
     * where O is the hyperplane's orthogonal vector, P is a point belonging
     * the line, V the line's director vector and D is the hyperplane's 
     * indepent.
     *
     * @param line to insersect the hyperplane
     * @return the insersection point
     */
    public Point3D getIntersectionPoint(Line3D line) {
        // calculate numerator
        Double numerator = -1 * this.independentTerm;
        for (int i = 0; i < this.orthogonalVector.getCoordinates().getDimension(); i++) {
            try {
                Double Oi = this.orthogonalVector.getCoordinates().getValueOfCoordinate(i);
                Double Pi = line.getBelongerPoint().getValueOfCoordinate(i);

                numerator -= Oi * Pi;
            } catch (Exception ex) {
                Logger.getLogger(Plane.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        // calculate denominator
        Double denominator = 0.0;
        for (int i = 0; i < line.getDirectorVector().getCoordinates().getDimension(); i++) {
            try {
                // sum each Vi
                denominator += this.orthogonalVector.getCoordinates().getValueOfCoordinate(i) * line.getDirectorVector().getCoordinates().getValueOfCoordinate(i);
            } catch (Exception ex) {
                Logger.getLogger(Plane.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        Double lambda = numerator / denominator * 1.0;
        Point3D out = line.getPointSubstitutingLambda(lambda);
        return out;
    }

    /**
     * Check if two planes are equals. Two planes are equals if their othogonal
     * vectors are equals and their independent term 'b' are equals.
     *
     * @param plane to compare
     * @return true if both planes are equals, false otherwise.
     */
    public boolean equals(Plane plane) {
        return this.orthogonalVector.equals(plane.getOrthogonalVector()) && plane.getIndependentTerm().equals(this.getIndependentTerm());
    }

    /**
     * Calculates the distance between this plane and a point.
     * @param point the point where the distances is calculated to.
     * @return the distance calculated
     */
    public double distanceToPoint(Point3D point) {
        double coefA = this.orthogonalVector.getCoordinates().getX();
        double coefB = this.orthogonalVector.getCoordinates().getY();
        double coefC = this.orthogonalVector.getCoordinates().getZ();
        return (Math.abs(point.getX() * coefA + point.getY() * coefB + point.getZ() * coefC + this.independentTerm) / (double) Math.sqrt(coefA * coefA + coefB * coefB + coefC * coefC));
    }

    /**
     * Create a parallel plane to this
     *
     * @param dist distance between this and the new plane we want create.
     * @return the parallel plane
     */
    public Plane parallelPlane(double dist) {
        double module = this.orthogonalVector.module();
        double coefA = this.orthogonalVector.getCoordinates().getX();
        double coefB = this.orthogonalVector.getCoordinates().getY();
        double coefC = this.orthogonalVector.getCoordinates().getZ();
        return new Plane(coefA, coefB, coefC, this.independentTerm + dist * module);
    }

    /**
     * Create a perpendicular plane given a vector and a point
     *
     * @param vector the vector
     * @param point the point
     * @return A perpendicular plane
     */
    public static Plane perpendicularPlane(Vector3D vector, Point3D point) {
        double A = vector.getCoordinates().getX();
        double B = vector.getCoordinates().getY();
        double C = vector.getCoordinates().getZ();
        double total = Math.max(Math.abs(A), Math.max(Math.abs(B), Math.abs(C)));
        A /= total;
        B /= total;
        C /= total;
        return new Plane(new Vector3D(new Point3D(A, B, C)), point);
    }

    /**
     * Returns a string that represents this plane.
     *
     * @return a string representation of this plane.
     */
    public String toString() {
        String out = "Plane[";
        for (int i = 0; i < this.orthogonalVector.getCoordinates().getDimension(); i++) {
            out += this.orthogonalVector.getCoordinates().getValueOfCoordinate(i) + ", ";
        }
        out += this.independentTerm + "]";
        return out;
    }
}
