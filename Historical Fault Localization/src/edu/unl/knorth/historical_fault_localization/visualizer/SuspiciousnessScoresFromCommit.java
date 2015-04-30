package edu.unl.knorth.historical_fault_localization.visualizer;

import edu.unl.knorth.historical_fault_localization.suspiciousness_calculation.SuspiciousnessScore;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class SuspiciousnessScoresFromCommit {
    private final List<SuspiciousnessScore> suspiciousnessScores;
    private final String commitHash;
    private final int orderProcessed;

    public SuspiciousnessScoresFromCommit(String commitHash, int orderProcessed,
            Collection<SuspiciousnessScore> suspiciousnessScores) {
        this.suspiciousnessScores = new ArrayList<>(suspiciousnessScores);
        this.commitHash = commitHash;
        this.orderProcessed = orderProcessed;
    }

    public SuspiciousnessScoresFromCommit(String commitHash, int orderProcessed,
            SuspiciousnessScore... suspiciousnessScores) {
        this.suspiciousnessScores =
                new ArrayList<>(Arrays.asList(suspiciousnessScores));
        this.commitHash = commitHash;
        this.orderProcessed = orderProcessed;
    }
    
    public List<SuspiciousnessScore> getSuspiciousnessScores() {
        return suspiciousnessScores;
    }

    public String getCommitHash() {
        return commitHash;
    }

    public int getOrderProcessed() {
        return orderProcessed;
    }
}