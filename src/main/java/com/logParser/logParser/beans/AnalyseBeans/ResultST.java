package com.logParser.logParser.beans.AnalyseBeans;


import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class ResultST {
    private int transactionsCount;
    private ArrayList<RoundST> roundsST;

    public ResultST(int transactionsCount, ArrayList<RoundST> roundsST) {
        this.transactionsCount = transactionsCount;
        this.roundsST = roundsST;
    }

    public ResultST() {
    }

    public ArrayList<RoundST> getRounds() {
        return roundsST;
    }

    public void setRounds(ArrayList<RoundST> roundsTS) {
        this.roundsST = roundsST;
    }

    public int getTransactionsCount() {
        return transactionsCount;
    }

    public void setTransactionsCount(int transactionsCount) {
        this.transactionsCount = transactionsCount;
    }

    @Override
    public String toString() {
        return "Result{" +
                "transactionsCount=" + transactionsCount +
                ", roundsST=" + roundsST +
                '}';
    }
}
