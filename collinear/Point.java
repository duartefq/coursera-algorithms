/******************************************************************************
 *  Compilation:  javac Point.java
 *  Execution:    java Point
 *  Dependencies: none
 *
 *  An immutable data type for points in the plane.
 *  For use on Coursera, Algorithms Part I programming assignment.
 *
 ******************************************************************************/

import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;
import java.util.Comparator;

public class Point implements Comparable<Point> {

    private final int x;     // x-coordinate of this point
    private final int y;     // y-coordinate of this point

    /**
     * Initializes a new point.
     *
     * @param x the <em>x</em>-coordinate of the point
     * @param y the <em>y</em>-coordinate of the point
     */
    public Point(int x, int y) {
        /* DO NOT MODIFY */
        this.x = x;
        this.y = y;
    }

    /**
     * Draws this point to standard draw.
     */
    public void draw() {
        /* DO NOT MODIFY */
        StdDraw.point(x, y);
    }

    /**
     * Draws the line segment between this point and the specified point
     * to standard draw.
     *
     * @param that the other point
     */
    public void drawTo(Point that) {
        /* DO NOT MODIFY */
        StdDraw.line(this.x, this.y, that.x, that.y);
    }

    /**
     * Returns the slope between this point and the specified point.
     * Formally, if the two points are (x0, y0) and (x1, y1), then the slope
     * is (y1 - y0) / (x1 - x0). For completeness, the slope is defined to be
     * +0.0 if the line segment connecting the two points is horizontal;
     * Double.POSITIVE_INFINITY if the line segment is vertical;
     * and Double.NEGATIVE_INFINITY if (x0, y0) and (x1, y1) are equal.
     *
     * @param that the other point
     * @return the slope between this point and the specified point
     */
    public double slopeTo(Point that) {
        if (this.compareTo(that) == 0) {
            // (x0, y0) and (x1, y1) are equal
            return Double.NEGATIVE_INFINITY;
        }

        double deltaX = (that.x - this.x);

        if (Double.compare(deltaX, 0.0) == 0) {
            // the line segment is vertical
            return Double.POSITIVE_INFINITY;
        }

        double deltaY = (that.y - this.y);

        if (Double.compare(deltaY, 0.0) == 0) {
            // line segment connecting the two points is horizontal
            return +0.0;
        }

        // calculate the slope
        return deltaY / deltaX;
    }

    /**
     * Compares two points by y-coordinate, breaking ties by x-coordinate.
     * Formally, the invoking point (x0, y0) is less than the argument point
     * (x1, y1) if and only if either y0 < y1 or if y0 = y1 and x0 < x1.
     *
     * @param that the other point
     * @return the value <tt>0</tt> if this point is equal to the argument
     * point (x0 = x1 and y0 = y1);
     * a negative integer if this point is less than the argument
     * point; and a positive integer if this point is greater than the
     * argument point
     */
    public int compareTo(Point that) {
        if (this.y < that.y) {
            return -1;
        }

        if (this.y > that.y) {
            return 1;
        }

        return this.x - that.x;
    }

    private class BySlope implements Comparator<Point> {

        public int compare(Point o1, Point o2) {
            return Double.compare(Point.this.slopeTo(o1), Point.this.slopeTo(o2));
        }
    }

    /**
     * Compares two points by the slope they make with this point.
     * The slope is defined as in the slopeTo() method.
     *
     * @return the Comparator that defines this ordering on points
     */
    public Comparator<Point> slopeOrder() {
        return new BySlope();
    }


    /**
     * Returns a string representation of this point.
     * This method is provide for debugging;
     * your program should not rely on the format of the string representation.
     *
     * @return a string representation of this point
     */
    public String toString() {
        /* DO NOT MODIFY */
        return "(" + x + ", " + y + ")";
    }

    /**
     * Unit tests the Point data type.
     */
    public static void main(String[] args) {
        StdOut.println("---- Slopes ----");

        Point p1 = new Point(100, 150);
        Point p2 = new Point(200, 150);

        StdOut.println("Horizontal: positive zero. " + p1.slopeTo(p2));

        Point p3 = new Point(200, 150);
        Point p4 = new Point(200, 300);

        StdOut.println("Vertical: positive infinity. " + p3.slopeTo(p4));

        Point p5 = new Point(-200, 150);
        Point p6 = new Point(-200, 150);

        StdOut.println("between a point and itself: negative infinity. " + p5.slopeTo(p6));

        StdOut.println("---- Slope Order ----");

        StdOut.printf("Compare slots between %s and %s, %s: \n Expected to be -1: ", p1, p2, p4);
        StdOut.println(p1.slopeOrder().compare(p2, p4));

        StdOut.printf("Compare slots between %s and %s, %s: \n Expected to be 1: ", p1, p4, p2);
        StdOut.println(p1.slopeOrder().compare(p4, p2));

        StdOut.printf("Compare slots between %s and %s, %s: \n Expected to be 0: ", p1, p2, p3);
        StdOut.println(p1.slopeOrder().compare(p2, p3));

        Point[] points = new Point[] { p1, p2, p3, p4, p5 };

        for (Point point : points) {
            StdOut.print(point);
            StdOut.print(", ");
        }
        StdOut.print("\n");

        Arrays.sort(points, p5.slopeOrder());

        for (Point point : points) {
            StdOut.print(point);
            StdOut.print(", ");
        }
    }
}
