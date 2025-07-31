import edu.princeton.cs.algs4.WeightedQuickUnionUF;

/**
 * Name:              Duarte Fernandes
 * Coursera User ID:  c82c720edf73d8e72f3d7f09e52b0429
 * Last modified:     4/18/2024
 */

public class Percolation {
    private WeightedQuickUnionUF ufPerc;
    private WeightedQuickUnionUF ufFull;

    private boolean[][] grid;

    private int length;

    private int virtualTop;
    private int virtualBottom;

    private int count;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException();
        }

        length = n;

        int idsSize = length * length + 2;
        ufFull = new WeightedQuickUnionUF(idsSize);
        ufPerc = new WeightedQuickUnionUF(idsSize);

        grid = new boolean[length][length];
        virtualTop = length * length;
        virtualBottom = length * length + 1;

        for (int i = 0; i < length; i++) {
            // virtual top
            this.union(getIndex(0, i), virtualTop);
            // virtual bottom
            ufPerc.union(getIndex(length - 1, i), virtualBottom);
        }

    }

    private void validate(int row, int col) {
        if (row > length || row < 1) {
            throw new IllegalArgumentException("row " + row + " is not between 0 and " + (length));
        }

        if (col > length || col < 1) {
            throw new IllegalArgumentException("row " + col + " is not between 0 and " + (length));
        }
    }

    private int getIndex(int row, int col) {
        return row * length + col;
    }

    private void union(int p, int q) {
        ufFull.union(p, q);
        ufPerc.union(p, q);
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        validate(row, col);

        row -= 1;
        col -= 1;

        int current = getIndex(row, col);

        if (!grid[row][col]) {
            count++;
        }

        grid[row][col] = true;


        // top
        if (row > 0 && grid[row - 1][col]) {
            this.union(current, getIndex(row - 1, col));
        }
        // bottom
        if (row < (length - 1) && grid[row + 1][col]) {
            this.union(current, getIndex(row + 1, col));
        }
        // right
        if (col < (length - 1) && grid[row][col + 1]) {
            this.union(current, getIndex(row, col + 1));
        }
        // left
        if (col > 0 && grid[row][col - 1]) {
            this.union(current, getIndex(row, col - 1));
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        validate(row, col);

        return grid[row - 1][col - 1];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        validate(row, col);

        row--;
        col--;

        if (!grid[row][col]) {
            return false;
        }

        int current = this.getIndex(row, col);

        return ufFull.find(current) == ufFull.find(virtualTop);
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return count;
    }

    // does the system percolate?
    public boolean percolates() {
        if (length == 1) {
            return grid[0][0];
        }
        return ufPerc.find(virtualTop) == ufPerc.find(virtualBottom);
    }

    // test client (optional)
    public static void main(String[] args) {
        Percolation p = new Percolation(2);
        // p.open(1, 1);
        // System.out.println(p.isFull(1, 1));
        p.open(2, 1);
        System.out.println(p.isFull(2, 1));
        // p.open(1, 2);
        // System.out.println(p.isFull(1, 2));
        // p.open(2, 2);
        // System.out.println(p.isFull(2, 2));
    }
}
