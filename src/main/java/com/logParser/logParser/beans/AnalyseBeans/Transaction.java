package com.logParser.logParser.beans.AnalyseBeans;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@JsonInclude(Include.NON_DEFAULT)
public class Transaction {
    private long roundId;
    private long operatorId;
    private String uid;
    private String gameType;
    private int tableId;
    private String seatId;
    private Date date;
    private String betType;
    @JsonInclude(Include.ALWAYS)
    private double bet = 0;
    @JsonInclude(Include.ALWAYS)
    private double win = 0;
    private String currency;
    private double balance;
    private int errorCode;
    private String internalError;
    private String transactionId;
    private long timestamp;

    public Transaction(long roundId, long operatorId, String uid, String gameType, int tableId, String seatId, Date date,
                       String betType, double bet, double win, String currency, double balance, int errorCode,
                       String internalError, String transactionId, long timestamp) {
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
        this.internalError = internalError;
        this.transactionId = transactionId;
        this.timestamp = timestamp;
    }

    public Transaction() {
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
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

    public String getInternalError() {
        return internalError;
    }

    public void setInternalError(String internalError) {
        this.internalError = internalError;
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
                ", internalError='" + internalError + '\'' +
                ", transactionId='" + transactionId + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
