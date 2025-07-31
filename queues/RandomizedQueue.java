/* *****************************************************************************
 *  Name: Duarte Fernandes.
 *  Date: April 28, 2024.
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private static final int INIT_CAPACITY = 50;
    private Item[] queue;
    private int head;

    private class ListIterator implements Iterator<Item> {
        private int[] shuffledIndices;
        private int current = head;

        public ListIterator() {
            shuffledIndices = StdRandom.permutation(head);
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public boolean hasNext() {
            return this.current > 0;
        }

        public Item next() {
            if (current == 0) {
                throw new NoSuchElementException();
            }

            return queue[shuffledIndices[--this.current]];
        }
    }

    // construct an empty randomized queue
    public RandomizedQueue() {
        this.queue = (Item[]) new Object[INIT_CAPACITY];
        this.head = 0;
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return this.head == 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return this.head;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }

        if (this.head == this.queue.length) {
            this.resize(this.queue.length * 2);
        }

        this.queue[this.head++] = item;
    }

    // remove and return a random item
    public Item dequeue() {
        if (this.isEmpty()) {
            throw new NoSuchElementException();
        }

        if (this.head > 0 && this.head == this.queue.length / 4) {
            this.resize(this.queue.length / 2);
        }

        int randomIndex = this.getRandomIndex();

        Item element = this.queue[randomIndex];

        this.queue[randomIndex] = this.queue[--this.head];

        this.queue[this.head] = null;

        return element;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (this.isEmpty()) {
            throw new NoSuchElementException();
        }

        return this.queue[this.getRandomIndex()];
    }

    private int getRandomIndex() {
        return StdRandom.uniformInt(this.head);
    }

    private void resize(int newSize) {
        Item[] copy = (Item[]) new Object[newSize];

        for (int i = 0; i < this.head; i++) {
            copy[i] = this.queue[i];
        }

        this.queue = copy;
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new ListIterator();
    }


    // unit testing (required)
    public static void main(String[] args) {
        RandomizedQueue<String> d = new RandomizedQueue<String>();

        StdOut.println("Expected: true");
        StdOut.println(d.isEmpty());
        StdOut.println("Enqueuing 'Item 1' ");
        d.enqueue("Item 1");
        StdOut.println("Enqueuing 'Item 2' ");
        d.enqueue("Item 2");
        StdOut.println("Enqueuing 'Item 3' ");
        d.enqueue("Item 3");
        StdOut.println("Enqueuing 'Item 4' ");
        d.enqueue("Item 4");
        StdOut.println("Enqueuing 'Item 5' ");
        d.enqueue("Item 5");

        StdOut.println("Expected: false");
        StdOut.println(d.isEmpty());
        StdOut.println("Expected: 5");
        StdOut.println(d.size());

        StdOut.println("------");
        StdOut.println("Sample: Random Item");
        StdOut.println(d.sample());
        StdOut.println("Sample: Random Item");
        StdOut.println(d.sample());
        StdOut.println("Sample: Random Item");
        StdOut.println(d.sample());
        StdOut.println("Sample: Random Item");
        StdOut.println(d.sample());
        StdOut.println("Sample: Random Item");
        StdOut.println(d.sample());
        StdOut.println("Sample: Random Item");
        StdOut.println(d.sample());
        StdOut.println("Sample: Random Item");
        StdOut.println(d.sample());
        StdOut.println("Sample: Random Item");
        StdOut.println(d.sample());
        StdOut.println("------");

        StdOut.println("Expected: 5 random items");
        for (String item : d) {
            StdOut.println(item);
        }

        StdOut.println("------");
        Iterator<String> iterator1 = d.iterator();
        Iterator<String> iterator2 = d.iterator();
        StdOut.println("Iterator1 Expected: 5 Random items");
        StdOut.print(iterator1.next() + " - ");
        StdOut.print(iterator1.next() + " - ");
        StdOut.print(iterator1.next() + " - ");
        StdOut.print(iterator1.next() + " - ");
        StdOut.print(iterator1.next());
        StdOut.println("");
        StdOut.println("Iterator2 Expected: 2 Random items");
        StdOut.print(iterator2.next() + " - ");
        StdOut.print(iterator2.next() + " - ");
        StdOut.print(iterator2.next() + " - ");
        StdOut.print(iterator2.next() + " - ");
        StdOut.print(iterator2.next());
        StdOut.println("");

        StdOut.println("");
        StdOut.println("------");
        StdOut.println("Expected: Removed Random");
        StdOut.println(d.dequeue());
        StdOut.println("Expected: Removed Random");
        StdOut.println(d.dequeue());

        StdOut.println("------");
        StdOut.println("Expected: 3 Random items");
        for (String item : d) {
            StdOut.println(item);
        }

        StdOut.println("------");
        StdOut.println("Expected: Removed 1 item:");
        StdOut.println(d.dequeue());

        StdOut.println("------");
        StdOut.println("Expected: 2 random items");
        for (String item : d) {
            StdOut.println(item);
        }

        StdOut.println("------");
        StdOut.println("Expected: Removed all remaining 2 items:");

        StdOut.println(d.dequeue());
        StdOut.println(d.dequeue());
        StdOut.println("Expected: List is empty: true");
        StdOut.println(d.isEmpty());

        int n = 5;
        RandomizedQueue<Integer> queue = new RandomizedQueue<Integer>();
        for (int i = 0; i < n; i++)
            queue.enqueue(i);
        for (int a : queue) {
            for (int b : queue)
                StdOut.print(a + "-" + b + " ");
            StdOut.println();
        }
    }
}
