package com.radkovich.robotfactorysimulation;

import java.util.Random;
import java.util.concurrent.Phaser;

public class Factory implements Runnable {
    private final Inventory inventory;
    private final Random random = new Random();
    private final Phaser phaser;
    private final int days;

    public Factory(Inventory inventory, Phaser phaser, int days) {
        this.inventory = inventory;
        this.phaser = phaser;
        this.days = days;
    }

    @Override
    public void run() {
        for (int day = 0; day < days; day++) {
            int quantityPartsToday = random.nextInt(10) + 1;

            for (int i = 0; i < quantityPartsToday; i++) {
                PartType part = PartType.values()[random.nextInt(PartType.values().length)];
                inventory.addPart(part);
            }
            phaser.arriveAndAwaitAdvance();
            phaser.arriveAndAwaitAdvance();
        }
    }
}
