import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Arrays;

public class Board {
    private int[] tiles;
    private Board twin = null;
    private int dimension;
    private int manhattan;
    private int hamming;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        this.dimension = tiles.length;
        this.tiles = new int[this.dimension * this.dimension];

        for (int i = 0; i < this.dimension; i++) {
            for (int j = 0; j < this.dimension; j++) {
                this.tiles[i * this.dimension + j] = tiles[i][j];
            }
        }

        this.hamming = -1;
        this.manhattan = -1;
    }

    // string representation of this board
    public String toString() {
        StringBuilder boardString = new StringBuilder();
        boardString.append(this.dimension);

        for (int i = 0; i < this.tiles.length; i++) {
            if (i % this.dimension == 0) {
                boardString.append("\n");
            }
            boardString.append(String.format("%2d ", tiles[i]));
        }

        return boardString.toString();
    }

    private int getRow(int n) {
        if (this.isEmpty(n)) {
            return this.dimension - 1;
        }

        return (n - 1) / this.dimension;
    }

    private int getCol(int n) {
        if (this.isEmpty(n)) {
            return this.dimension - 1;
        }

        return (n - 1) % this.dimension;
    }

    // board dimension n
    public int dimension() {
        return this.dimension;
    }

    // number of tiles out of place
    public int hamming() {
        if (this.hamming != -1) {
            return this.hamming;
        }

        int res = 0;

        for (int i = 0; i < this.tiles.length; i++) {
            if (this.isEmpty(this.tiles[i])) {
                continue;
            }

            int row = this.getRow(this.tiles[i]);
            int col = this.getCol(this.tiles[i]);

            int x = this.getRow((i + 1) % this.tiles.length);
            int y = this.getCol((i + 1) % this.tiles.length);

            if (row != x || col != y) {
                res += 1;
            }
        }

        this.hamming = res;
        return res;
    }

    private boolean isEmpty(int n) {
        return (n == 0);
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        if (this.manhattan != -1) {
            return this.manhattan;
        }

        int res = 0;

        for (int i = 0; i < this.tiles.length; i++) {
            if (this.isEmpty(this.tiles[i])) {
                continue;
            }

            int row = this.getRow(this.tiles[i]);
            int col = this.getCol(this.tiles[i]);

            int x = this.getRow((i + 1) % this.tiles.length);
            int y = this.getCol((i + 1) % this.tiles.length);

            res += Math.abs(row - x) + Math.abs(col - y);
        }

        this.manhattan = res;
        return res;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return this.hamming() == 0;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y == null || y.getClass() != this.getClass()) {
            return false;
        }

        if (this == y) {
            return true;
        }

        Board that = (Board) y;

        if (this.dimension() != that.dimension()) {
            return false;
        }

        return Arrays.equals(this.tiles, that.tiles);
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        Queue<Board> neighbors = new Queue<Board>();

        for (int i = 0; i < this.tiles.length; i++) {
            if (!this.isEmpty(this.tiles[i])) {
                continue;
            }

            int x = i / this.dimension;
            int y = i % this.dimension;

            if (y > 0) {
                Board left = this.createNeighbor(i, i - 1);
                if (left != null) neighbors.enqueue(left);
            }

            if (x < this.dimension - 1) {
                Board down = this.createNeighbor(i, i + this.dimension);
                if (down != null) neighbors.enqueue(down);
            }

            if (y < this.dimension - 1) {
                Board right = this.createNeighbor(i, i + 1);
                if (right != null) neighbors.enqueue(right);
            }

            if (x > 0) {
                Board up = this.createNeighbor(i, i - this.dimension);
                if (up != null) neighbors.enqueue(up);
            }
            // found empty slot, break loop.
            break;
        }

        return neighbors;
    }

    private Board createNeighbor(int source, int destination) {
        if (destination < 0 || destination > this.tiles.length - 1) {
            return null;
        }

        int[][] boardCopy = this.copyBoard();

        int x = source / this.dimension;
        int y = source % this.dimension;

        int newX = destination / this.dimension;
        int newY = destination % this.dimension;


        int aux = boardCopy[x][y];
        boardCopy[x][y] = boardCopy[newX][newY];
        boardCopy[newX][newY] = aux;

        return new Board(boardCopy);
    }

    private int[][] copyBoard() {
        int[][] newBoard = new int[this.dimension][this.dimension];

        for (int i = 0; i < this.tiles.length; i++) {
            int x = i / this.dimension;
            int y = i % this.dimension;
            newBoard[x][y] = this.tiles[i];
        }

        return newBoard;
    }

    private int getRandomIndex() {
        return StdRandom.uniformInt(this.tiles.length);
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        if (this.twin != null) {
            return this.twin;
        }
        int source = this.getRandomIndex();
        while (this.isEmpty(this.tiles[source])) {
            source = this.getRandomIndex();
        }

        int destination = this.getRandomIndex();
        while (this.isEmpty(this.tiles[destination]) || source == destination) {
            destination = this.getRandomIndex();
        }

        this.twin = this.createNeighbor(source, destination);
        return this.twin;
    }

    // unit testing (not graded)
    public static void main(String[] args) {
        int[][] tiles = { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 0 } };
        Board bGoal = new Board(tiles);

        StdOut.println(bGoal);
        StdOut.println("Manhattan:");
        StdOut.println(bGoal.manhattan());
        StdOut.println("Hamming:");
        StdOut.println(bGoal.hamming());
        StdOut.println("isGoal (should be True):");
        StdOut.println(bGoal.isGoal());

        int[][] tiles2 = { { 8, 1, 3 }, { 4, 0, 2 }, { 7, 6, 5 } };
        Board bTest1 = new Board(tiles2);
        Board bTest2 = new Board(tiles2);


        StdOut.println("---");
        StdOut.println(bTest1);
        StdOut.println("Manhattan (should be 10):");
        StdOut.println(bTest1.manhattan());
        StdOut.println("Hamming (should be 5):");
        StdOut.println(bTest1.hamming());
        StdOut.println("isGoal (should be False):");
        StdOut.println(bTest1.isGoal());

        StdOut.println("--- Testing Equality ---");
        StdOut.println("Expected false:");
        StdOut.println(bGoal.equals(bTest1));
        StdOut.println("Expected false:");
        StdOut.println(bTest1.equals(bGoal));
        StdOut.println("Expected true:");
        StdOut.println(bTest1.equals(bTest2));
        StdOut.println("Expected true:");
        StdOut.println(bTest2.equals(bTest2));

        StdOut.println("--- Testing Iterable ---");

        int[][] tiles3 = { { 5, 0, 4 }, { 2, 3, 8 }, { 7, 1, 6 } };
        Board bTest3 = new Board(tiles3);
        StdOut.println(bTest3);
        StdOut.println("Manhattan (should be 10):");
        StdOut.println(bTest3.manhattan());
        StdOut.println("Hamming (should be 5):");
        StdOut.println(bTest3.hamming());
        StdOut.println("isGoal (should be False):");
        StdOut.println(bTest3.isGoal());

        StdOut.println("--- Neighbours: ---");

        for (Board neighbor : bTest3.neighbors()) {
            StdOut.println(neighbor);
        }

        StdOut.println("--- Twins: ---");
        StdOut.println(bTest3);
        StdOut.println("---");
        StdOut.println(bTest3.twin());
        StdOut.println(bTest3.twin());
        StdOut.println(bTest3.twin());
        StdOut.println(bTest3.twin());
    }
}
