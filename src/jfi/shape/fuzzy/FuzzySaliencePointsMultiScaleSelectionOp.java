package jfi.shape.fuzzy;

import jfi.shape.Contour;
import jfi.utils.Pair;

import java.awt.geom.Point2D;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.IntStream;


/**
 * Class implementing the segmentation of a contour using multi scale method.
 * 
 * @author Alejandro Torres Aguilera (darthata@gmail.com)
 */
public class FuzzySaliencePointsMultiScaleSelectionOp extends FuzzySaliencePointsSelectionOp {
    private static double DEFAULT_ALPHACUT = 0.4;
    private static double MAX_SIGMA = 45;

    private double euclideanDistance2D(Point2D p, Point2D q){
        return Math.sqrt(Math.pow(2, p.getX() - q.getX()) + Math.pow(2, p.getY() - q.getY()));
    }

    public FuzzySaliencePointsMultiScaleSelectionOp(int window_size_curvacity, double alpha_curvacity, int window_size_maxima,
                                               double alpha_quantifier_almostall, double alpha_quantifier_enough,
                                               double beta_quantifier_enough) {
        super(window_size_curvacity, alpha_curvacity, window_size_maxima, alpha_quantifier_almostall,
                alpha_quantifier_enough, beta_quantifier_enough);
    }

    public FuzzySaliencePointsMultiScaleSelectionOp(){
        super();
    }


    /**
     * Calculate the absolute distance between the line defined by two points and
     * a third.
     * @param lineStart start point of the line.
     * @param lineEnd end point of the line.
     * @param point whose calculate the distance.
     * @return the distance between the line and the point.
     */
    private static double absDistanceBetweenLineAndPoint(Point2D lineStart, Point2D lineEnd, Point2D point){
        double x1 = lineStart.getX();
        double y1 = lineStart.getY();

        double x2 = lineEnd.getX();
        double y2 = lineEnd.getY();

        double x0 = point.getX();
        double y0 = point.getY();

        return Math.abs((y2-y1)*x0 - (x2-x1)*y0 + x2*y1 - y2*x1)/(Math.sqrt(Math.pow(y2 - y1,2) + Math.pow(x2 - x1,2)));
    }

    /**
     * Calculate the error between the original contour segment and the straight 
     * line that joins the two points.
     * @param segment of the contour.
     * @param startPoint start point of the line.
     * @param endPoint end point of the line.
     * @return the error between the contour segment and the line defined by the two points.
     */
    private static double errorSegments(List<Point2D> segment, Point2D startPoint, Point2D endPoint){
        double maxDistance = -Double.MAX_VALUE;
        double iDistance;
        for (Point2D point2D : segment) {
            iDistance = absDistanceBetweenLineAndPoint(startPoint, endPoint, point2D);
            if (iDistance > maxDistance) {
                maxDistance = iDistance;
            }
        }
        return maxDistance;
    }

    /**
     * It finds the nearest point in a set of points.
     * @param contour the contour whose find the nearest point.
     * @param point the point for find the nearest point.
     * @return the nearest point.
     */
    private Point2D nearestPoint(Contour contour, Point2D point){
        double pointMinDistance = Double.MAX_VALUE;
        double iPointDistance;
        Point2D nearestPoint = point;
        for (Point2D iPoint : contour) {
            iPointDistance = euclideanDistance2D(point, iPoint);
            if (iPointDistance < pointMinDistance) {
                nearestPoint = iPoint;
                pointMinDistance = iPointDistance;
            }
        }
        return nearestPoint;
    }
    
    /**
     * It finds the nearest point in a set of points.
     * @param listPoint the points whose find the nearest point.
     * @param point the point for find the nearest point.
     * @return the nearest point.
     */
    private Point2D nearestPoint(List<Point2D> listPoint, Point2D point){
        double pointMinDistance = Double.MAX_VALUE;
        double iPointDistance;
        Point2D nearestPoint = point;
        for (Point2D iPoint : listPoint) {
            iPointDistance = euclideanDistance2D(point, iPoint);
            if (iPointDistance < pointMinDistance) {
                nearestPoint = iPoint;
                pointMinDistance = iPointDistance;
            }
        }
        return nearestPoint;
    }
    
