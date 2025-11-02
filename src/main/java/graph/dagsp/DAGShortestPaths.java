package graph.dagsp;

import core.Edge;
import core.Graph;
import util.Metrics;

import java.util.*;


public class DAGShortestPaths {
    private final Graph g;
    private final Metrics metrics;

    public DAGShortestPaths(Graph g, Metrics metrics) {
        this.g = g;
        this.metrics = metrics;
    }

    public Result shortestFrom(int source, List<Integer> topoOrder) {
        int n = g.n();
        double[] dist = new double[n];
        int[] pred = new int[n];
        Arrays.fill(dist, Double.POSITIVE_INFINITY);
        Arrays.fill(pred, -1);
        dist[source] = 0.0;

        // process vertices in topo order
        for (int v : topoOrder) {
            for (Edge e : g.adjacency().get(v)) {
                metrics.incRelaxations();
                int to = e.to();
                if (dist[v] != Double.POSITIVE_INFINITY) {
                    double nd = dist[v] + e.weight();
                    if (nd < dist[to]) {
                        dist[to] = nd;
                        pred[to] = v;
                    }
                }
            }
        }
        return new Result(dist, pred);
    }

    public Result longestFrom(int source, List<Integer> topoOrder) {
        int n = g.n();
        double[] dist = new double[n];
        int[] pred = new int[n];
        Arrays.fill(dist, Double.NEGATIVE_INFINITY);
        Arrays.fill(pred, -1);
        dist[source] = 0.0;

        for (int v : topoOrder) {
            for (Edge e : g.adjacency().get(v)) {
                metrics.incRelaxations();
                int to = e.to();
                if (dist[v] != Double.NEGATIVE_INFINITY) {
                    double nd = dist[v] + e.weight();
                    if (nd > dist[to]) {
                        dist[to] = nd;
                        pred[to] = v;
                    }
                }
            }
        }
        return new Result(dist, pred);
    }

    public static class Result {
        public final double[] dist;
        public final int[] pred;
        public Result(double[] dist, int[] pred) { this.dist = dist; this.pred = pred; }

        public List<Integer> reconstruct(int target) {
            if (dist[target] == Double.POSITIVE_INFINITY || dist[target] == Double.NEGATIVE_INFINITY) {
                return Collections.emptyList();
            }
            LinkedList<Integer> path = new LinkedList<>();
            int cur = target;
            while (cur != -1) {
                path.addFirst(cur);
                cur = pred[cur];
            }
            return path;
        }
    }
}
