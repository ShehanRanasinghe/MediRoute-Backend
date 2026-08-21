package com.mediroute.mediroutebackend.routing.model;

public class DispatchItem {

    private Long id;
    private String name;
    private int value; 
    private int weight; 

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

    public double densityScore() {
        return (double) value / weight;
    }
}
