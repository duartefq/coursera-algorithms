/* *****************************************************************************
 *  Name: Duarte Fernandes
 *  Date: 2025-07-28
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdDraw;

public class PointSET {
    private SET<Point2D> set;

    /**
     * Construct an empty set of points
     */
    public PointSET() {
        this.set = new SET<Point2D>();
    }

    /**
     * @return true if the set is empty, false otherwise
     */
    public boolean isEmpty() {
        return this.set.isEmpty();
    }

    /**
     * @return number of points in the set
     */
    public int size() {
        return this.set.size();
    }

    /**
     * Add the point to the set (if it is not already in the set).
     *
     * @throws IllegalArgumentException if p is null
     */
    public void insert(Point2D p) {
        this.set.add(p);
    }

    /**
     * Checks if the point p is in the set.
     *
     * @return does the set contain point p?
     */
    public boolean contains(Point2D p) {
        return this.set.contains(p);
    }

    /**
     * Draw all points to standard draw.
     */
    public void draw() {
        StdDraw.setPenColor(StdDraw.BLACK);
        for (Point2D p : this.set) {
            p.draw();
        }
    }

    /**
     * @return all points that are inside the rectangle (or on the boundary)
     * @throws IllegalArgumentException if rect is null
     */
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

    /**
     * @return a nearest neighbor in the set to point p; null if the set is empty
     * @throws IllegalArgumentException if p is null
     */
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
