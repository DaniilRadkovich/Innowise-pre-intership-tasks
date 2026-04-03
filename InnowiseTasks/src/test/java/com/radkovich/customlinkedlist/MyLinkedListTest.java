package com.radkovich.customlinkedlist;

import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class MyLinkedListTest {

    // Test for size()
    @Test
    void size_shouldReturnZeroWhenListIsEmpty() {
        MyLinkedList<String> list = new MyLinkedList<>();
        assertEquals(0, list.size());
    }
    // Test for addFirst()
    @Test
    void addFirst_shouldAddElementToTheBeginning() {
        MyLinkedList<String> list = new MyLinkedList<>();
        list.addFirst("first");
        assertEquals("first", list.getFirst());
        assertEquals(1, list.size());
    }
    // Test for addLast()
    @Test
    void addLast_shouldAddElementToTheEnd() {
        MyLinkedList<Integer> list = new MyLinkedList<>();
        list.addLast(10);
        assertEquals(10, list.getLast());
        assertEquals(1, list.size());
    }
    // Tests for add()
    @Test
    void add_shouldThrowExceptionForInvalidIndex() {
        MyLinkedList<Integer> list = new MyLinkedList<>();
        assertThrows(IndexOutOfBoundsException.class, () -> {
            list.add(5,44);
        });
    }
    // Test for add()
    @Test
    void add_shouldAddElementInExactIndex() {
        MyLinkedList<Integer> list = new MyLinkedList<>();
        list.addFirst(0);
        list.addFirst(1);
        list.add(1,44);
        assertEquals(3, list.size());
        assertEquals(44,list.get(1));
    }
    // Test for getFirst()
    @Test
    void getFirst_shouldThrowExceptionForEmptyList() {
        MyLinkedList<Integer> list = new MyLinkedList<>();
        assertThrows(NoSuchElementException.class, () -> {
            list.getFirst();
        });
    }
    // Test for getLast()
    @Test
    void getLast_shouldThrowExceptionForEmptyList() {
        MyLinkedList<Integer> list = new MyLinkedList<>();
        assertThrows(NoSuchElementException.class, () -> {
            list.getLast();
        });
    }
    // Test for get()
    @Test
    void get_shouldThrowExceptionForInvalidIndex() {
        MyLinkedList<String> list = new MyLinkedList<>();
        list.addLast("A");
        assertThrows(IndexOutOfBoundsException.class, () -> {
            list.get(5);
        });
    }
    // Test for removeFirst()
    @Test
    void removeFirst_shouldThrowExceptionForEmptyList() {
        MyLinkedList<Integer> list = new MyLinkedList<>();
        assertThrows(NoSuchElementException.class, () -> {
            list.removeFirst();
        });
    }
    // Test for removeLast()
    @Test
    void removeLast_shouldThrowExceptionForEmptyList() {
        MyLinkedList<Integer> list = new MyLinkedList<>();
        assertThrows(NoSuchElementException.class, () -> {
            list.removeLast();
        });
    }
    // Test for remove()
    @Test
    void remove_shouldThrowExceptionForInvalidIndex() {
        MyLinkedList<String> list = new MyLinkedList<>();
        list.addLast("A");
        assertThrows(IndexOutOfBoundsException.class, () -> {
            list.remove(5);
        });
    }
}
