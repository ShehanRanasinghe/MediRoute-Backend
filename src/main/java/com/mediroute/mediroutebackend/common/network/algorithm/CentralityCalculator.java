package com.mediroute.network.algorithm;

import com.mediroute.network.model.CentralityResult;
import com.mediroute.network.model.NodeCentrality;
import com.mediroute.routing.graph.Graph;
import com.mediroute.routing.model.Node;

import java.util.ArrayList;
import java.util.List;

/**
 * Degree centrality: ranks every node by how many roads connect directly
 * to it. A hospital with a high degree is a well-connected regional hub -
 * useful for referral planning (which hospital should be the primary
 * transfer point for a region).
 *
 * Chosen over betweenness centrality (which measures how often a node
 * lies on the shortest path between other node pairs) because betweenness
 * costs O(V x E) to compute exactly - too expensive to run on-demand for
 * this coursework's scale. Degree centrality is a reasonable, much
 * cheaper approximation of "importance" - see docs/03 for the full
 * justification and docs/09 (Individual Report Ch.9) for betweenness as a
 * suggested future improvement.
 *
 * Time complexity : O(V + E) - one pass counting each node's neighbor list.
 * Space complexity: O(V)
 *
 */
public class CentralityCalculator {

    public CentralityResult calculateDegreeCentrality(Graph graph) {
        long startTime = System.nanoTime();

        List<NodeCentrality> scores = new ArrayList<>();
        for (Node node : graph.getAllNodes()) {
            int degree = graph.getNeighbors(node.getId()).size();
            scores.add(new NodeCentrality(node.getId(), node.getName(), degree));
        }

        scores.sort((a, b) -> Integer.compare(b.getDegreeScore(), a.getDegreeScore()));

        long endTime = System.nanoTime();

        CentralityResult result = new CentralityResult();
        result.setRankedNodes(scores);
        result.setExecutionTimeNanos(endTime - startTime);
        result.setAlgorithmUsed("Degree Centrality");
        return result;
    }
}
