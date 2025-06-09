package com.gruposilla.back.algorithm.graph;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class DisjointSet<T> {
    private Map<T, T> parent = new HashMap<>();

    public void makeSet(Collection<T> items) {
        for (T item : items) {
            parent.put(item, item);
        }
    }

    public T find(T item) {
        if (parent.get(item) != item) {
            parent.put(item, find(parent.get(item))); // Path compression
        }
        return parent.get(item);
    }

    public void union(T a, T b) {
        T rootA = find(a);
        T rootB = find(b);
        if (!rootA.equals(rootB)) {
            parent.put(rootA, rootB);
        }
    }

    public boolean connected(T a, T b) {
        return find(a).equals(find(b));
    }
}
