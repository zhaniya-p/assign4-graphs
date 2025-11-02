import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import core.CondensationGraph;
import core.Graph;
import graph.dagsp.DAGShortestPaths;
import graph.scc.TarjanSCC;
import graph.topo.KahnTopoSort;
import util.DataGenerator;
import util.Metrics;

import java.io.File;
import java.util.*;

public class Main {
    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            System.out.println("No input specified - generating example datasets...");
            new DataGenerator().generateAll();
            return;
        }

        String path = args[0];
        ObjectMapper om = new ObjectMapper();
        JsonNode root = om.readTree(new File(path));
        int n = root.get("n").asInt();
        Graph g = new Graph(n);
        for (JsonNode e : root.withArray("edges")) {
            int u = e.get("from").asInt();
            int v = e.get("to").asInt();
            double w = e.get("w").asDouble();
            g.addEdge(u, v, w);
        }

        System.out.println("Graph loaded: n=" + n + " edges=" + root.withArray("edges").size());

        Metrics sccMetrics = new Metrics();
        TarjanSCC scc = new TarjanSCC(g, sccMetrics);
        sccMetrics.startTimer();
        List<List<Integer>> comps = scc.computeSCCs();
        sccMetrics.stopTimer();

        System.out.println("\n=== SCCs ===");
        for (int i = 0; i < comps.size(); i++) {
            System.out.printf("C%d (size=%d): %s%n", i, comps.get(i).size(), comps.get(i));
        }
        System.out.println("SCC metrics: " + sccMetrics);

        // Build condensation DAG
        CondensationGraph cg = new CondensationGraph(g, comps);
        Graph compGraph = cg.compGraph();
        System.out.printf("%nCondensation graph: components=%d edges=%d%n", compGraph.n(), countEdges(compGraph));

        // Topo sort
        Metrics topoMetrics = new Metrics();
        KahnTopoSort topo = new KahnTopoSort(compGraph, topoMetrics);
        topoMetrics.startTimer();
        List<Integer> compOrder = topo.topoSort();
        topoMetrics.stopTimer();

        System.out.println("\nCondensation DAG topo order: " + compOrder);
        System.out.println("Topo metrics: " + topoMetrics);

        // Derived order of original tasks after SCC compression
        List<Integer> tasksOrder = new ArrayList<>();
        for (int c : compOrder) tasksOrder.addAll(comps.get(c));
        System.out.println("\nDerived order of original tasks after SCC compression: " + tasksOrder);

        // DAG SP (single-source) - choose source = component of vertex 0 (if exists)
        int sourceVertex = 0;
        int sourceComp = cg.vertexToComp()[sourceVertex];
        Metrics spMetrics = new Metrics();
        DAGShortestPaths dsp = new DAGShortestPaths(compGraph, spMetrics);
        spMetrics.startTimer();
        var shortest = dsp.shortestFrom(sourceComp, compOrder);
        spMetrics.stopTimer();
        System.out.println("\nShortest distances from comp " + sourceComp + ": " + Arrays.toString(shortest.dist));
        int sampleTarget = compGraph.n() - 1;
        System.out.println("Example shortest path to " + sampleTarget + ": " + shortest.reconstruct(sampleTarget));
        System.out.println("SP metrics: " + spMetrics);

        // Longest path (critical path)
        Metrics lpMetrics = new Metrics();
        DAGShortestPaths dsp2 = new DAGShortestPaths(compGraph, lpMetrics);
        lpMetrics.startTimer();
        var longest = dsp2.longestFrom(sourceComp, compOrder);
        lpMetrics.stopTimer();

        double best = Double.NEGATIVE_INFINITY;
        int bestT = -1;
        for (int i = 0; i < longest.dist.length; i++) {
            if (longest.dist[i] > best) { best = longest.dist[i]; bestT = i; }
        }
        if (bestT >= 0 && best != Double.NEGATIVE_INFINITY) {
            System.out.println("\nCritical (longest) path length from comp " + sourceComp + " = " + best + " to comp " + bestT);
            System.out.println("Critical path: " + longest.reconstruct(bestT));
        } else {
            System.out.println("\nNo reachable longest path from comp " + sourceComp);
        }
        System.out.println("LP metrics: " + lpMetrics);
    }

    private static int countEdges(Graph g) {
        int cnt = 0;
        for (int u = 0; u < g.n(); u++) cnt += g.adjacency().get(u).size();
        return cnt;
    }
}
