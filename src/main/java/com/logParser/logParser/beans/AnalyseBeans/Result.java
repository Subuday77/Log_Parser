package com.logParser.logParser.beans.AnalyseBeans;


import org.springframework.stereotype.Component;

@Component
public class Result {
    private OperatorST operatorST;
    private int numberOfLines;
    private int numberOfTransactions;
    private int fastestAnswerTime;
    private int slowestAnswerTime;
    private int averageAnswerTime;

    public Result(OperatorST operatorST, int numberOfLines, int numberOfTransactions, int fastestAnswerTime, int slowestAnswerTime, int averageAnswerTime) {
        this.operatorST = operatorST;
        this.numberOfLines = numberOfLines;
        this.numberOfTransactions = numberOfTransactions;
        this.fastestAnswerTime = fastestAnswerTime;
        this.slowestAnswerTime = slowestAnswerTime;
        this.averageAnswerTime = averageAnswerTime;
    }

    public Result() {
    }

    public OperatorST getOperatorST() {
        return operatorST;
    }

    public void setOperatorST(OperatorST operatorST) {
        this.operatorST = operatorST;
    }

    public int getNumberOfLines() {
        return numberOfLines;
    }

    public void setNumberOfLines(int numberOfLines) {
        this.numberOfLines = numberOfLines;
    }

    public int getNumberOfTransactions() {
        return numberOfTransactions;
    }

    public void setNumberOfTransactions(int numberOfTransactions) {
        this.numberOfTransactions = numberOfTransactions;
    }

    public int getFastestAnswerTime() {
        return fastestAnswerTime;
    }

    public void setFastestAnswerTime(int fastestAnswerTime) {
        this.fastestAnswerTime = fastestAnswerTime;
    }

    public int getSlowestAnswerTime() {
        return slowestAnswerTime;
    }

    public void setSlowestAnswerTime(int slowestAnswerTime) {
        this.slowestAnswerTime = slowestAnswerTime;
    }

    public int getAverageAnswerTime() {
        return averageAnswerTime;
    }

    public void setAverageAnswerTime(int averageAnswerTime) {
        this.averageAnswerTime = averageAnswerTime;
    }

    @Override
    public String toString() {
        return "Result{" +
                "operatorST=" + operatorST +
                ", numberOfLines=" + numberOfLines +
                ", numberOfTransactions=" + numberOfTransactions +
                ", fastestAnswerTime=" + fastestAnswerTime +
                ", slowestAnswerTime=" + slowestAnswerTime +
                ", averageAnswerTime=" + averageAnswerTime +
                '}';
    }
}
