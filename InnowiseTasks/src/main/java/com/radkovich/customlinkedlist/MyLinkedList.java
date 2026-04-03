package com.radkovich.customlinkedlist;

import java.util.NoSuchElementException;

public class MyLinkedList<E> {

    private MyNode<E> head;
    private MyNode<E> tail;
    private int size;

    public MyLinkedList() {
        this.head = null;
        this.tail = null;
        this.size = 0;
    }

    public int size() {
        return size;
    }

    public void addFirst(E el) {
        MyNode<E> newNode = new MyNode<>(el);

        if (size == 0) {
            head = tail = newNode;
        } else {
            newNode.next = head;
            head.prev = newNode;
            head = newNode;
        }
        size++;
    }

    public void addLast(E el) {
        MyNode<E> newNode = new MyNode<>(el);

        if (size == 0) {
            head = tail = newNode;
        } else {
            tail.next = newNode;
            newNode.prev = tail;
            tail = newNode;
        }
        size++;
    }

    public void add(int index, E el) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index out of bounds!");
        }

        if (index == size) {
            addLast(el);
            return;
        }

        MyNode<E> current = getNode(index);
        MyNode<E> newNode = new MyNode<>(el);
        MyNode<E> prevNode = current.prev;

        newNode.next = current;
        newNode.prev = prevNode;

        if (prevNode == null) {
            head = newNode;
        } else {
            prevNode.next = newNode;
        }
        current.prev = newNode;
        size++;
    }

    public E getFirst() {
        if (size == 0) {
            throw new NoSuchElementException("List is empty!");
        }
        return getNode(0).item;
    }

    public E getLast() {
        if (size == 0) {
            throw new NoSuchElementException("List is empty!");
        }
        return getNode(size - 1).item;
    }

    public E get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index out of bounds!");
        }
        return getNode(index).item;
    }

    public void removeFirst() {
        if (size == 0) {
            throw new NoSuchElementException("List is empty!");
        }

        if (head == tail) {
            head = tail = null;
        } else {
            head = head.next;
            head.prev = null;
        }
        size--;
    }

    public void removeLast() {
        if (size == 0) {
            throw new NoSuchElementException("List is empty!");
        }

        if (tail == null) {
            return;
        }
        remove(size - 1);
    }

    public void remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index out of bounds!");
        }

        MyNode<E> nodeToRemove = getNode(index);
        MyNode<E> prevNode = nodeToRemove.prev;
        MyNode<E> nextNode = nodeToRemove.next;

        if (prevNode == null) {
            head = nextNode;
        } else {
            prevNode.next = nextNode;
        }

        if (nextNode == null) {
            tail = prevNode;
        } else {
            nextNode.prev = prevNode;
        }
        size--;
    }

    private MyNode<E> getNode(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index out of bounds!");
        }
        if (index < (size / 2)) {
            MyNode<E> current = head;
            for (int i = 0; i < index; i++) {
                current = current.next;
            }
            return current;
        } else {
            MyNode<E> current = tail;
            for (int i = size - 1; i > index; i--) {
                current = current.prev;
            }
            return current;
        }
    }
}
