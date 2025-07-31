/* *****************************************************************************
 *  Name: Duarte Fernandes
 *  Date: May 12, 2024
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class Solver {
    private Stack<Board> solutionStack;
    private int minMoves = -1;

    private class SearchNode implements Comparable<SearchNode> {
        private Board board;
        private int manhattan;
        private int moves;
        private SearchNode parent;

        public SearchNode(Board board, int moves) {
            this.board = board;
            this.manhattan = board.manhattan();
            this.moves = moves;
        }

        public boolean isGoal() {
            return this.board.isGoal();
        }

        public int manhattanPriority() {
            return this.manhattan + this.moves;
        }

        public void setParent(SearchNode parent) {
            this.parent = parent;
        }

        public SearchNode getParent() {
            return this.parent;
        }

        public Board getBoard() {
            return this.board;
        }

        public Iterable<Board> getNeighbours() {
            return this.board.neighbors();
        }

        public int compareTo(SearchNode that) {
            if (this.manhattanPriority() > that.manhattanPriority()) {
                return 1;
            }

            if (this.manhattanPriority() < that.manhattanPriority()) {
                return -1;
            }

            return this.manhattan - that.manhattan;
        }
    }

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) {
            throw new IllegalArgumentException();
        }

        this.solutionStack = new Stack<>();
        this.aStar(initial);
    }

    private void aStar(Board initial) {
        MinPQ<SearchNode> openSet = new MinPQ<>();
        MinPQ<SearchNode> openSetTwin = new MinPQ<>();

        openSet.insert(new SearchNode(initial, 0));
        openSetTwin.insert(new SearchNode(initial.twin(), 0));

        while (!openSet.isEmpty()) {
            SearchNode current = openSet.delMin();
            SearchNode currentTwin = openSetTwin.delMin();

            if (current.isGoal()) {
                // Found the goal!
                this.reconstructPath(current);
                return;
            }

            if (currentTwin.isGoal()) {
                // Unsolvable!
                return;
            }

            for (Board neighbour : current.getNeighbours()) {
                if (current.getParent() != null && neighbour.equals(
                        current.getParent().getBoard())) {
                    continue;
                }

                SearchNode child = new SearchNode(neighbour, current.moves + 1);
                child.setParent(current);
                openSet.insert(child);
            }

            for (Board neighbour : currentTwin.getNeighbours()) {
                if (currentTwin.getParent() != null && neighbour.equals(
                        currentTwin.getParent().getBoard())) {
                    continue;
                }

                SearchNode child = new SearchNode(neighbour, currentTwin.moves + 1);
                child.setParent(currentTwin);
                openSetTwin.insert(child);
            }
        }

        // Open set is empty but goal was never reached.
    }

    private void reconstructPath(SearchNode current) {
        this.minMoves = current.moves;
        while (current != null) {
            this.solutionStack.push(current.getBoard());
            current = current.getParent();
        }
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return this.minMoves > -1;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        return this.minMoves;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        return this.isSolvable() ? this.solutionStack : null;
    }

    // test client (see below)
    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        // int[][] tiles = { { 1, 2, 3 }, { 4, 0, 5 }, { 7, 8, 6 } };
        Board initial = new Board(tiles);

        // StdOut.println(initial);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}