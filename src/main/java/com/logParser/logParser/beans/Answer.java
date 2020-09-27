package com.logParser.logParser.beans;


import org.springframework.stereotype.Component;

@Component
public class Answer {
    private long operatorId;
    private long roundId;
    private String initialToken;
    private String sessionToken;
    private String uid;
    private double balance;
    private String currency;

    public Answer(long operatorId, long roundId, String initialToken, String sessionToken, String uid, double balance, String currency) {
        this.operatorId = operatorId;
        this.roundId = roundId;
        this.initialToken = initialToken;
        this.sessionToken = sessionToken;
        this.uid = uid;
        this.balance = balance;
        this.currency = currency;
    }

    public Answer() {
    }

    public long getRoundId() {
        return roundId;
    }

    public void setRoundId(long roundId) {
        this.roundId = roundId;
    }

    public long getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(long operatorId) {
        this.operatorId = operatorId;
    }

    public String getInitialToken() {
        return initialToken;
    }

    public void setInitialToken(String initialToken) {
        this.initialToken = initialToken;
    }

    public String getSessionToken() {
        return sessionToken;
    }

    public void setSessionToken(String sessionToken) {
        this.sessionToken = sessionToken;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
