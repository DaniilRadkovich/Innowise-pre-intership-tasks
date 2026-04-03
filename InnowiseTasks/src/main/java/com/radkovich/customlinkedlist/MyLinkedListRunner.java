package com.radkovich.customlinkedlist;

public class MyLinkedListRunner {

    public static void main(String[] args) {
        MyLinkedList<String> list = new MyLinkedList<>();

        list.addFirst("B");
        list.addFirst("A");
        list.addLast("C");
        list.addLast("D");
        list.addLast("E");
        list.addLast("F");
        list.add(1, "111");

        System.out.println("Size: " + list.size());

        System.out.println("Element at 2: " + list.get(2));

        for (int i = 0; i < list.size(); i++) {
            System.out.println("Element " + i + ": " + list.get(i));
        }

        System.out.println("---------------------------------------");
        System.out.println("Get first element: " + list.getFirst());
        System.out.println("Get last element: " + list.getLast());

        list.removeFirst();

        System.out.println("After removing first: ");

        for (int i = 0; i < list.size(); i++) {
            System.out.println("Element " + i + ": " + list.get(i));
        }
        System.out.println("---------------------------------------");

        list.removeLast();

        System.out.println("After removing last: ");

        for (int i = 0; i < list.size(); i++) {
            System.out.println("Element " + i + ": " + list.get(i));
        }
        System.out.println("---------------------------------------");

        list.remove(0);

        System.out.println("After removing by index 0: ");

        for (int i = 0; i < list.size(); i++) {
            System.out.println("Element " + i + ": " + list.get(i));
        }
    }
}
