package com.logParser.logParser.beans.AnalyseBeans;

import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class OperatorST {
    private long operatorId;
    private ArrayList<Round> rounds;

    public OperatorST(long operatorId, ArrayList<Round> rounds) {
        this.operatorId = operatorId;
        this.rounds = rounds;
    }

    public OperatorST() {
    }

    public long getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(long operatorId) {
        this.operatorId = operatorId;
    }

    public ArrayList<Round> getRounds() {
        return rounds;
    }

    public void setRounds(ArrayList<Round> rounds) {
        this.rounds = rounds;
    }

    @Override
    public String toString() {
        return "OperatorST{" +
                "operatorId=" + operatorId +
                ", rounds=" + rounds +
                '}';
    }
}
