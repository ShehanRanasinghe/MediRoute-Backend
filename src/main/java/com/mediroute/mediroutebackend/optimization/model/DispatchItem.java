package com.mediroute.mediroutebackend.optimization.model;


// This model represents one medical item that can be loaded into the vehicle under a capacity limit.
// The value and weight are used by the knapsack algorithms to decide which items best improve the dispatch outcome.
public class DispatchItem {

    private Long id;
    private String name;
    private int value;  // urgency - the "value" in the knapsack sense
    private int weight; // size/cost - the "weight" in the knapsack sense

    public DispatchItem() {}

    public DispatchItem(Long id, String name, int value, int weight) {
        this.id = id;
        this.name = name;
        this.value = value;
        this.weight = weight;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public int getValue() { return value; }
    public void setValue(int value) { this.value = value; }
    public int getWeight() { return weight; }
    public void setWeight(int weight) { this.weight = weight; }

    /** value-to-weight ratio, used by GreedyOptimizer and BacktrackingOptimizer's pruning bound. */
    public double densityScore() {
        return (double) value / weight;
    }
}
