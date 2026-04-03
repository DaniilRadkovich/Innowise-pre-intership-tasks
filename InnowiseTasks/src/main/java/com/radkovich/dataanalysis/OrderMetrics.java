package com.radkovich.dataanalysis;

import java.util.*;
import java.util.stream.Collectors;

public class OrderMetrics {

    Set<String> getUniqueCities(List<Order> sampleOrders) {
        return sampleOrders.stream()
                .map(o -> o.getCustomer().getCity())
                .collect(Collectors.toSet());
    }

    double getTotalIncome(List<Order> sampleOrders) {
        return sampleOrders.stream()
                .filter(o -> o.getStatus().equals(OrderStatus.DELIVERED))
                .flatMap(o -> o.getItems().stream())
                .mapToDouble(OrderItem::getTotalPrice)
                .sum();
    }

    String getMostPopularProductBySales(List<Order> sampleOrders) {
        return sampleOrders.stream()
                .flatMap(o -> o.getItems().stream())
                .collect(Collectors.groupingBy(
                        OrderItem::getProductName,
                        Collectors.summingInt(OrderItem::getQuantity)
                ))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .orElseThrow()
                .getKey();
    }


    double getAverageCheck(List<Order> sampleOrders) {
        return sampleOrders.stream()
                .filter(o -> o.getStatus().equals(OrderStatus.DELIVERED))
                .collect(Collectors.averagingDouble(
                        o -> o.getItems().stream()
                                .mapToDouble(OrderItem::getTotalPrice)
                                .sum())
                );
    }

    List<Customer> getFiveMoreOrders(List<Order> sampleOrders) {
        return sampleOrders.stream()
                .collect(Collectors.groupingBy(Order::getCustomer, Collectors.counting()))
                .entrySet().stream()
                .filter(o -> o.getValue() > 5)
                .map(Map.Entry::getKey)
                .toList();
    }
}

