package graph.scc;

import core.Graph;
import util.Metrics;

import java.util.*;

public class TarjanSCC {
    private final Graph g;
    private final Metrics metrics;

    public TarjanSCC(Graph g, Metrics metrics) {
        this.g = g;
        this.metrics = metrics;
    }

    public List<List<Integer>> computeSCCs() {
        int n = g.n();
        int[] idx = new int[n];
        Arrays.fill(idx, -1);
        int[] low = new int[n];
        boolean[] onStack = new boolean[n];
        Deque<Integer> stack = new ArrayDeque<>();
        List<List<Integer>> sccs = new ArrayList<>();
        int[] time = {0};

        for (int v = 0; v < n; v++) {
            if (idx[v] == -1) {
                dfs(v, idx, low, onStack, stack, sccs, time);
            }
        }
        return sccs;
    }

    private void dfs(int v, int[] idx, int[] low, boolean[] onStack,
                     Deque<Integer> stack, List<List<Integer>> sccs, int[] time) {
        idx[v] = low[v] = time[0]++;
        metrics.incNodeVisits();
        stack.push(v); onStack[v] = true;

        for (var e : g.neighbors(v)) {
            metrics.incEdgesExamined();
            int to = e.to();
            if (idx[to] == -1) {
                dfs(to, idx, low, onStack, stack, sccs, time);
                low[v] = Math.min(low[v], low[to]);
            } else if (onStack[to]) {
                low[v] = Math.min(low[v], idx[to]);
            }
        }

        if (low[v] == idx[v]) { // root of SCC
            List<Integer> comp = new ArrayList<>();
            while (true) {
                int u = stack.pop();
                onStack[u] = false;
                comp.add(u);
                if (u == v) break;
            }
            sccs.add(comp);
        }
    }
}
