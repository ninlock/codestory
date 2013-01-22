package com.codestory;

import java.util.HashMap;
import java.util.Map;

public enum Cent {           
    FOO ("foo", 1, 1),
    BAR ("bar", 7, 2),
    QIX ("qix", 11, 3),
    BAZ ("baz", 21, 4);

    private final String name; 
    private final int value; 
    private final int order;
    
    private static final Map<Integer, Cent> lookup = new HashMap<Integer, Cent>();
    static {
        for (Cent d : Cent.values())
            lookup.put(d.order, d);
    }
    
    Cent (String name, int value, int order){
        this.name = name;
        this.value = value;
        this.order = order;
    }

    public int getMultipleCent(int valueToChange){
        return valueToChange/value;
    }
    
    public int getRestCent(int valueToChange){
        return valueToChange % value;
    }
    
    public String getName(){
        return this.name;
    }
    
    public int getValue(){
        return this.value;
    }
    
    public int getOrder(){
        return this.order;
    }
    
    public Cent getCentByOrder(int order){
        return lookup.get(order);
    }    
}