    /**
     * It calculates each point trajectory between the scales of a fuzzy contour.
     * @param scalesSaliencePoints the different fuzzy contour in each scale.
     * @return each scale with their points trajectory.
     */
    public Map<Double, Map<Point2D, Point2D>> pointsTrajectory(Map<Double, FuzzyContour> scalesSaliencePoints){
        Map<Double, Map<Point2D, Point2D>> pointsTrajectory = new ConcurrentHashMap();
        List<Double> sigmaList = new ArrayList(scalesSaliencePoints.keySet());
        Collections.sort(sigmaList);
        
        IntStream.range(0, sigmaList.size() - 1)
                .parallel()
                .forEachOrdered(i -> {
                    double currentSigma = sigmaList.get(i);
                    double nextSigma = sigmaList.get(i+1);
                    Map<Point2D, Point2D> pointsCorrespondence = new ConcurrentHashMap();
                    Contour currentPoints = scalesSaliencePoints.get(currentSigma).getContourReferenceSet();
                    Contour nextPoints = scalesSaliencePoints.get(nextSigma).getContourReferenceSet();
                    currentPoints.forEach(point -> {
                        pointsCorrespondence.put(nearestPoint(nextPoints, point), point);
                    });
                    pointsTrajectory.put(nextSigma, pointsCorrespondence);
                });
        return pointsTrajectory;
    }
    
    /**
     * It takes the scale and a point and returns the point correspondence in the 
     * next scale.
     * @param pointsTrajectoryTree the tree of trajectories.
     * @param sigma the current scale of the point. 
     * @param point the point whose find its correspondence.
     * @return the point correspondence in the next scale.
     */
    private Point2D pointScaleCorrespondence(Map<Double, Map<Point2D, Point2D>> pointsTrajectoryTree, Double sigma, Point2D point){
        if (pointsTrajectoryTree.containsKey(sigma)){
            Map<Point2D, Point2D> correspondencePoints = pointsTrajectoryTree.get(sigma);
            return correspondencePoints.get(point);
        }
        else {
            return point;
        }
    }
    
    /**
     * It takes the scale and a point and returns the point correspondence in the 
     * original contour.
     * @param pointsTrajectoryTree the tree of trajectories.
     * @param sigma the current scale of the point.
     * @param point the point whose find its correspondence.
     * @return the point correspondence in the original contour.
     */
    private Point2D pointOriginalScaleTrajectory(Map<Double, Map<Point2D, Point2D>> pointsTrajectoryTree, Double sigma, Point2D point){
        List<Double> sigmaList = new ArrayList(pointsTrajectoryTree.keySet());
        Collections.sort(sigmaList);
        
        Point2D correspondency = point;
        for (int i = sigmaList.indexOf(sigma); i >= 0; i--){
            double iSigma = sigmaList.get(i);
            Map<Point2D, Point2D> correspondencePoints = pointsTrajectoryTree.get(iSigma);
            List<Point2D> listOfPoints = new ArrayList(correspondencePoints.keySet());
            correspondency = correspondencePoints.get(this.nearestPoint(listOfPoints, correspondency));
        }
        return correspondency;
    }

