package com.radkovich.robotfactorysimulation;

import java.util.EnumMap;
import java.util.Map;

public class Inventory {
    private final Map<PartType, Integer> parts = new EnumMap<>(PartType.class);

    public Inventory() {
        for (PartType partType : PartType.values()) {
            parts.put(partType, 0);
        }
    }

    public synchronized void addPart(PartType partType) {
        parts.put(partType, parts.get(partType) + 1);
    }

    public synchronized boolean takePart(PartType partType) {
        int count = parts.get(partType);
        if (count == 0) {
            return false;
        } else {
            parts.put(partType, count - 1);
            return true;
        }
    }

    public synchronized int getPartsCount(PartType partType) {
        return parts.get(partType);
    }
}
