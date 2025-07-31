/* *****************************************************************************
 *  Name: Duarte Fernandes
 *  Date: May 4, 2024
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;

public class BruteCollinearPoints {
    private ArrayList<LineSegment> segments;

    /**
     * finds all line segments containing 4 points
     *
     * @param points a list of points
     */
    public BruteCollinearPoints(Point[] points) {
        Point[] copyPoints = this.validate(points);

        this.segments = new ArrayList<LineSegment>();

        for (int p = 0; p < copyPoints.length; p++) {
            for (int q = p + 1; q < copyPoints.length; q++) {
                for (int r = q + 1; r < copyPoints.length; r++) {
                    for (int s = r + 1; s < copyPoints.length; s++) {
                        double pSlopeQ = copyPoints[p].slopeTo(copyPoints[q]);
                        double pSlopeR = copyPoints[p].slopeTo(copyPoints[r]);
                        double pSlopeS = copyPoints[p].slopeTo(copyPoints[s]);
                        if (Double.compare(pSlopeQ, pSlopeR) == 0
                                && Double.compare(pSlopeQ, pSlopeS) == 0) {
                            this.segments.add(new LineSegment(copyPoints[p], copyPoints[s]));
                        }
                    }
                }
            }
        }
    }

    private Point[] validate(Point[] points) {
        if (points == null) {
            throw new IllegalArgumentException();
        }

        for (int i = 0; i < points.length; i++) {
            if (points[i] == null) {
                throw new IllegalArgumentException();
            }
        }

        Point[] auxPoints = points.clone();
        Arrays.sort(auxPoints);

        for (int i = 0; i < auxPoints.length - 1; i++) {
            if (auxPoints[i].compareTo(auxPoints[i + 1]) == 0) {
                throw new IllegalArgumentException();
            }
        }

        return auxPoints;
    }

    /**
     * the number of line segments
     *
     * @return number of segments
     */
    public int numberOfSegments() {
        return this.segments.size();
    }

    /**
     * the line segments
     *
     * @return the line segments.
     */
    public LineSegment[] segments() {
        return this.segments.toArray(new LineSegment[this.numberOfSegments()]);
    }

    public static void main(String[] args) {
        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}