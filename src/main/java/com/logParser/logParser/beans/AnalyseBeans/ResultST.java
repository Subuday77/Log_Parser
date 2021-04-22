package com.logParser.logParser.beans.AnalyseBeans;


import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class ResultST  {
    private int transactionsCount;
    private ArrayList<RoundST> rounds;

    public ResultST(int transactionsCount, ArrayList<RoundST> rounds) {
        this.transactionsCount = transactionsCount;
        this.rounds = rounds;
    }

    public ResultST() {
    }

    public ArrayList<RoundST> getRounds() {
        return rounds;
    }

    public void setRounds(ArrayList<RoundST> roundsTS) {
        this.rounds = rounds;
    }

    public int getTransactionsCount() {
        return transactionsCount;
    }

    public void setTransactionsCount(int transactionsCount) {
        this.transactionsCount = transactionsCount;
    }

    @Override
    public String toString() {
        return "ResultST{" +
                "transactionsCount=" + transactionsCount +
                ", rounds=" + rounds +
                '}';
    }
}
