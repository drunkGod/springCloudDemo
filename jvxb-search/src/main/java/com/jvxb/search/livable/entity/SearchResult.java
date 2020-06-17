package com.jvxb.search.livable.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 全文检索
 *
 * @param <T>
 * @author jvxb
 */
public class SearchResult<T> implements Serializable {

    private static final long serialVersionUID = -6503305221683864157L;

    private List<T> records;
    private long total;
    private long pageSize;
    private long pageNum;
    private Map<String, Map<String, List<String>>> highlights;

    public List<T> getRecords() {
        return records;
    }

    public void setRecords(List<T> records) {
        this.records = records;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public long getPageSize() {
        return pageSize;
    }

    public void setPageSize(long pageSize) {
        this.pageSize = pageSize;
    }

    public long getPageNum() {
        return pageNum;
    }

    public void setPageNum(long pageNum) {
        this.pageNum = pageNum;
    }

    public Map<String, Map<String, List<String>>> getHighlights() {
        return highlights;
    }

    public void setHighlights(Map<String, Map<String, List<String>>> highlights) {
        this.highlights = highlights;
    }

    public SearchResult(List<T> records, long total) {
        this.records = records;
        this.total = total;
    }

    public SearchResult(List<T> records, long total, long pageSize, long pageNum) {
        this.records = records;
        this.total = total;
        this.pageSize = pageSize;
        this.pageNum = pageNum;
    }

    public static SearchResult empty() {
        return new SearchResult(new ArrayList(), 0L, 1L, 10L);
    }
}
