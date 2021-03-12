package com.logParser.logParser.beans.AnalyseBeans;

import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class UserST {
    private String uid;
    private ArrayList<Transaction>transactionsOriginal;
    private ArrayList<Transaction>transactionsSorted;
    private int numberOfBalanceJumps;
    private boolean lastBalanceCorrect;
    private boolean correctBalanceExists;

    public UserST(String uid, ArrayList<Transaction> transactionsOriginal, ArrayList<Transaction> transactionsSorted,
                  int numberOfBalanceJumps, boolean lastBalanceCorrect, boolean correctBalanceExists) {
        this.uid = uid;
        this.transactionsOriginal = transactionsOriginal;
        this.transactionsSorted = transactionsSorted;
        this.numberOfBalanceJumps = numberOfBalanceJumps;
        this.lastBalanceCorrect = lastBalanceCorrect;
        this.correctBalanceExists = correctBalanceExists;
    }

    public UserST() {
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public ArrayList<Transaction> getTransactionsOriginal() {
        return transactionsOriginal;
    }

    public void setTransactionsOriginal(ArrayList<Transaction> transactionsOriginal) {
        this.transactionsOriginal = transactionsOriginal;
    }

    public ArrayList<Transaction> getTransactionsSorted() {
        return transactionsSorted;
    }

    public void setTransactionsSorted(ArrayList<Transaction> transactionsSorted) {
        this.transactionsSorted = transactionsSorted;
    }

    public int getNumberOfBalanceJumps() {
        return numberOfBalanceJumps;
    }

    public void setNumberOfBalanceJumps(int numberOfBalanceJumps) {
        this.numberOfBalanceJumps = numberOfBalanceJumps;
    }

    public boolean isLastBalanceCorrect() {
        return lastBalanceCorrect;
    }

    public void setLastBalanceCorrect(boolean lastBalanceCorrect) {
        this.lastBalanceCorrect = lastBalanceCorrect;
    }

    public boolean isCorrectBalanceExists() {
        return correctBalanceExists;
    }

    public void setCorrectBalanceExists(boolean correctBalanceExists) {
        this.correctBalanceExists = correctBalanceExists;
    }

    @Override
    public String toString() {
        return "UserST{" +
                "uid='" + uid + '\'' +
                ", transactionsOriginal=" + transactionsOriginal +
                ", transactionsSorted=" + transactionsSorted +
                ", numberOfBalanceJumps=" + numberOfBalanceJumps +
                ", lastBalanceCorrect=" + lastBalanceCorrect +
                ", correctBalanceExists=" + correctBalanceExists +
                '}';
    }
}
