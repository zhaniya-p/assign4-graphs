package core;

import java.util.*;

/**
 * Builds condensation (component) graph from original graph and component mapping.
 */
public class CondensationGraph {
    private final Graph compGraph;
    private final int[] vertexToComp; // vertex -> component id
    private final int comps;

    /**
     * Build condensation DAG.
     * @param g original graph
     * @param sccs list of components (each a list of vertices)
     */
    public CondensationGraph(Graph g, List<List<Integer>> sccs) {
        this.comps = sccs.size();
        this.vertexToComp = new int[g.n()];
        for (int i = 0; i < sccs.size(); i++) {
            for (int v : sccs.get(i)) vertexToComp[v] = i;
        }
        Graph cg = new Graph(comps);
        var added = new HashSet<Long>();
        for (int u = 0; u < g.n(); u++) {
            for (var e : g.neighbors(u)) {
                int v = e.to();
                int cu = vertexToComp[u], cv = vertexToComp[v];
                if (cu != cv) {
                    long key = ((long)cu << 32) | (cv & 0xffffffffL);
                    if (!added.contains(key)) {
                        // For condensation weights we take min of edges if needed; here we store weight 1.0 by default
                        cg.addEdge(cu, cv, e.weight());
                        added.add(key);
                    }
                }
            }
        }
        this.compGraph = cg;
    }

    public Graph compGraph() { return compGraph; }
    public int[] vertexToComp() { return vertexToComp; }
    public int components() { return comps; }
}
