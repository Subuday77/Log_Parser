package com.logParser.logParser.beans;

import org.springframework.stereotype.Component;

@Component
public class SearchData {
    private long additionalParam;
    private int searchType;
    private String logToParse;

    public SearchData(long additionalParam, int searchType, String logToParse) {
        this.additionalParam = additionalParam;
        this.searchType = searchType;
        this.logToParse = logToParse;
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
}