    /**
     * It calculates recursively the salience points using the multi scale method.
     * @param originalContour the original contour.
     * @param start start point of the segment whose applies the algorithm.
     * @param end end point of the segment whose applies the algorithm.
     * @param sigmaScalesSaliencePoints the different scales points.
     * @param threshold the threshold for the error in the multi scale method.
     * @param pointsTrajectoryTree the tree of trajectories for each point.
     * @return the points that are saliency points after the multi scale method.
     */
    public List<Point2D> getSalienceMultiScale(Contour originalContour, Point2D start, Point2D end,
                                               Map<Double, FuzzyContour> sigmaScalesSaliencePoints,
                                               double threshold,
                                               Map<Double, Map<Point2D, Point2D>> pointsTrajectoryTree){
        // If we have scales...
        if (sigmaScalesSaliencePoints.size() > 0){
            Set<Point2D>  saliencePointsSet = new HashSet<>();

            Double maxSigma = sigmaScalesSaliencePoints.keySet().stream().max(Double::compare).get();
            FuzzyContour salienceFuzzyContour = sigmaScalesSaliencePoints.get(maxSigma);
            Contour salienceContour  = salienceFuzzyContour.getContourReferenceSet();
            List<Point2D> segmentSaliencePoints;
            if (start == null || end == null)
                segmentSaliencePoints = salienceContour;
            else {
                Point2D nearStart = this.nearestPoint(salienceContour,start);
                Point2D nearEnd = this.nearestPoint(salienceContour,end);
                int startIndex = salienceContour.indexOf(nearStart);
                int endIndex = salienceContour.indexOf(nearEnd);
                
                if (startIndex <= endIndex){
                    segmentSaliencePoints = salienceContour.getSegment(nearStart, nearEnd, true);
                }
                else{
                    segmentSaliencePoints = salienceContour.getSegment(nearStart, nearEnd, false);
                }
            }
            
            if (segmentSaliencePoints.size() > 1) {
                IntStream.range(0, segmentSaliencePoints.size())
                    //.parallel()
                    .forEach(i -> {
                        Point2D startPoint = segmentSaliencePoints.get(i % segmentSaliencePoints.size());
                        Point2D endPoint = segmentSaliencePoints.get((i + 1) % segmentSaliencePoints.size());
                        Point2D startPointOriginal = this.pointOriginalScaleTrajectory(pointsTrajectoryTree, maxSigma, startPoint);
                        Point2D endPointOriginal = this.pointOriginalScaleTrajectory(pointsTrajectoryTree, maxSigma, endPoint);

                        Map<Double, FuzzyContour> iSigmaScales = new ConcurrentHashMap(sigmaScalesSaliencePoints);

                        List<Point2D> originalSegment = originalContour.getSegment(startPointOriginal, endPointOriginal);
                        double segmentError = errorSegments(originalSegment, startPointOriginal, endPointOriginal);

                        saliencePointsSet.add(startPointOriginal);
                        if (segmentError > threshold) {
                            iSigmaScales.remove(maxSigma);
                            startPoint = pointScaleCorrespondence(pointsTrajectoryTree, maxSigma, startPoint);
                            endPoint = pointScaleCorrespondence(pointsTrajectoryTree, maxSigma, endPoint);
                            saliencePointsSet.addAll(getSalienceMultiScale(originalContour, startPoint, endPoint,
                                    iSigmaScales, threshold, pointsTrajectoryTree));
                        }
                        saliencePointsSet.add(endPointOriginal);
                    });
            }
            else if (segmentSaliencePoints.size() > 0) {
                Point2D startPoint = segmentSaliencePoints.get(0);
                saliencePointsSet.add(this.pointOriginalScaleTrajectory(pointsTrajectoryTree, maxSigma, startPoint));
            }
            else if (sigmaScalesSaliencePoints.size() > 1){
                Map<Double, FuzzyContour> iSigmaScales = new ConcurrentHashMap(sigmaScalesSaliencePoints);
                iSigmaScales.remove(maxSigma);
                saliencePointsSet.addAll(getSalienceMultiScale(originalContour, start, end,
                        iSigmaScales, threshold, pointsTrajectoryTree));
            }
            return new ArrayList(saliencePointsSet);
        }
        else{
            return new ArrayList<>();
        }
    }

    /**
     * Calculate the min salience error point.
     * @param originalContour whose calculate error
     * @param salienceMultiScale the list of salience points selected.
     * @return the point and his associate error.
     */
    private static Pair<Point2D, Double> minSalienceError(Contour originalContour, List<Point2D> salienceMultiScale){
        Pair<Point2D, Double> minError = new Pair(salienceMultiScale.get(0), Double.MAX_VALUE);
        double iError;
        List<Point2D> originalSegment;
        Point2D iStartPoint;
        Point2D iEndPoint;
        for(int i = 0; i < salienceMultiScale.size(); i++){
            iStartPoint = salienceMultiScale.get((i-1) < 0 ? salienceMultiScale.size() - 1 : (i-1));
            iEndPoint = salienceMultiScale.get((i+2) % salienceMultiScale.size());
            originalSegment = originalContour.getSegment(iStartPoint, iEndPoint);
            iError = errorSegments(originalSegment, iStartPoint, iEndPoint);
            if (iError < minError.getRight()){
                minError = new Pair(i, iError);
            }
        }
        return minError;
    }

