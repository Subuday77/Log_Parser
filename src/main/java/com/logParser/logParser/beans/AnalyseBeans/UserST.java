package com.logParser.logParser.beans.AnalyseBeans;

import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class UserST {
    private String uid;
    private int transactionsCount;
    private int debitsCount;
    private int creditsCount;
    private double expectedBalance;
    private double lastShownBalance;
    private double lastReturnedBalance;
    private String sortArrowsTime = "login";
    private ArrayList<TransactionST> transactionsST;
    private ArrayList<TransactionST> issuedTransactionsST;

    public UserST() {
    }

    public UserST(String uid, int transactionsCount, int debitsCount, int creditsCount, double expectedBalance,
                  double lastShownBalance, double lastReturnedBalance, ArrayList<TransactionST> transactionsST, ArrayList<TransactionST> issuedTransactionsST) {
        this.uid = uid;
        this.transactionsCount = transactionsCount;
        this.debitsCount = debitsCount;
        this.creditsCount = creditsCount;
        this.expectedBalance = expectedBalance;
        this.lastShownBalance = lastShownBalance;
        this.lastReturnedBalance = lastReturnedBalance;
        this.transactionsST = transactionsST;
        this.issuedTransactionsST = issuedTransactionsST;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public int getDebitsCount() {
        return debitsCount;
    }

    public void setDebitsCount(int debitsCount) {
        this.debitsCount = debitsCount;
    }

    public int getCreditsCount() {
        return creditsCount;
    }

    public void setCreditsCount(int creditsCount) {
        this.creditsCount = creditsCount;
    }

    public double getExpectedBalance() {
        return expectedBalance;
    }

    public void setExpectedBalance(double expectedBalance) {
        this.expectedBalance = expectedBalance;
    }

    public double getLastShownBalance() {
        return lastShownBalance;
    }

    public void setLastShownBalance(double lastShownBalance) {
        this.lastShownBalance = lastShownBalance;
    }

    public double getLastReturnedBalance() {
        return lastReturnedBalance;
    }

    public void setLastReturnedBalance(double lastReturnedBalance) {
        this.lastReturnedBalance = lastReturnedBalance;
    }

    public ArrayList<TransactionST> getTransactions() {
        return transactionsST;
    }

    public void setTransactions(ArrayList<TransactionST> transactionsTS) {
        this.transactionsST = transactionsTS;
    }

    public int getTransactionsCount() {
        return transactionsCount;
    }

    public void setTransactionsCount(int transactionsCount) {
        this.transactionsCount = transactionsCount;
    }

    public ArrayList<TransactionST> getIssuedTransactions() {
        return issuedTransactionsST;
    }

    public void setIssuedTransactions(ArrayList<TransactionST> issuedTransactionsTS) {
        this.issuedTransactionsST = issuedTransactionsTS;
    }

    @Override
    public String toString() {
        return "UserST{" +
                "uid='" + uid + '\'' +
                ", transactionsCount=" + transactionsCount +
                ", debitsCount=" + debitsCount +
                ", creditsCount=" + creditsCount +
                ", expectedBalance=" + expectedBalance +
                ", lastShownBalance=" + lastShownBalance +
                ", lastReturnedBalance=" + lastReturnedBalance +
                ", transactions=" + transactionsST +
                ", issuedTransactions=" + issuedTransactionsST +
                '}';
    }

    public String getSortArrowsTime() {
        return sortArrowsTime;
    }

    public void setSortArrowsTime(String sortArrowsTime) {
        this.sortArrowsTime = sortArrowsTime;
    }
}
