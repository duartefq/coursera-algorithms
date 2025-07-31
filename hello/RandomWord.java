import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdRandom;

public class RandomWord {
    public static void main(String[] args) {
        int i = 1;
        String name = "";
        String champion = "";
        while (!StdIn.isEmpty()) {
            name = StdIn.readString();
            if (!name.isEmpty()) {
                if (StdRandom.bernoulli(1.0 / i)) {
                    champion = name;
                }
                i++;
            }
        }
        System.out.println(champion);
    }
}
