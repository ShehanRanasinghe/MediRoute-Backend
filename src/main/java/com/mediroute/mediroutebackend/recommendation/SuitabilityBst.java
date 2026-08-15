// WHAT: Binary search tree that stores scored hospitals ordered by suitability score.

// WHY: Task 4's lecture requirement is a Tree (BST) for ranking / searching hospitals by
//      suitability. After the weighted score is computed, each feasible hospital is inserted
//      here so the best hospital is the maximum key and a descending in-order walk yields
//      the ranked list for the dispatcher.

// HOW: Standard BST: left child < node < right child. The comparison key is (score, hospitalId)
//      so equal scores still have a unique, deterministic position. Reverse in-order traversal
//      (right, node, left) returns hospitals from best score to worst. Average insert is O(log n);
//      a degenerate tree is O(n), which is acceptable for city-scale hospital counts.

package com.mediroute.mediroutebackend.recommendation;

import com.mediroute.mediroutebackend.recommendation.model.RankedHospital;

import java.util.ArrayList;
import java.util.List;

public class SuitabilityBst {

    private Node root;

    private static class Node {
        final RankedHospital hospital;
        Node left;
        Node right;

        Node(RankedHospital hospital) {
            this.hospital = hospital;
        }
    }

    public void insert(RankedHospital hospital) {
        root = insertRecursive(root, hospital);
    }

    private Node insertRecursive(Node current, RankedHospital hospital) {
        if (current == null) {
            return new Node(hospital);
        }
        if (compare(hospital, current.hospital) < 0) {
            current.left = insertRecursive(current.left, hospital);
        } else {
            current.right = insertRecursive(current.right, hospital);
        }
        return current;
    }

    public RankedHospital findBest() {
        if (root == null) {
            return null;
        }
        Node current = root;
        while (current.right != null) {
            current = current.right;
        }
        return current.hospital;
    }

    public List<RankedHospital> toRankedList() {
        List<RankedHospital> ranked = new ArrayList<>();
        reverseInOrder(root, ranked);
        return ranked;
    }

    public boolean isEmpty() {
        return root == null;
    }

    private void reverseInOrder(Node current, List<RankedHospital> output) {
        if (current == null) {
            return;
        }
        reverseInOrder(current.right, output);
        output.add(current.hospital);
        reverseInOrder(current.left, output);
    }

    private int compare(RankedHospital a, RankedHospital b) {
        int scoreCompare = Double.compare(a.getTotalScore(), b.getTotalScore());
        if (scoreCompare != 0) {
            return scoreCompare;
        }
        return Long.compare(b.getHospitalId(), a.getHospitalId());
    }
}
