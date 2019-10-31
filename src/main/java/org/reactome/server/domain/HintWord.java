package org.reactome.server.domain;

import java.util.Set;

public class HintWord {
    private Set<String> keywords;

    public HintWord(Set<String> keywords) {
        this.keywords = keywords;
    }

    public Set<String> getKeywords() {
        return keywords;
    }

    public void setKeywords(Set<String> keywords) {
        this.keywords = keywords;
    }

    @Override
    public String toString() {
        return "HintWord{" +
                "keywords=" + keywords +
                '}';
    }
}