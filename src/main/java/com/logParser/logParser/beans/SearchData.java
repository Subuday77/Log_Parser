package com.logParser.logParser.beans;

import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class SearchData {
    private long additionalParam;
    private int searchType;
    private String logToParse;
    private ArrayList<Long> roundsToAnalyse;

    public SearchData(long additionalParam, int searchType, String logToParse, ArrayList<Long> roundsToAnalyse) {
        this.additionalParam = additionalParam;
        this.searchType = searchType;
        this.logToParse = logToParse;
        this.roundsToAnalyse = roundsToAnalyse;
    }

    public SearchData() {
    }

    public long getAdditionalParam() {
        return additionalParam;
    }

    public void setAdditionalParam(long additionalParam) {
        this.additionalParam = additionalParam;
    }

    public int getSearchType() {
        return searchType;
    }

    public void setSearchType(int searchType) {
        this.searchType = searchType;
    }

    public String getLogToParse() {
        return logToParse;
    }

    public void setLogToParse(String logToParse) {
        this.logToParse = logToParse;
    }

    public ArrayList<Long> getRoundsToAnalyse() {
        return roundsToAnalyse;
    }
    public void setRoundsToAnalyse(ArrayList<Long> roundsToAnalyse) {
        this.roundsToAnalyse = roundsToAnalyse;
    }
}
