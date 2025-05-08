import java.util.NoSuchElementException;

public class LinkedListDeque<T> {
    private static class Node<T> {
        private Node<T> previous, next;
        private T item;
        public Node(Node<T> previous, T item, Node<T> next) {
            this.item = item;
            this.previous = previous;
            this.next = next;
        }
        public Node(T item) {
            this( null, item, null);
        }
        public Node() {
            this(null, null, null);
        }
        public Node(T item, Node<T> next) {
            this( null, item, next);
        }
        public Node(Node<T> previous, T item) {
            this( previous, item, null);
        }
    }
    private Node<T> head, tail;
    private int size;
    public LinkedListDeque() {
        head = tail = null;
        size = 0;
    }
    public LinkedListDeque(T x) {
        head = new Node<>(x);
        tail = head;
        size = 1;
    }

    public void addFirst(T x) {
        Node<T> newNode = new Node<>(x);
        newNode.next = head;
        if (head != null) {
            head.previous = newNode;
        }
        head = newNode;
        size++;
    }
    public void addLast(T x) {
        Node<T> newNode = new Node<>(x);
        newNode.previous = tail;
        if (tail != null) {
            tail.next = newNode;
        }
        tail = newNode;
        size++;
    }
    public boolean isEmpty(){
        return size == 0;
    }
    public int size(){
        return size;
    }
    public void printDeque() {
        Node<T> current = head;
        while (current != null) {
            System.out.print(current.item + " ");
            current = current.next;
        }
        System.out.println();
    }
    public T removeFirst(){
        if (isEmpty()) return null;
        T x = head.item;
        if (size == 1) {
            head = tail = null;
        } else {
            head = head.next;
            head.previous = null;
        }
        size--;
        return x;
    }
    public T removeLast(){
        if (isEmpty()) return null;
        T x = tail.item;
        if (size == 1) {
            head = tail = null;
        } else {
            tail = tail.previous;
            tail.next = null;
        }
        size--;
        return x;
    }
    public T get(int index){
        Node<T> current = head;
        for (int i = 0; i < index; i++) {
            if (current == null) return null;
            current = current.next;
        }
        return current.item;
    }
    public T getRecursiveHelper(Node<T> current, int index){
        if (current == null) return null;
        if (index == 0) return current.item;
        return getRecursiveHelper(current.next, index - 1);
    }

}
