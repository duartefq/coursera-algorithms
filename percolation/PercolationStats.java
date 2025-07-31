/**
 * Name:              Duarte Fernandes
 * Coursera User ID:  c82c720edf73d8e72f3d7f09e52b0429
 * Last modified:     4/18/2024
 */

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;


public class PercolationStats {
    private double mean;
    private double stddev;
    private double confidenceLo;
    private double confidenceHi;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException();
        }

        this.runStats(n, trials);
    }

    private void runStats(int n, int trials) {
        double[] fractions = new double[trials];

        for (int i = 0; i < trials; i++) {
            Percolation p = new Percolation(n);

            while (!p.percolates()) {
                p.open(StdRandom.uniformInt(1, n + 1), StdRandom.uniformInt(1, n + 1));
            }

            fractions[i] = ((double) p.numberOfOpenSites()) / (n * n);
        }

        mean = StdStats.mean(fractions);
        stddev = StdStats.stddev(fractions);
        double CONFIDENCE_95 = 1.96;
        confidenceLo = mean - ((CONFIDENCE_95 * stddev) / Math.sqrt(trials));
        confidenceHi = mean + ((CONFIDENCE_95 * stddev) / Math.sqrt(trials));
    }

    // sample mean of percolation threshold
    public double mean() {
        return mean;
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return stddev;
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return confidenceLo;
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return confidenceHi;
    }

    // test client (see below)
    public static void main(String[] args) {
        if (args.length != 2) {
            throw new IllegalArgumentException("Please pass two arguments n and T.");
        }

        int n = Integer.parseInt(args[0]);

        int trials = Integer.parseInt(args[1]);

        PercolationStats stats = new PercolationStats(n, trials);

        String format = "%-24s = %s%n";

        System.out.printf(format, "mean", stats.mean());
        System.out.printf(format, "stddev", stats.stddev());
        System.out.printf(format, "95% confidence interval",
                          "[" + stats.confidenceLo() + ", " + stats.confidenceHi() + "]");
    }
}