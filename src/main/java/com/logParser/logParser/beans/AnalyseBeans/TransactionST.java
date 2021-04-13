package com.logParser.logParser.beans.AnalyseBeans;

import org.springframework.stereotype.Component;

@Component

public class TransactionST {
    private long roundId;
    private long operatorId;
    private String uid;
    private String gameType;
    private int tableId;
    private String seatId;
    private String date;
    private String betType;
    private double bet = 0;
    private double win = 0;
    private String currency;
    private double balance;
    private int errorCode;
    private String errorDescription;
    private String transactionId;
    private long timestamp;
    private int orderNum;
    private boolean correctPlace;

    public TransactionST(long roundId, long operatorId, String uid, String gameType, int tableId, String seatId, String date,
                         String betType, double bet, double win, String currency, double balance, int errorCode,
                         String errorDescription, String transactionId, long timestamp, int orderNum, boolean correctPlace) {
        this.roundId = roundId;
        this.operatorId = operatorId;
        this.uid = uid;
        this.gameType = gameType;
        this.tableId = tableId;
        this.seatId = seatId;
        this.date = date;
        this.betType = betType;
        this.bet = bet;
        this.win = win;
        this.currency = currency;
        this.balance = balance;
        this.errorCode = errorCode;
        this.errorDescription = errorDescription;
        this.transactionId = transactionId;
        this.timestamp = timestamp;
        this.orderNum = orderNum;
        this.correctPlace = correctPlace;
    }

    public TransactionST() {
    }

    public TransactionST(long operatorId, long roundId, String uid, String transactionId, int errorCode, String errorDescription) {
        this.operatorId = operatorId;
        this.roundId = roundId;
        this.uid = uid;
        this.transactionId = transactionId;
        this.errorCode = errorCode;
        this.errorDescription = errorDescription;
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

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getGameType() {
        return gameType;
    }

    public void setGameType(String gameType) {
        this.gameType = gameType;
    }

    public int getTableId() {
        return tableId;
    }

    public void setTableId(int tableId) {
        this.tableId = tableId;
    }

    public String getSeatId() {
        return seatId;
    }

    public void setSeatId(String seatId) {
        this.seatId = seatId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getBetType() {
        return betType;
    }

    public void setBetType(String betType) {
        this.betType = betType;
    }

    public double getBet() {
        return bet;
    }

    public void setBet(double bet) {
        this.bet = bet;
    }

    public double getWin() {
        return win;
    }

    public void setWin(double win) {
        this.win = win;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorDescription() {
        return errorDescription;
    }

    public void setErrorDescription(String errorDescription) {
        this.errorDescription = errorDescription;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isCorrectPlace() {
        return correctPlace;
    }

    public void setCorrectPlace(boolean correctPlace) {
        this.correctPlace = correctPlace;
    }

    public int getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(int orderNum) {
        this.orderNum = orderNum;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "roundId=" + roundId +
                ", operatorId=" + operatorId +
                ", uid='" + uid + '\'' +
                ", gameType='" + gameType + '\'' +
                ", tableId=" + tableId +
                ", seatId='" + seatId + '\'' +
                ", date=" + date +
                ", betType='" + betType + '\'' +
                ", bet=" + bet +
                ", win=" + win +
                ", currency='" + currency + '\'' +
                ", balance=" + balance +
                ", errorCode=" + errorCode +
                ", errorDescription='" + errorDescription + '\'' +
                ", transactionId='" + transactionId + '\'' +
                ", timestamp=" + timestamp +
                ", orderNum=" + orderNum +
                ", correctPlace=" + correctPlace +
                '}';
    }
}
