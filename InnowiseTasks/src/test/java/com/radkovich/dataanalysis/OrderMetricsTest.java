package com.radkovich.dataanalysis;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class OrderMetricsTest {
    private List<Order> sampleOrders;
    private OrderMetrics orderMetrics;

    @BeforeEach
    public void setUp() {
        sampleOrders = new ArrayList<>();
        orderMetrics = new OrderMetrics();

        sampleOrders.add(new Order("0",
                LocalDateTime.now(),
                new Customer("0", "Vasya", "test0@test.com", LocalDateTime.now(), 20, "New York"),
                List.of(new OrderItem("Door", 1, 100.0, Category.HOME),
                        new OrderItem("Window", 2, 130.0, Category.HOME)),
                OrderStatus.NEW
        ));

        sampleOrders.add(new Order(
                "1",
                LocalDateTime.now(),
                new Customer("1", "Dima", "test1@test.com", LocalDateTime.now(), 47, "New Vegas"),
                List.of(new OrderItem("Macbook", 1, 1700.0, Category.ELECTRONICS),
                        new OrderItem("Mouse", 1, 99.0, Category.ELECTRONICS),
                        new OrderItem("Display", 1, 1000.0, Category.ELECTRONICS)),
                OrderStatus.PROCESSING
        ));

        sampleOrders.add(new Order(
                "2",
                LocalDateTime.now(),
                new Customer("2", "Nastya", "test2@test.com", LocalDateTime.now(), 31, "Berlin"),
                List.of(new OrderItem("Nike", 3, 170.0, Category.CLOTHING)),
                OrderStatus.CANCELLED
        ));

        sampleOrders.add(new Order("3",
                LocalDateTime.now(),
                new Customer("3", "Gena", "test3@test.com", LocalDateTime.now(), 50, "Monaco"),
                List.of(new OrderItem("Miki Mouse", 1, 30.0, Category.TOYS),
                        new OrderItem("Table", 1, 185.0, Category.HOME),
                        new OrderItem("Fork", 15, 15.0, Category.HOME),
                        new OrderItem("Pents", 1, 70.0, Category.CLOTHING)),
                OrderStatus.DELIVERED
        ));

        sampleOrders.add(new Order("4",
                LocalDateTime.now(),
                new Customer("4", "Denis", "test4@test.com", LocalDateTime.now(), 23, "Minsk"),
                List.of(new OrderItem("Azbooka", 1, 100.0, Category.BOOKS)),
                OrderStatus.SHIPPED
        ));

        sampleOrders.add(new Order("5",
                LocalDateTime.now(),
                new Customer("5", "Vika", "test5@test.com", LocalDateTime.now(), 10, "Brest"),
                List.of(new OrderItem("Kookla", 3, 23.0, Category.TOYS)),
                OrderStatus.PROCESSING
        ));

        sampleOrders.add(new Order("6",
                LocalDateTime.now(),
                new Customer("6", "Tanya", "test6@test.com", LocalDateTime.now(), 36, "Chicago"),
                List.of(new OrderItem("Mint balm", 1, 30.0, Category.BEAUTY),
                        new OrderItem("Shadows", 2, 20.0, Category.BEAUTY)),
                OrderStatus.SHIPPED
        ));

        sampleOrders.add(new Order("7",
                LocalDateTime.now(),
                new Customer("7", "Tomas", "test7@test.com", LocalDateTime.now(), 63, "New York"),
                List.of(new OrderItem("Sweater", 1, 84.0, Category.CLOTHING),
                        new OrderItem("Sneakers", 1, 137.0, Category.CLOTHING),
                        new OrderItem("Lamp", 4, 37.0, Category.HOME),
                        new OrderItem("Plate", 20, 11.0, Category.HOME),
                        new OrderItem("Lego", 4, 75.0, Category.TOYS),
                        new OrderItem("Phone", 1, 430.0, Category.ELECTRONICS)),
                OrderStatus.NEW
        ));

        sampleOrders.add(new Order("8",
                LocalDateTime.now(),
                new Customer("6", "Tanya", "test6@test.com", LocalDateTime.now(), 36, "Chicago"),
                List.of(
                        new OrderItem("Laptop", 1, 1200.0, Category.ELECTRONICS),
                        new OrderItem("Mouse", 2, 25.0, Category.ELECTRONICS),
                        new OrderItem("SSD", 1, 250.0, Category.ELECTRONICS)),
                OrderStatus.DELIVERED
        ));

        sampleOrders.add(new Order("9",
                LocalDateTime.now(),
                new Customer("9", "Mark", "test9@test.com", LocalDateTime.now(), 35, "Paris"),
                List.of(
                        new OrderItem("Book", 3, 20.0, Category.BOOKS)),
                OrderStatus.DELIVERED
        ));

        sampleOrders.add(new Order("10",
                LocalDateTime.now(),
                new Customer("10", "John", "test10@test.com", LocalDateTime.now(), 42, "Berlin"),
                List.of(
                        new OrderItem("T-shirt", 2, 30.0, Category.CLOTHING),
                        new OrderItem("Jeans", 1, 80.0, Category.CLOTHING)),
                OrderStatus.CANCELLED
        ));

        sampleOrders.add(new Order("11",
                LocalDateTime.now(),
                new Customer("11", "Emma", "test11@test.com", LocalDateTime.now(), 31, "London"),
                List.of(
                        new OrderItem("Shampoo", 3, 15.0, Category.BEAUTY),
                        new OrderItem("Cream", 2, 25.0, Category.BEAUTY),
                        new OrderItem("Cream", 1, 45.0, Category.BEAUTY)),
                OrderStatus.DELIVERED
        ));

        sampleOrders.add(new Order("12",
                LocalDateTime.now(),
                new Customer("12", "Lucas", "test12@test.com", LocalDateTime.now(), 27, "Madrid"),
                List.of(
                        new OrderItem("Puzzle", 2, 22.0, Category.TOYS)),
                OrderStatus.PROCESSING
        ));

        sampleOrders.add(new Order("13",
                LocalDateTime.now(),
                new Customer("13", "Sophia", "test13@test.com", LocalDateTime.now(), 22, "Rome"),
                List.of(
                        new OrderItem("Tablet", 1, 600.0, Category.ELECTRONICS),
                        new OrderItem("Headphones", 1, 150.0, Category.ELECTRONICS)),
                OrderStatus.DELIVERED
        ));

        sampleOrders.add(new Order("14",
                LocalDateTime.now(),
                new Customer("14", "Daniel", "test14@test.com", LocalDateTime.now(), 50, "New York"),
                List.of(
                        new OrderItem("Chair", 2, 120.0, Category.HOME),
                        new OrderItem("Table", 1, 300.0, Category.HOME)),
                OrderStatus.DELIVERED
        ));

        sampleOrders.add(new Order("15",
                LocalDateTime.now(),
                new Customer("14", "Daniel", "test14@test.com", LocalDateTime.now(), 50, "New York"),
                List.of(
                        new OrderItem("Sofa", 2, 130.0, Category.HOME),
                        new OrderItem("Case", 1, 100.0, Category.HOME)),
                OrderStatus.DELIVERED
        ));

        sampleOrders.add(new Order("16",
                LocalDateTime.now(),
                new Customer("14", "Daniel", "test14@test.com", LocalDateTime.now(), 50, "New York"),
                List.of(
                        new OrderItem("Sofa", 1, 130.0, Category.HOME),
                        new OrderItem("Case", 4, 100.0, Category.HOME)),
                OrderStatus.DELIVERED
        ));

        sampleOrders.add(new Order("17",
                LocalDateTime.now(),
                new Customer("14", "Daniel", "test14@test.com", LocalDateTime.now(), 50, "New York"),
                List.of(
                        new OrderItem("Sofa", 1, 130.0, Category.HOME),
                        new OrderItem("Case", 4, 100.0, Category.HOME)),
                OrderStatus.DELIVERED
        ));

        sampleOrders.add(new Order("18",
                LocalDateTime.now(),
                new Customer("14", "Daniel", "test14@test.com", LocalDateTime.now(), 50, "New York"),
                List.of(
                        new OrderItem("Sofa", 1, 130.0, Category.HOME),
                        new OrderItem("Case", 4, 100.0, Category.HOME)),
                OrderStatus.DELIVERED
        ));

        sampleOrders.add(new Order("19",
                LocalDateTime.now(),
                new Customer("14", "Daniel", "test14@test.com", LocalDateTime.now(), 50, "New York"),
                List.of(
                        new OrderItem("Sofa", 1, 130.0, Category.HOME),
                        new OrderItem("Case", 4, 100.0, Category.HOME)),
                OrderStatus.DELIVERED
        ));

        sampleOrders.add(new Order("20",
                LocalDateTime.now(),
                new Customer("14", "Daniel", "test14@test.com", LocalDateTime.now(), 50, "New York"),
                List.of(
                        new OrderItem("Sofa", 1, 130.0, Category.HOME),
                        new OrderItem("Case", 4, 100.0, Category.HOME)),
                OrderStatus.DELIVERED
        ));
    }

    @Test
    void shouldReturnCorrectUniqueCities() {
        Set<String> uniqueCities = orderMetrics.getUniqueCities(sampleOrders);

        assertEquals(11, uniqueCities.size());
        assertTrue(uniqueCities.contains("New York"));
        assertTrue(uniqueCities.contains("Minsk"));
    }

    @Test
    void shouldReturnCorrectTotalIncome() {
        double totalIncome = orderMetrics.getTotalIncome(sampleOrders);
        double expected = 6510.0;
        assertEquals(totalIncome, expected);
    }

    @Test
    void shouldReturnMostPopularProductBySales() {
        String mostPopularProductBySales = orderMetrics.getMostPopularProductBySales(sampleOrders);

        assertEquals(mostPopularProductBySales, "Case");
    }

    @Test
    void shouldReturnAverageCheck() {
        double averageCheck = orderMetrics.getAverageCheck(sampleOrders);

        double expectedAverage = 542.5;
        assertEquals(expectedAverage, averageCheck);
    }

    @Test
    void shouldReturnFiveMoreOrders() {
        List<Customer> fiveMoreOrders = orderMetrics.getFiveMoreOrders(sampleOrders);

        assertEquals(1, fiveMoreOrders.size());
        assertEquals("Daniel", fiveMoreOrders.get(0).getName());
    }
}
