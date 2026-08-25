package com.mediroute.network.algorithm;

import com.mediroute.network.model.MSTEdgeDTO;
import com.mediroute.network.model.MSTResult;
import com.mediroute.routing.graph.Graph;
import com.mediroute.routing.model.Edge;
import com.mediroute.routing.model.Node;

import java.util.*;

/**
 * Prim's algorithm - builds the Minimum Spanning Tree (MST): the cheapest
 * possible set of roads that still connects every node in the network,
 * with no cycles. Represents the "essential backbone" of the referral
 * network - if you could only afford to maintain a subset of roads, this
 * is the subset that keeps everything reachable at minimum total cost.
 *
 * WHY A MIN-HEAP WITH "LAZY DELETION":
 * Every time a new node joins the tree, its edges to outside nodes are
 * pushed onto the heap. Some of those edges may later become "stale" (both
 * endpoints end up inside the tree) - rather than removing them from the
 * heap immediately (expensive), we just check-and-skip stale entries when
 * they're popped. This is simpler to implement correctly than a
 * heap with decrease-key support, at a small, acceptable extra memory cost.
 *
 * Time complexity : O(E log E) with lazy deletion (each edge may be
 *                    pushed once, each pop is O(log E)).
 * Space complexity: O(V + E)
 *
 */
public class MSTBuilder {

    public MSTResult buildMST(Graph graph) {
        long startTime = System.nanoTime();

        Collection<Node> allNodes = graph.getAllNodes();
        if (allNodes.isEmpty()) {
            return emptyResult(startTime);
        }

        Set<Long> inTree = new HashSet<>();
        List<MSTEdgeDTO> mstEdges = new ArrayList<>();
        double totalWeight = 0;

        Long startNodeId = allNodes.iterator().next().getId();
        PriorityQueue<CandidateEdge> minHeap = new PriorityQueue<>(Comparator.comparingDouble(e -> e.weight));

        inTree.add(startNodeId);
        addFrontierEdges(graph, startNodeId, inTree, minHeap);

        while (!minHeap.isEmpty() && inTree.size() < graph.nodeCount()) {
            CandidateEdge candidate = minHeap.poll();
            if (inTree.contains(candidate.toNodeId)) {
                continue; // stale entry - both endpoints already connected, skip it
            }

            inTree.add(candidate.toNodeId);
            mstEdges.add(new MSTEdgeDTO(candidate.fromNodeId, candidate.toNodeId, candidate.weight));
            totalWeight += candidate.weight;

            addFrontierEdges(graph, candidate.toNodeId, inTree, minHeap);
        }

        long endTime = System.nanoTime();

        MSTResult result = new MSTResult();
        result.setEdges(mstEdges);
        result.setTotalWeightKm(totalWeight);
        result.setExecutionTimeNanos(endTime - startTime);
        result.setAlgorithmUsed("Prim's MST");
        result.setConnected(inTree.size() == graph.nodeCount());
        return result;
    }

    private void addFrontierEdges(Graph graph, Long nodeId, Set<Long> inTree, PriorityQueue<CandidateEdge> minHeap) {
        for (Edge edge : graph.getNeighbors(nodeId)) {
            if (!inTree.contains(edge.getTargetNodeId())) {
                minHeap.add(new CandidateEdge(nodeId, edge.getTargetNodeId(), edge.getDistanceKm()));
            }
        }
    }

    private MSTResult emptyResult(long startTime) {
        MSTResult result = new MSTResult();
        result.setEdges(new ArrayList<>());
        result.setTotalWeightKm(0);
        result.setExecutionTimeNanos(System.nanoTime() - startTime);
        result.setAlgorithmUsed("Prim's MST");
        result.setConnected(true);
        return result;
    }

    private static class CandidateEdge {
        final Long fromNodeId;
        final Long toNodeId;
        final double weight;

        CandidateEdge(Long fromNodeId, Long toNodeId, double weight) {
            this.fromNodeId = fromNodeId;
            this.toNodeId = toNodeId;
            this.weight = weight;
        }
    }
}