    /**
     * Minimization algorithm for critic points.
     * @param originalContour
     * @param salienceMultiScale
     * @param threshold
     * @return
     */
    public List<Point2D> minSalienceMultiScale(Contour originalContour, List<Point2D> salienceMultiScale, double threshold){
        List<Point2D> minSalienceMultiScale = new ArrayList(salienceMultiScale);
        Pair<Point2D, Double> iMinError = minSalienceError(originalContour, minSalienceMultiScale);
        Point2D removablePoint;
        while(iMinError.getRight() <= threshold && minSalienceMultiScale.size() > 2){
            removablePoint = iMinError.getLeft();
            minSalienceMultiScale.remove(removablePoint);
            iMinError = minSalienceError(originalContour, minSalienceMultiScale);
        }
        /*
        It establish a min distance between the points and remove the relevant points that do not have this minimum distance between them
        For the moment I have not encounter eny need for use it.

        for (int i = 0; i < minSalienceMultiScale.size() && minSalienceMultiScale.size() > 2; i++){
            if (Math.abs(minSalienceMultiScale.get(i) - minSalienceMultiScale.get(((i+1) % minSalienceMultiScale.size()))) < MINIMUN_DISTANCE){
                minSalienceMultiScale.remove(i);
                i--;
            }
        }
        */
        return minSalienceMultiScale;
    }

    /**
     * Calculates the set of salience points associated to the given contour.
     *
     * @param contour contour to be analyzed.
     * @param alphacut the alpha associated to the alpha-cut of the saliency (fuzzy
     * property of a contour) that will be used to select the salience points.
     * Each (crisp) segment of the alpha-cut will have associated a salience
     * point (specifically, the centerpoint of the segment).
     * @return the set of salience points associated to the given contour. The
     * salience poits are returned as a <code>FuzzyContour</code>, where each
     * point has a membership degree to the saliency property.
     * @param sigma the sigma associated for calculate the scales of the contour.
     * @param threshold the threshold that the multi-scale method permit for the 
     * error.
     * @return
     */
    public List<Point2D> apply(Contour contour, double sigma, double threshold, double alphacut) {
        double maxSigma = 0.1 * contour.size() > MAX_SIGMA ? MAX_SIGMA : 0.1 * contour.size();
        
        List<Double> sigmaScales = new ArrayList();
        for (double iSigma = sigma; iSigma < maxSigma; iSigma *= sigma){
            sigmaScales.add(iSigma);
        }
        
        Map<Double, FuzzyContour> scalesFuzzyContours = new ConcurrentHashMap<>();
        
        Contour firstContour = contour;
        for (int i = 0; i < sigmaScales.size(); i++){
            double iSigma = sigmaScales.get(i);
            Contour iContour = contour.gaussianFiltering(iSigma);
            if (i == 0)
                firstContour = iContour;
            FuzzyContour iSaliencePoints = super.apply(iContour, alphacut);
            scalesFuzzyContours.put(iSigma, iSaliencePoints);
        }
               
        // Calculates the points trajectories in each scale.
        Map<Double, Map<Point2D, Point2D>> pointsTrajectoryTree = this.pointsTrajectory(scalesFuzzyContours);   
        
        List<Point2D> minSalience = this.getSalienceMultiScale(firstContour, null, null, 
                scalesFuzzyContours, threshold, pointsTrajectoryTree);
        minSalience = this.minSalienceMultiScale(firstContour, minSalience, threshold);

        return minSalience;
    }

    public List<Point2D> apply(Contour contour, double sigma, double threshold){
        return this.apply(contour, sigma, threshold, DEFAULT_ALPHACUT);
    }
}
