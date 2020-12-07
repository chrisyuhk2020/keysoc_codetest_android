package com.keysoc.myapplication.Entity;

import java.util.ArrayList;
import java.util.List;

public class ResultData {
    public int getResultCount() {
        return resultCount;
    }

    public void setResultCount(int resultCount) {
        this.resultCount = resultCount;
    }

    public List<ResultDataItem> getResults() {
        return results;
    }

    public void setResults(List<ResultDataItem> results) {
        this.results = results;
    }

    private int resultCount;
    private List<ResultDataItem> results = new ArrayList<>();
}
