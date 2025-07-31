/* *****************************************************************************
 *  Name: Duarte Fernandes.
 *  Date: April 28, 2024.
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
    private Node<Item> first;
    private Node<Item> last;
    private int size;

    private class Node<NodeItem> {
        NodeItem value;
        Node<NodeItem> next;
        Node<NodeItem> prev;
    }

    private class ListIterator implements Iterator<Item> {
        private Node<Item> current = first;

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public boolean hasNext() {
            return current != null;
        }

        public Item next() {
            if (current == null) {
                throw new NoSuchElementException();
            }

            Item item = current.value;
            current = current.next;
            return item;
        }
    }

    /**
     * construct an empty deque
     */
    public Deque() {
        this.first = null;
        this.last = null;
        this.size = 0;
    }

    /**
     * @return is the deque empty?
     */
    public boolean isEmpty() {
        return this.first == null && this.last == null;
    }

    /**
     * @return the number of items on the deque
     */
    public int size() {
        return this.size;
    }

    /**
     * add the item to the front
     */
    public void addFirst(Item item) {
        this.validate(item);
        Node<Item> oldFirst = this.first;
        this.first = new Node<Item>();
        this.first.value = item;
        this.first.next = oldFirst;

        if (oldFirst != null) {
            oldFirst.prev = this.first;
        }

        if (this.last == null) {
            this.last = this.first;
        }

        this.size++;
    }

    /**
     * add the item to the back
     */
    public void addLast(Item item) {
        this.validate(item);

        Node<Item> newNode = new Node<Item>();
        newNode.value = item;

        if (this.last == null) {
            this.last = newNode;
            this.first = newNode;
        }
        else {
            this.last.next = newNode;
            newNode.prev = this.last;
            this.last = newNode;
        }

        this.size++;
    }

    /**
     * remove and return the item from the front
     */
    public Item removeFirst() {
        this.checkEmptyList();
        Node<Item> oldFirst = this.first;

        this.first = this.first.next;

        if (this.first != null) {
            this.first.prev = null;
        }
        else {
            this.last = null;
        }

        this.size--;

        return oldFirst.value;
    }

    /**
     * remove and return the item from the back
     * @return the item removed from the back
     */
    public Item removeLast() {
        this.checkEmptyList();
        Node<Item> oldLast = this.last;

        this.last = this.last.prev;

        if (this.last != null) {
            this.last.next = null;
        }
        else {
            this.first = null;
        }

        this.size--;

        return oldLast.value;
    }

    /**
     * @return an iterator over items in order from front to back
     */
    public Iterator<Item> iterator() {
        return new ListIterator();
    }

    /**
     * support method: validate item
     */
    private void validate(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * support method: check if can remove item
     */
    private void checkEmptyList() {
        if (this.isEmpty()) {
            throw new NoSuchElementException();
        }
    }


    /**
     * unit testing (required)
     */
    public static void main(String[] args) {
        Deque<Integer> d = new Deque<Integer>();

        StdOut.println("Expected: true");
        StdOut.println(d.isEmpty());
        d.addFirst(1);
        d.addFirst(2);
        d.addLast(3);
        d.addLast(4);
        d.addLast(5);

        StdOut.println("Expected: false");
        StdOut.println(d.isEmpty());
        StdOut.println("Expected: 5");
        StdOut.println(d.size());

        StdOut.println("Expected: 2 1 3 4 5");
        for (Integer item : d) {
            StdOut.println(item);
        }

        StdOut.println("Expected: 5");
        StdOut.println(d.removeLast());

        StdOut.println("Expected: 2");
        StdOut.println(d.removeFirst());

        StdOut.println("Expected: 1 3 4");
        for (Integer item : d) {
            StdOut.println(item);
        }

        Iterator<Integer> i = d.iterator();
        StdOut.println("Expected: 1 3");
        StdOut.println(i.next());
        StdOut.println(i.next());

        StdOut.println("Expected: 4");
        StdOut.println(d.removeLast());

        StdOut.println("Expected: 1");
        StdOut.println(d.removeFirst());

        StdOut.println("Expected: 3");
        for (Integer item : d) {
            StdOut.println(item);
        }

        StdOut.println("Expected: 3");
        StdOut.println(d.removeFirst());

        StdOut.println("Expected: true");
        StdOut.println(d.isEmpty());

        // Deque<Integer> deque = new Deque<>();
        // StdOut.println("Expected: true");
        // StdOut.println(deque.isEmpty());
        // deque.addLast(3);
        // deque.addFirst(4);
        // StdOut.println("Expected: false");
        // StdOut.println(deque.isEmpty());
        // StdOut.println(deque.removeFirst());
        // StdOut.println(deque.removeLast());
        // deque.addFirst(8);
        // StdOut.println("Expected: false");
        // StdOut.println(deque.isEmpty());
        // StdOut.println(deque.removeLast());
        // StdOut.println("Expected: true");
        // StdOut.println(deque.isEmpty());
    }
}
