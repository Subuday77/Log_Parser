package com.logParser.logParser.beans;

import org.springframework.stereotype.Component;

@Component
public class SearchData {
    private long operatorId;
    private int searchType;
    private String logToParse;

    public SearchData(long operatorId, int searchType, String logToParse) {
        this.operatorId = operatorId;
        this.searchType = searchType;
        this.logToParse = logToParse;
    }

    public SearchData() {
    }

    public long getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(long operatorId) {
        this.operatorId = operatorId;
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
}
