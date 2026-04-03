package com.radkovich.robotfactorysimulation;

import org.junit.jupiter.api.Test;

import java.util.EnumMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

class SimulationTest {

    @Test
    void shouldCreateEmptyInventory() {
        Inventory inventory = new Inventory();

        assertEquals(0, inventory.getPartsCount(PartType.HEAD));
        assertEquals(0, inventory.getPartsCount(PartType.TORSO));
        assertEquals(0, inventory.getPartsCount(PartType.HAND));
        assertEquals(0, inventory.getPartsCount(PartType.FEET));
    }

    @Test
    void shouldAddPartsToInventory() {
        Inventory inventory = new Inventory();

        inventory.addPart(PartType.HAND);
        inventory.addPart(PartType.HAND);
        inventory.addPart(PartType.HAND);
        inventory.addPart(PartType.TORSO);

        assertEquals(3, inventory.getPartsCount(PartType.HAND));
        assertEquals(1, inventory.getPartsCount(PartType.TORSO));
    }

    @Test
    void shouldTakePartsFromInventory() {
        Inventory inventory = new Inventory();

        inventory.addPart(PartType.HAND);
        inventory.addPart(PartType.HAND);
        inventory.addPart(PartType.HAND);
        inventory.addPart(PartType.TORSO);

        assertEquals(3, inventory.getPartsCount(PartType.HAND));
        assertTrue(inventory.takePart(PartType.HAND));
        assertFalse(inventory.takePart(PartType.FEET));
        assertEquals(2, inventory.getPartsCount(PartType.HAND));
        assertEquals(1, inventory.getPartsCount(PartType.TORSO));
    }

    @Test
    void shouldGetMaxRobotsCount() {
        Faction faction = new Faction("test", null, null, 1);
        Map<PartType, Integer> parts = new EnumMap<>(PartType.class);

        parts.put(PartType.HEAD, 1);
        parts.put(PartType.TORSO, 2);
        parts.put(PartType.HAND, 2);
        parts.put(PartType.FEET, 2);

        assertEquals(1, faction.getMaximumRobotsBuilt(parts));
    }

    @Test
    void shouldOnlyOneThreadRemoveLastPart() throws InterruptedException {
        Inventory inventory = new Inventory();
        inventory.addPart(PartType.TORSO);

        CountDownLatch readyFlag = new CountDownLatch(2);
        CountDownLatch start = new CountDownLatch(1);
        AtomicInteger counter = new AtomicInteger();

        Runnable task = () -> {
            readyFlag.countDown();
            try {
                start.await();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException(e);
            }
            if (inventory.takePart(PartType.TORSO)) {
                counter.incrementAndGet();
            }
        };

        Thread t1 = new Thread(task, "T1");
        Thread t2 = new Thread(task, "T2");

        t1.start();
        t2.start();

        readyFlag.await();
        start.countDown();

        t1.join();
        t2.join();

        assertEquals(1, counter.get());
        assertEquals(0, inventory.getPartsCount(PartType.TORSO));
    }
}
