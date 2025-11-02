package graph.topo;

import core.Graph;
import util.Metrics;

import java.util.*;

public class KahnTopoSort {
    private final Graph g;
    private final Metrics metrics;

    public KahnTopoSort(Graph g, Metrics metrics) {
        this.g = g;
        this.metrics = metrics;
    }

    public List<Integer> topoSort() {
        int n = g.n();
        int[] indeg = new int[n];
        for (int u = 0; u < n; u++) {
            for (var e : g.neighbors(u)) {
                indeg[e.to()]++;
                metrics.incEdgesExamined();
            }
        }
        Deque<Integer> q = new ArrayDeque<>();
        for (int i = 0; i < n; i++) if (indeg[i] == 0) q.add(i);

        List<Integer> order = new ArrayList<>();
        while (!q.isEmpty()) {
            int u = q.remove();
            metrics.incQueueOps();
            order.add(u);
            for (var e : g.neighbors(u)) {
                int v = e.to();
                indeg[v]--;
                if (indeg[v] == 0) q.add(v);
            }
        }

        if (order.size() != n) {
            // cycle in graph
            return Collections.emptyList();
        }
        return order;
    }
}
