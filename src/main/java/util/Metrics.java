package util;

public class Metrics {
    private long startTime = 0;
    private long elapsedNanos = 0;

    // counters
    private long dfsNodeVisits = 0;
    private long edgesExamined = 0;
    private long queueOps = 0;    // pushes/pops for Kahn
    private long relaxations = 0; // DAG SP relaxations

    public void startTimer() { startTime = System.nanoTime(); }
    public void stopTimer() { elapsedNanos = System.nanoTime() - startTime; }

    public void incNodeVisits() { dfsNodeVisits++; }
    public void incEdgesExamined() { edgesExamined++; }
    public void incQueueOps() { queueOps++; }
    public void incRelaxations() { relaxations++; }

    public long getElapsedNanos() { return elapsedNanos; }
    public long getDfsNodeVisits() { return dfsNodeVisits; }
    public long getEdgesExamined() { return edgesExamined; }
    public long getQueueOps() { return queueOps; }
    public long getRelaxations() { return relaxations; }

    @Override
    public String toString() {
        return String.format("time(ns)=%d dfsVisits=%d edgesExamined=%d queueOps=%d relax=%d",
                elapsedNanos, dfsNodeVisits, edgesExamined, queueOps, relaxations);
    }
}
