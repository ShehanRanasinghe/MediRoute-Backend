package com.mediroute.mediroutebackend.incident.model;

import java.util.List;

/**
 * UPDATED from the original version: added `note`, used to explain WHY
 * there's no dispatch plan (e.g. no ambulance was assigned to this
 * incident, so recommending a supply loadout doesn't make sense) instead
 * of silently returning an empty/misleading result.
 */
public class DispatchPlanView {

    private List<String> selectedItemNames;
    private int totalValueAchieved;
    private int capacityUsed;
    private int totalCapacity;
    private String algorithmUsed;
    private String note; // non-null only when no real plan was computed

    public DispatchPlanView() {}

    public List<String> getSelectedItemNames() { return selectedItemNames; }
    public void setSelectedItemNames(List<String> selectedItemNames) { this.selectedItemNames = selectedItemNames; }
    public int getTotalValueAchieved() { return totalValueAchieved; }
    public void setTotalValueAchieved(int totalValueAchieved) { this.totalValueAchieved = totalValueAchieved; }
    public int getCapacityUsed() { return capacityUsed; }
    public void setCapacityUsed(int capacityUsed) { this.capacityUsed = capacityUsed; }
    public int getTotalCapacity() { return totalCapacity; }
    public void setTotalCapacity(int totalCapacity) { this.totalCapacity = totalCapacity; }
    public String getAlgorithmUsed() { return algorithmUsed; }
    public void setAlgorithmUsed(String algorithmUsed) { this.algorithmUsed = algorithmUsed; }
    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
}
