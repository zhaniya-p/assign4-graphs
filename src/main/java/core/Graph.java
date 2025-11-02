package core;

import java.util.*;

public class Graph {
    private final int n;
    private final List<List<Edge>> adj;

    public Graph(int n) {
        this.n = n;
        this.adj = new ArrayList<>(n);
        for (int i = 0; i < n; i++) adj.add(new ArrayList<>());
    }

    public int n() { return n; }

    public void addEdge(int u, int v, double w) {
        adj.get(u).add(new Edge(u, v, w));
    }

    public List<Edge> neighbors(int u) {
        return Collections.unmodifiableList(adj.get(u));
    }

    public List<List<Edge>> adjacency() { return adj; }
}
