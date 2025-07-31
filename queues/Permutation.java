/* *****************************************************************************
 *  Name: Duarte Fernandes.
 *  Date: April 28, 2024.
 **************************************************************************** */

import edu.princeton.cs.algs4.StdIn;

public class Permutation {
    public static void main(String[] args) {
        if (args.length != 1) {
            throw new IllegalArgumentException("Please pass 1 argument argument k.");
        }

        int k = Integer.parseInt(args[0]);

        RandomizedQueue<String> q = new RandomizedQueue<String>();

        while (!StdIn.isEmpty()) {
            String item = StdIn.readString();
            q.enqueue(item);
        }

        while (k > 0) {
            System.out.println(q.dequeue());
            k--;
        }
    }
}
