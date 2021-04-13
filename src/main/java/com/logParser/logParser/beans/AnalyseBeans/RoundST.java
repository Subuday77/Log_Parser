package com.logParser.logParser.beans.AnalyseBeans;

import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class RoundST {
    private long roundId;
    private int usersCount;
    private int transactionsCount;
    private ArrayList<UserST> usersST;

    public RoundST(long roundId, int usersCount, int transactionsCount, ArrayList<UserST> usersST) {
        this.roundId = roundId;
        this.usersCount = usersCount;
        this.transactionsCount = transactionsCount;
        this.usersST = usersST;
    }

    public RoundST() {
    }

    public long getRoundId() {
        return roundId;
    }

    public void setRoundId(long roundId) {
        this.roundId = roundId;
    }

    public ArrayList<UserST> getUsersST() {
        return usersST;
    }

    public void setUsersST(ArrayList<UserST> usersST) {
        this.usersST = usersST;
    }


    public int getTransactionsCount() {
        return transactionsCount;
    }

    public void setTransactionsCount(int transactionsCount) {
        this.transactionsCount = transactionsCount;
    }

    public int getUsersCount() {
        return usersCount;
    }

    public void setUsersCount(int usersCount) {
        this.usersCount = usersCount;
    }

    @Override
    public String toString() {
        return "Round{" +
                "roundId=" + roundId +
                ", usersCount=" + usersCount +
                ", transactionsCount=" + transactionsCount +
                ", usersST=" + usersST +
                '}';
    }
}
