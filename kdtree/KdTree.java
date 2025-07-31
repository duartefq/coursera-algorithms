/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.ArrayList;

public class KdTree {
    private KdTree.Node root;     // root of the BST

    // BST helper node data type
    private static class Node {
        private Point2D p;           // key
        private KdTree.Node left, right;  // links to left and right subtrees
        private RectHV rect;
        private int size;

        public Node(Point2D p, RectHV rect, int size) {
            this.p = p;
            this.rect = rect;
            this.size = size;
        }

        public void draw() {
            StdDraw.setPenRadius(0.01);
            StdDraw.setPenColor(StdDraw.BLACK);
            this.p.draw();
        }
    }

    // construct an empty set of points
    public KdTree() {
    }

    // is the set empty?
    public boolean isEmpty() {
        return this.root == null;
    }

    // number of points in the set
    public int size() {
        return size(this.root);
    }

    private int size(Node x) {
        if (x == null) {
            return 0;
        }

        return x.size;
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        root = put(root, p, 0, 0.0, 0.0, 1.0, 1.0);
    }

    private Node put(Node current, Point2D p, int level, double xmin, double ymin, double xmax,
                     double ymax) {
        if (current == null) {
            return new Node(p, new RectHV(xmin, ymin, xmax, ymax), 1);
        }

        if (current.p.equals(p)) {
            return current;
        }

        // vertical split
        if (level % 2 == 0) {
            if (p.x() < current.p.x()) {
                // go left
                current.left = put(current.left, p, level + 1, xmin, ymin, current.p.x(), ymax);
            }
            else {
                // go right
                current.right = put(current.right, p, level + 1, current.p.x(), ymin, xmax, ymax);
            }
        }
        // horizontal split
        else {
            // go left (below)
            if (p.y() < current.p.y()) {
                current.left = put(current.left, p, level + 1, xmin, ymin, xmax, current.p.y());
            }
            // go right (above)
            else {
                current.right = put(current.right, p, level + 1, xmin, current.p.y(), xmax, ymax);
            }
        }

        current.size = size(current.left) + size(current.right) + 1;

        return current;
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        return get(root, p, 0) != null;
    }

    private Node get(Node current, Point2D p, int level) {
        if (current == null) {
            return null;
        }

        if (current.p.compareTo(p) == 0) {
            return current;
        }

        if (
                (level % 2 == 0 && p.x() < current.p.x())
                        || (level % 2 == 1 && p.y() < current.p.y())
        ) {
            return get(current.left, p, level + 1);
        }
        else {
            return get(current.right, p, level + 1);
        }
    }

    // draw all points to standard draw
    public void draw() {
        drawLines(root, 0);
        drawPoints(root);
    }

    private void drawLines(Node current, int level) {
        if (current == null) {
            return;
        }

        // vertical split
        if (level % 2 == 0) {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.line(current.p.x(), current.rect.ymin(), current.p.x(), current.rect.ymax());

        }
        // horizontal split
        else {
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.line(current.rect.xmin(), current.p.y(), current.rect.xmax(), current.p.y());
        }

        drawLines(current.left, level + 1);
        drawLines(current.right, level + 1);
    }

    private void drawPoints(Node current) {
        if (current == null) {
            return;
        }

        current.draw();

        drawPoints(current.left);
        drawPoints(current.right);
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new IllegalArgumentException();
        }

        ArrayList<Point2D> list = new ArrayList<Point2D>();

        if (root == null) {
            return list;
        }

