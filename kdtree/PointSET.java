/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdDraw;

public class PointSET {
    private SET<Point2D> set;

    // construct an empty set of points
    public PointSET() {
        this.set = new SET<Point2D>();
    }

    // is the set empty?
    public boolean isEmpty() {
        return this.set.isEmpty();
    }

    // number of points in the set
    public int size() {
        return this.set.size();
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        this.set.add(p);
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        return this.set.contains(p);
    }

    // draw all points to standard draw
    public void draw() {
        StdDraw.setPenColor(StdDraw.BLACK);
        for (Point2D p : this.set) {
            p.draw();
        }
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new IllegalArgumentException();
        }

        SET<Point2D> list = new SET<Point2D>();

        for (Point2D p : this.set) {
            if (rect.contains(p)) {
                list.add(p);
            }
        }

        return list;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }

        Point2D current = null;

        for (Point2D q : this.set) {
            if (current == null) {
                current = q;
                continue;
            }

            if (q.distanceSquaredTo(p) < current.distanceSquaredTo(p)) {
                current = q;
            }
        }

        return current;
    }

    public static void main(String[] args) {
        PointSET pointSet = new PointSET();
        pointSet.insert(new Point2D(0.1, 0.2));
        pointSet.insert(new Point2D(0.3, 0.4));
        pointSet.insert(new Point2D(0.3, 0.5));
        pointSet.insert(new Point2D(1, 1));
        System.out.println("Nearest?");
        System.out.println(pointSet.nearest(new Point2D(0.2, 0.4)));

        System.out.println("Range:");
        for (Point2D p : pointSet.range(new RectHV(0.2, 0.2, 0.3, 0.5))) {
            System.out.println(p);
        }
    }
}
