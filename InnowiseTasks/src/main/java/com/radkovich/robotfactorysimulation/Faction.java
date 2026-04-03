package com.radkovich.robotfactorysimulation;

import java.util.*;
import java.util.concurrent.Phaser;

public class Faction implements Runnable {
    private static final int NIGHT_CAPACITY = 5;
    private final String name;
    private final Inventory inventory;
    private final Phaser phaser;
    private final int nights;
    private int robotsBuilt;
    private final Map<PartType, Integer> factionStorage = new EnumMap<>(PartType.class);
    private final Random random = new Random();

    public Faction(String name, Inventory inventory, Phaser phaser, int nights) {
        this.name = name;
        this.inventory = inventory;
        this.phaser = phaser;
        this.nights = nights;
        for (PartType partType : PartType.values()) {
            factionStorage.put(partType, 0);
        }
    }

    @Override
    public void run() {
        for (int night = 0; night < nights; night++) {
            phaser.arriveAndAwaitAdvance();

            collectParts();
            assembleRobots();

            phaser.arriveAndAwaitAdvance();
        }
    }

    public int getRobotsBuilt() {
        return robotsBuilt;
    }

    public int getMaximumRobotsBuilt(Map<PartType, Integer> parts) {
        Map<PartType, Integer> someStorage = new EnumMap<>(PartType.class);

        for (PartType partType : PartType.values()) {
            someStorage.put(partType, parts.getOrDefault(partType, 0));
        }
        return Math.min(
                Math.min(someStorage.get(PartType.HEAD), someStorage.get(PartType.TORSO)),
                Math.min(someStorage.get(PartType.HAND) / 2, someStorage.get(PartType.FEET) / 2)
        );
    }
    private void collectParts() {
        for (int i = 0; i < NIGHT_CAPACITY; i++) {
            PartType selected = choosePartToTake();
            if (selected == null) {
                break;
            }

            if (inventory.takePart(selected)) {
                factionStorage.put(selected, factionStorage.get(selected) + 1);
            } else {
                i--;
            }
        }
    }

    private PartType choosePartToTake() {
        List<PartType> priorities = getPriorityOrder();
        for (PartType partType : priorities) {
            if (inventory.getPartsCount(partType) > 0) {
                return partType;
            }
        }
        return null;
    }

    private List<PartType> getPriorityOrder() {
        Map<PartType, Integer> missing = new EnumMap<>(PartType.class);
        missing.put(PartType.HEAD, Math.max(0, 1 - factionStorage.get(PartType.HEAD)));
        missing.put(PartType.TORSO, Math.max(0, 1 - factionStorage.get(PartType.TORSO)));
        missing.put(PartType.HAND, Math.max(0, 2 - factionStorage.get(PartType.HAND)));
        missing.put(PartType.FEET, Math.max(0, 2 - factionStorage.get(PartType.FEET)));

        List<PartType> ordered = new ArrayList<>(List.of(PartType.values()));
        Collections.shuffle(ordered, random);
        ordered.sort((left, right) -> Integer.compare(missing.get(right), missing.get(left)));
        return ordered;
    }

    private void assembleRobots() {
        int completeRobots = Math.min(
                Math.min(factionStorage.get(PartType.HEAD), factionStorage.get(PartType.TORSO)),
                Math.min(factionStorage.get(PartType.HAND) / 2, factionStorage.get(PartType.FEET) / 2)
        );

        if (completeRobots == 0) {
            return;
        }

        factionStorage.put(PartType.HEAD, factionStorage.get(PartType.HEAD) - completeRobots);
        factionStorage.put(PartType.TORSO, factionStorage.get(PartType.TORSO) - completeRobots);
        factionStorage.put(PartType.HAND, factionStorage.get(PartType.HAND) - completeRobots * 2);
        factionStorage.put(PartType.FEET, factionStorage.get(PartType.FEET) - completeRobots * 2);
        robotsBuilt += completeRobots;
    }
}
