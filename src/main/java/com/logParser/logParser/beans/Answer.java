package com.logParser.logParser.beans;

import org.springframework.stereotype.Component;

@Component
public class Answer {
    private String operatorId;
    private String initialToken;
    private String sessionToken;
    private String uid;
    private String balance;
    private String currency;

    public Answer(String operatorId, String initialToken, String sessionToken, String uid, String balance, String currency) {
        this.operatorId = operatorId;

        this.initialToken = initialToken;
        this.sessionToken = sessionToken;
        this.uid = uid;
        this.balance = balance;
        this.currency = currency;
    }

    public Answer() {
    }



    public String getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(String operatorId) {
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

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
