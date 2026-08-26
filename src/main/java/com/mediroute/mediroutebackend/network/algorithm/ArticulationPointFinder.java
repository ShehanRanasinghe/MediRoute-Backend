package com.mediroute.mediroutebackend.network.algorithm;

import com.mediroute.mediroutebackend.network.model.CriticalNodeResult;
import com.mediroute.mediroutebackend.routing.graph.Graph;
import com.mediroute.mediroutebackend.routing.model.Edge;

import java.util.*;

/**
 * Finds articulation points (critical nodes) - junctions/hospitals whose
 * removal would disconnect part of the network. Based on Tarjan's
 * algorithm (discovery time + "low-link" value per node).
 *
 * WHY AN EXPLICIT STACK (iterative DFS, not recursive):
 * A naive recursive DFS would call itself once per node depth-first, which
 * risks a StackOverflowError on a large, deeply-connected city road graph
 * (Java's call stack is limited, typically a few thousand frames). Using
 * an explicit Deque as a manual stack lets DFS go arbitrarily deep,
 * bounded only by heap memory instead of call-stack depth.
 *
 * Core idea per node u:
 *   - discovery[u]  = the order u was first visited
 *   - low[u]        = the earliest discovery time reachable from u's
 *                      subtree (including via one "back edge" upward)
 *   - u (non-root) is an articulation point if some child c has
 *     low[c] >= discovery[u] - meaning c's subtree cannot reach back above u.
 *   - the root is an articulation point only if it has more than one
 *     DFS child (i.e. removing it splits the graph into 2+ pieces).
 *
 * Time complexity : O(V + E) - each node and edge visited once.
 * Space complexity: O(V) for the discovery/low/parent maps and the stack.
 */
public class ArticulationPointFinder {

    public CriticalNodeResult findArticulationPoints(Graph graph) {
        long startTime = System.nanoTime();

        Map<Long, Integer> discovery = new HashMap<>();
        Map<Long, Integer> low = new HashMap<>();
        Map<Long, Long> parent = new HashMap<>();
        Map<Long, Integer> childCount = new HashMap<>();
        Set<Long> visited = new HashSet<>();
        Set<Long> articulationPoints = new LinkedHashSet<>();
        int[] timer = {0};

        // Handles disconnected graphs by starting a fresh DFS from any
        // unvisited node, until every node has been visited at least once.
        for (var node : graph.getAllNodes()) {
            if (!visited.contains(node.getId())) {
                iterativeDfs(node.getId(), graph, discovery, low, parent, childCount, visited, articulationPoints, timer);
            }
        }

        long endTime = System.nanoTime();

        CriticalNodeResult result = new CriticalNodeResult();
        result.setCriticalNodeIds(new ArrayList<>(articulationPoints));
        result.setExecutionTimeNanos(endTime - startTime);
        result.setAlgorithmUsed("DFS Articulation Points (iterative, Tarjan's)");
        return result;
    }

    /** One "frame" of the manually-managed DFS call stack. */
    private static class Frame {
        final Long nodeId;
        final Iterator<Edge> neighbors;

        Frame(Long nodeId, Iterator<Edge> neighbors) {
            this.nodeId = nodeId;
            this.neighbors = neighbors;
        }
    }

    private void iterativeDfs(Long start, Graph graph,
                               Map<Long, Integer> discovery, Map<Long, Integer> low,
                               Map<Long, Long> parent, Map<Long, Integer> childCount,
                               Set<Long> visited, Set<Long> articulationPoints, int[] timer) {

        Deque<Frame> stack = new ArrayDeque<>();

        visited.add(start);
        discovery.put(start, timer[0]);
        low.put(start, timer[0]);
        timer[0]++;
        parent.put(start, null); // null parent marks the DFS root
        childCount.put(start, 0);
        stack.push(new Frame(start, graph.getNeighbors(start).iterator()));

        while (!stack.isEmpty()) {
            Frame frame = stack.peek();
            Long u = frame.nodeId;

            if (frame.neighbors.hasNext()) {
                Long v = frame.neighbors.next().getTargetNodeId();

                if (Objects.equals(v, parent.get(u))) {
                    continue; // don't walk straight back along the edge we arrived on
                }

                if (!visited.contains(v)) {
                    // Tree edge - descend into v
                    visited.add(v);
                    discovery.put(v, timer[0]);
                    low.put(v, timer[0]);
                    timer[0]++;
                    parent.put(v, u);
                    childCount.put(v, 0);
                    childCount.put(u, childCount.get(u) + 1);
                    stack.push(new Frame(v, graph.getNeighbors(v).iterator()));
                } else {
                    // Back edge - v was already visited via a different path;
                    // this means u can "reach back" as far as v's discovery time.
                    low.put(u, Math.min(low.get(u), discovery.get(v)));
                }
            } else {
                // Done exploring all of u's neighbors - pop it and update its parent
                stack.pop();
                Long p = parent.get(u);

                if (p != null) {
                    low.put(p, Math.min(low.get(p), low.get(u)));

                    boolean pIsRoot = parent.get(p) == null;
                    if (!pIsRoot && low.get(u) >= discovery.get(p)) {
                        // u's subtree cannot reach above p - removing p disconnects u's side
                        articulationPoints.add(p);
                    }
                    if (pIsRoot && childCount.get(p) > 1) {
                        // Root with 2+ independent DFS subtrees - removing it splits the graph
                        articulationPoints.add(p);
                    }
                }
            }
        }
    }
}