        return buildList(root, rect, list, 0);
    }

    private Iterable<Point2D> buildList(Node current, RectHV rect, ArrayList<Point2D> list,
                                        int level) {
        if (current == null) {
            return list;
        }

        if (rect.contains(current.p)) {
            list.add(current.p);
        }

        if (
                (level % 2 == 0 && (rect.xmax() < current.p.x()))
                        || (level % 2 == 1 && (rect.ymax() < current.p.y()))
        ) {
            buildList(current.left, rect, list, level + 1);
        }
        else if (
                (level % 2 == 0 && (rect.xmin() > current.p.x()))
                        || (level % 2 == 1 && (rect.ymin() > current.p.y()))
        ) {
            buildList(current.right, rect, list, level + 1);
        }
        else {
            buildList(current.left, rect, list, level + 1);
            buildList(current.right, rect, list, level + 1);
        }


        return list;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }

        return nearest(root, p, 0, null);
    }

    /**
     * Nearest-neighbor search. To find a closest point to a given query point,
     * start at the root and recursively search in both subtrees using the following pruning rule:
     * - if the closest point discovered so far is closer than the distance between the query point
     * and the rectangle corresponding to a node,
     * there is no need to explore that node (or its subtrees).That is, search a node
     * only if it might contain a point that is closer than the best one found so far.
     * The effectiveness of the pruning rule depends on quickly finding a nearby point.
     * To do this, organize the recursive method so that when there are two possible subtrees to go
     * down,
     * you always choose the subtree that is on the same side of the splitting line as the query
     * point
     * as the first subtree to explore -- the closest point found while exploring the
     * first subtree may enable pruning of the second subtree.
     *
     * @param current
     * @param p
     * @param level
     * @param champion
     * @return
     */
    private Point2D nearest(Node current, Point2D p, int level, Point2D champion) {
        if (current == null) {
            return champion;
        }

        if (champion == null) {
            champion = current.p;
        }

        double championDistance = champion.distanceSquaredTo(p);

        if (current.p.distanceSquaredTo(p) < championDistance) {
            champion = current.p;
        }

        if (current.rect.distanceSquaredTo(p) < championDistance) {
            if (
                    (level % 2 == 0 && p.x() < current.p.x())
                            || (level % 2 == 1 && p.y() < current.p.y())
            ) {
                champion = nearest(current.left, p, level + 1, champion);
                champion = nearest(current.right, p, level + 1, champion);
            }
            else {
                champion = nearest(current.right, p, level + 1, champion);
                champion = nearest(current.left, p, level + 1, champion);
            }
        }

        return champion;
    }

    public static void main(String[] args) {
        KdTree kdtree = new KdTree();
        // kdtree.insert(new Point2D(0.206107, 0.095492));
        // kdtree.insert(new Point2D(0.975528, 0.654508));
        // kdtree.insert(new Point2D(0.024472, 0.345492));
        // kdtree.insert(new Point2D(0.793893, 0.095492));
        // kdtree.insert(new Point2D(0.793893, 0.904508));
        // kdtree.insert(new Point2D(0.975528, 0.345492));
        // kdtree.insert(new Point2D(0.206107, 0.904508));
        // kdtree.insert(new Point2D(0.500000, 0.000000));
        // kdtree.insert(new Point2D(0.024472, 0.654508));
        // kdtree.insert(new Point2D(0.500000, 1.000000));
        // System.out.println("isEmpty:");
        System.out.println(kdtree.isEmpty());

        kdtree.insert(new Point2D(0.7, 0.2));
        kdtree.insert(new Point2D(0.5, 0.4));
        kdtree.insert(new Point2D(0.2, 0.3));
        kdtree.insert(new Point2D(0.4, 0.7));
        kdtree.insert(new Point2D(0.9, 0.6));

        System.out.println("Contains:");
        System.out.println(kdtree.contains(new Point2D(0.024472, 0.345492)));
        System.out.println("Size:");
        System.out.println(kdtree.size());
        System.out.println("isEmpty:");
        System.out.println(kdtree.isEmpty());

        // [0.74, 0.96] x [0.15, 0.98]
        //  return "[" + xmin + ", " + xmax + "] x [" + ymin + ", " + ymax + "]";
        System.out.println("## Range");
        Iterable<Point2D> list = kdtree.range(new RectHV(0.74, 0.15, 0.96, 0.98));
        for (Point2D p : list) {
            System.out.println("Point: ");
            System.out.println(p);
        }
        // System.out.println("## Closest");
        // System.out.println(kdtree.nearest(new Point2D(0.500001, 1.000000)));

        StdDraw.clear();
        kdtree.draw();
        StdDraw.show();
    }
}
