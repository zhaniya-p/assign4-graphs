## Report: Graph Algorithms Implementation

This project implements core graph algorithms as part of assignment 4. The main goal was to develop a working solution for Directed Acyclic Graph shortest path, Tarjan Strongly Connected Components, and Topological Sorting. The project also includes condensation graph generation and performance metrics.

## Project Overview

The project consists of several graph processing modules. Each module performs a specific operation on a directed graph, including traversal, component identification, sorting of nodes based on dependencies, and path computation. The modules interact to allow complex graph analysis such as detecting strongly connected components and generating a condensed graph.

The project is written in Java using Maven for dependency and project structure management. The source code is organized into logical packages reflecting each algorithm.

## Implemented Algorithms

### Topological Sorting (Kahn Algorithm)

This module produces a valid ordering of vertices in a directed acyclic graph. It works by removing nodes with zero incoming edges and repeating this process until all vertices are processed. If at any moment no such vertices exist, the graph contains a cycle.

### Strongly Connected Components (Tarjan Algorithm)

Tarjan's algorithm is used to find strongly connected components in a directed graph using depth first search. It keeps track of low link values to determine when a completed component has been found. The output partitions the graph into sets of nodes where each is reachable from the others.

### Condensation Graph

Based on the result of strongly connected components, the program builds a condensation graph. In the condensation graph, each strongly connected component becomes a single node and edges represent connectivity between these groups.

### Shortest Path for Directed Acyclic Graph

The implementation computes the shortest path in a graph that does not contain cycles. The process begins with topological ordering, and then the path values are updated by relaxing edges according to the sorted sequence.

## Project Structure

The codebase is structured into packages, separating implementation logic clearly. Each major feature has its own package so that code is easier to navigate and maintain.

* core: basic graph structure and edge representation
* graph.scc: strongly connected components detection
* graph.topo: topological sorting
* graph.dagsp: shortest path in a DAG
* util: helper functions and metrics, including data generation for testing

## Development Notes

The project was developed on a dedicated branch. After implementing the features, the code was committed and pushed to the remote repository. Once all features were confirmed to be working, the branch was merged into the main repository branch.

## How to Run

The project uses Maven. To compile and run from the command line:

mvn clean package

After compilation, execute the program from the target directory or through an IDE such as IntelliJ IDEA.

## Conclusion

This project demonstrates an applied understanding of several graph algorithms. The implementation shows how strongly connected components relate to condensation graphs and how topological sorting supports pathfinding in directed acyclic graphs. The modular structure allows each algorithm to function independently, while still being part of a cohesive graph processing system.
