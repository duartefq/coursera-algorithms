/* *****************************************************************************
 *  Name: Duarte Fernandes
 *  Date: May 4, 2024
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;

public class FastCollinearPoints {
    private ArrayList<LineSegment> segments;

    /**
     * finds all line segments containing 4 or more points
     *
     * @param points list of points
     */
    public FastCollinearPoints(Point[] points) {
        Point[] auxPoints = this.validate(points);

        int pointsLength = auxPoints.length;

        this.segments = new ArrayList<LineSegment>();

        for (int i = 0; i < pointsLength - 3; i++) {
            Point[] copyPoints = auxPoints.clone();

            Arrays.sort(copyPoints, copyPoints[i].slopeOrder());

            int p = 0;
            int first = 1;
            int last = 2;

            while (last < pointsLength) {
                while (last < pointsLength
                        && Double.compare(copyPoints[p].slopeTo(copyPoints[first]),
                                          copyPoints[p].slopeTo(copyPoints[last])) == 0) {
                    last++;
                }

                // Since points have been sorted by natural order,
                // we can avoid adding duplicate line segments by checking
                // if p < q0 before adding to the list of segments.
                // p -> q0 -> q1 -> q2 -> q3
                if (last - first >= 3 && copyPoints[p].compareTo(copyPoints[first]) < 0) {
                    this.segments.add(new LineSegment(copyPoints[p], copyPoints[last - 1]));
                }

                first = last;
                last++;
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
     * @return number of line segments
     */
    public int numberOfSegments() {
        return this.segments.size();
    }

    /**
     * @return the line segments
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
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
