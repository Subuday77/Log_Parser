package com.logParser.logParser.beans.AnalyseBeans;

import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class Round {
    private long roundId;
    private ArrayList<UserST> usersST;

    public Round(long roundId, ArrayList<UserST> usersST) {
        this.roundId = roundId;
        this.usersST = usersST;
    }

    public Round() {
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

    @Override
    public String toString() {
        return "Round{" +
                "roundId=" + roundId +
                ", usersST=" + usersST +
                '}';
    }
}
