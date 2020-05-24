package org.reactome.server.domain;

import java.util.Set;

public class DiseaseNameHintWord {
    private Set<String> names;

    public DiseaseNameHintWord(Set<String> keywords) {
        this.names = keywords;
    }

    public Set<String> getNames() {
        return names;
    }

    public void setNames(Set<String> names) {
        this.names = names;
    }

    @Override
    public String toString() {
        return "DiseaseNameHintWord{" +
                "names=" + names +
                '}';
    }
}