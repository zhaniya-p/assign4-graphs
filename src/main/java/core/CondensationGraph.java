package core;

import java.util.*;


public class CondensationGraph {
    private final Graph compGraph;
    private final int[] vertexToComp; // vertex -> component id
    private final int comps;

    public CondensationGraph(Graph g, List<List<Integer>> sccs) {
        this.comps = sccs.size();
        this.vertexToComp = new int[g.n()];
        for (int i = 0; i < sccs.size(); i++) {
            for (int v : sccs.get(i)) vertexToComp[v] = i;
        }

        // We'll accumulate edges and pick minimum weight for parallel component edges
        Map<Long, Double> compEdgeMinWeight = new HashMap<>();
        for (int u = 0; u < g.n(); u++) {
            for (var e : g.adjacency().get(u)) {
                int v = e.to();
                int cu = vertexToComp[u], cv = vertexToComp[v];
                if (cu != cv) {
                    long key = (((long) cu) << 32) | (cv & 0xffffffffL);
                    compEdgeMinWeight.merge(key, e.weight(), Double::min);
                }
            }
        }

        Graph cg = new Graph(comps);
        for (var entry : compEdgeMinWeight.entrySet()) {
            long key = entry.getKey();
            int cu = (int) (key >> 32);
            int cv = (int) (key & 0xffffffffL);
            double w = entry.getValue();
            cg.addEdge(cu, cv, w);
        }
        this.compGraph = cg;
    }

    public Graph compGraph() { return compGraph; }
    public int[] vertexToComp() { return vertexToComp; }
    public int components() { return comps; }
}
