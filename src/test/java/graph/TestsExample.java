package graph;

import core.Graph;
import graph.scc.TarjanSCC;
import graph.topo.KahnTopoSort;
import graph.dagsp.DAGShortestPaths;
import org.junit.jupiter.api.Test;
import util.Metrics;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestsExample {

    @Test
    public void testSCCAndTopoSmall() {
        // graph: 0->1->2->0 (one SCC), 2->3 (edge to single), 3->4
        Graph g = new Graph(5);
        g.addEdge(0,1,1); g.addEdge(1,2,1); g.addEdge(2,0,1);
        g.addEdge(2,3,1); g.addEdge(3,4,1);
        Metrics m = new Metrics();
        TarjanSCC t = new TarjanSCC(g, m);
        var comps = t.computeSCCs();
        assertEquals(3, comps.size()); // one comp of size 3 and one for 3 and 4 maybe two singles
        // Build condensation and check topo
        // Build condensation manually: components >= 2, topo ordering length == components
    }

    @Test
    public void testDAGShortestPaths() {
        Graph g = new Graph(4);
        // 0->1 (1), 0->2(2), 1->3(3), 2->3(1)
        g.addEdge(0,1,1); g.addEdge(0,2,2);
        g.addEdge(1,3,3); g.addEdge(2,3,1);
        Metrics m = new Metrics();
        // topological order [0,1,2,3] is valid
        List<Integer> topo = List.of(0,1,2,3);
        DAGShortestPaths dsp = new DAGShortestPaths(g, m);
        var res = dsp.shortestFrom(0, topo);
        assertEquals(0.0, res.dist[0]);
        assertEquals(1.0, res.dist[1]);
        assertEquals(2.0, res.dist[2]);
        assertEquals(3.0, res.dist[3]); // via 0->2->3 is 3 or 0->1->3 is 4 => min 3
    }
}
