package com.radkovich.robotfactorysimulation;

import java.util.concurrent.Phaser;

public class Simulation {
    private static final int DAYS = 100;

    public static void main(String[] args) throws InterruptedException {
        Inventory inventory = new Inventory();
        Phaser phaser = new Phaser(3);
        Factory factory = new Factory(inventory, phaser, DAYS);

        Faction world = new Faction("World", inventory, phaser, DAYS);
        Faction wednesday = new Faction("Wednesday", inventory, phaser, DAYS);

        Thread factoryThread = new Thread(factory, "Factory Thread");
        Thread worldThread = new Thread(world, "World Thread");
        Thread wednesdayThread = new Thread(wednesday, "Wednesday Thread");

        factoryThread.start();
        worldThread.start();
        wednesdayThread.start();

        factoryThread.join();
        worldThread.join();
        wednesdayThread.join();

        System.out.println("Factory remaining parts:");
        for (PartType partType : PartType.values()) {
            System.out.printf("%s: %d%n", partType, inventory.getPartsCount(partType));
        }

        int worldRobots = world.getRobotsBuilt();
        int wednesdayRobots = wednesday.getRobotsBuilt();

        System.out.printf("%nWorld robots built: %d%n", worldRobots);
        System.out.printf("Wednesday robots built: %d%n", wednesdayRobots);

        if (worldRobots > wednesdayRobots) {
            System.out.println("Winner: World");
        } else if (worldRobots < wednesdayRobots) {
            System.out.println("Winner: Wednesday");
        } else {
            System.out.println("Result: Draw");
        }
    }
}
