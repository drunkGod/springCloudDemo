package com.jvxb.search.livable.entity;

import java.util.Map;

/**
 * @author jvxb
 * @since 2020-06-13
 */
public class EsDocument {

    String index;
    String type;
    Map document;

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Map getDocument() {
        return document;
    }

    public void setDocument(Map document) {
        this.document = document;
    }

    @Override
    public String toString() {
        return "EsDocument{" +
                "index='" + index + '\'' +
                ", type='" + type + '\'' +
                ", document=" + document +
                '}';
    }
}
