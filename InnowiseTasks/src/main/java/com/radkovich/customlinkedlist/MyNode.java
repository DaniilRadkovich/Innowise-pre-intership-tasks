package com.radkovich.customlinkedlist;

public class MyNode<E> {
    E item;
    MyNode<E> next;
    MyNode<E> prev;

    public MyNode(E element) {
        this.item = element;
    }
}
