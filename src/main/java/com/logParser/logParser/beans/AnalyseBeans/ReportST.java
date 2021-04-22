package com.logParser.logParser.beans.AnalyseBeans;

import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class ReportST {
    private long operatorId;
    private long roundId;
    private String[] uids;

    public ReportST(long operatorId, long roundId, String[] uids) {
        this.operatorId = operatorId;
        this.roundId = roundId;
        this.uids = uids;
    }

    public ReportST() {
    }

    public long getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(long operatorId) {
        this.operatorId = operatorId;
    }

    public long getRoundId() {
        return roundId;
    }

    public void setRoundId(long roundId) {
        this.roundId = roundId;
    }

    public String[] getUids() {
        return uids;
    }

    public void setUids(String[] uids) {
        this.uids = uids;
    }

    @Override
    public String toString() {
        return "ReportST{" +
                "operatorId=" + operatorId +
                ", roundId=" + roundId +
                ", uids=" + Arrays.toString(uids) +
                '}';
    }
}
